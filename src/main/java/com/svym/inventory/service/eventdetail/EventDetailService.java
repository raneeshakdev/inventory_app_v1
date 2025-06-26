package com.svym.inventory.service.eventdetail;

import java.util.List;

import com.svym.inventory.service.dto.EventDetailDTO;

public interface EventDetailService {
    EventDetailDTO createEvent(EventDetailDTO dto);
    List<EventDetailDTO> getAllEvents();
    EventDetailDTO getEventById(Long id);
    EventDetailDTO updateEvent(Long id, EventDetailDTO dto);
    void deleteEvent(Long id);
}
