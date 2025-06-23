package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 100 characters")
    @Column(name = "type_name", unique = true, nullable = false)
    private String typeName;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 100, message = "Description must be less than 500 characters")
    @Column(name = "description")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
