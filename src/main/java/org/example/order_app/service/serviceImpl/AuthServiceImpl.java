package org.example.order_app.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.LoginRequestDTO;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.JwtResponseDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.User;
import org.example.order_app.exception.UserIsAlreadyTakenException;
import org.example.order_app.mapper.AuthMapper;
import org.example.order_app.mapper.UserMapper;
import org.example.order_app.repository.UserRepository;
import org.example.order_app.security.JwtUtils;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO request) {
        validateUsername(request.getUsername());
        User user = authMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public JwtResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER")
                .replace("ROLE_", "");

        return JwtResponseDTO.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .role(role)
                .build();
    }

    private void validateUsername(String username){
        if (userRepository.existsByUsername(username)) {
            throw new UserIsAlreadyTakenException("Username is already taken" + username);
        }
    }
}
