package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a type of purchase in the inventory system.
 * 
 * This entity contains details about the purchase type such as its name, description,
 * and the timestamp when it was created.
 * This entity is mapped to the "purchase_types" table in the database.
 */

@Entity
@Table(name = "medicine_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineType {

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
