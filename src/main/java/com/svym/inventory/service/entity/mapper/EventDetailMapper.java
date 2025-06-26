package com.svym.inventory.service.entity.mapper;

import org.springframework.stereotype.Component;

import com.svym.inventory.service.dto.EventDetailDTO;
import com.svym.inventory.service.entity.EventDetail;

@Component
public class EventDetailMapper {

    public static EventDetailDTO toDTO(EventDetail entity) {
        EventDetailDTO dto = new EventDetailDTO();
        dto.setId(entity.getId());
        dto.setEventName(entity.getEventName());
        dto.setEventDate(entity.getEventDate());
        dto.setLocationId(entity.getLocation() != null ? entity.getLocation().getId() : null);
        dto.setEventDescription(entity.getEventDescription());
        dto.setTotalParticipants(entity.getTotalParticipants());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public static EventDetail toEntity(EventDetailDTO dto) {
        EventDetail entity = new EventDetail();
        entity.setId(dto.getId());
        entity.setEventName(dto.getEventName());
        entity.setEventDate(dto.getEventDate());
        entity.setLocation(null);
        entity.setEventDescription(dto.getEventDescription());
        entity.setTotalParticipants(dto.getTotalParticipants());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return entity;
    }
}
