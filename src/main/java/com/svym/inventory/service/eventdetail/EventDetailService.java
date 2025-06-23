package com.svym.inventory.service.eventdetail;

import java.util.List;

import com.svym.inventory.service.dto.EventDetailDTO;

public interface EventDetailService {
    EventDetailDTO createEvent(EventDetailDTO dto);
    EventDetailDTO getEventById(Long id);
    List<EventDetailDTO> getAllEvents();
    EventDetailDTO updateEvent(Long id, EventDetailDTO dto);
    void deleteEvent(Long id);
}
