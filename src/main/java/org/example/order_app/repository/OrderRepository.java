package org.example.order_app.repository;

import org.example.order_app.entity.Order;
import org.example.order_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Order> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Order> findAllByUser_Id(UUID user, Pageable pageable);
}
