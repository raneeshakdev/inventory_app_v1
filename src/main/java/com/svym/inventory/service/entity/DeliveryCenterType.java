package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Represents a type of delivery center in the inventory system.
 * This entity contains details about the delivery center type such as its name, description,
 * and the timestamp when it was created.
 * This entity is mapped to the "delivery_center_types" table in the database.
 */

@Entity
@Table(name = "delivery_center_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCenterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
