package org.example.order_app.repository;

import org.example.order_app.entity.Order;
import org.example.order_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * прочитать про pageble
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByUser(User user);

    Page<Order> findAllByUser(User user, Pageable pageable);

    Page<Order> findAll(Pageable pageable);
}
