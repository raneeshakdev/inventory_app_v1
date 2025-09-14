package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

/**
 * Entity representing the medicine_action_items_details view.
 * This view combines medicine action items with medicine details.
 * This is a read-only view entity.
 */
@Entity
@Table(name = "medicine_action_items_details")
@Immutable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineActionItemsDetails {

    @Id
    @Column(name = "action_item_id")
    private Long actionItemId;

    @Column(name = "medicine_id")
    private Long medicineId;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "stock_check_details")
    private String stockCheckDetails;

    @Column(name = "date_of_action_generated")
    private LocalDateTime dateOfActionGenerated;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "type_id")
    private Long typeId;
}
