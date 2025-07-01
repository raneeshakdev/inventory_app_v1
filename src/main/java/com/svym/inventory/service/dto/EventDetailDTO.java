package com.svym.inventory.service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailDTO {

    private Long id;

    @NotBlank(message = "Event name is required")
    private String eventName;

    // Relationships)
    @NotNull(message = "Location ID is required")
    private Long locationId;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date must be in the future or present")
    private LocalDateTime eventDate;

    @NotBlank(message = "Event description is required")
    private String eventDescription;

    @NotNull(message = "Total Participants is required")
    private Integer totalParticipants;

    private Boolean isActive;
}
