package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Represents action items related to medicines in the inventory system.
 * This entity tracks various actions like expired medicines, out of stock situations,
 * and critically low stock levels at specific locations.
 * This entity is mapped to the "medicine_action_items" table in the database.
 */
@Entity
@Table(name = "medicine_action_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineActionItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType;

    @Column(name = "stock_check_details", length = 255)
    private String stockCheckDetails;

    @Column(name = "date_of_action_generated", nullable = false)
    private LocalDateTime dateOfActionGenerated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
