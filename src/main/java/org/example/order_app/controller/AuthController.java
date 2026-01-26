package org.example.order_app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.LoginRequestDTO;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.JwtResponseDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.Role;
import org.example.order_app.entity.User;
import org.example.order_app.security.JwtUtils;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid
                                                @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(JwtResponseDTO.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .role(roles.get(0).replace("ROLE_", ""))
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid
                                                    @RequestBody RegisterRequestDTO registerRequest) {
        User user = authService.registerUser(registerRequest);
        return ResponseEntity.ok(UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(UserResponseDTO.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .role(Role.valueOf(
                        userDetails.getAuthorities().iterator().next()
                                .getAuthority().replace("ROLE_", "")))
                .build());
    }

}
