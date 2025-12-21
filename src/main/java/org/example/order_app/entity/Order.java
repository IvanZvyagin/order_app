package org.example.order_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @ToString.Exclude
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    //todo добавить product
    //todo номер заказа добавить

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;

    @CreationTimestamp
    @NotNull
    @Column(name = "created_at", updatable = false)
    private Instant cratedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}

//todo сделать колонку в БД под апдейт