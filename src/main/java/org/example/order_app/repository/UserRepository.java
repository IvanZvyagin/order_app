package org.example.order_app.repository;

import org.example.order_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
//поиск пользователя по имени
    Optional<User> findByUsername(String username);
//проверка на существование пользователя с таким именем
    boolean existsByUsername(String username);
}
