package com.svym.inventory.service.eventdetail;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.EventDetailDTO;
import com.svym.inventory.service.entity.EventDetail;
import com.svym.inventory.service.entity.mapper.EventDetailMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {

    private final EventDetailRepository repository;
    private final EventDetailMapper mapper;

    @Override
    public EventDetailDTO createEvent(EventDetailDTO dto) {
        EventDetail entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public EventDetailDTO getEventById(Long id) {
        EventDetail entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapper.toDto(entity);
    }

    @Override
    public List<EventDetailDTO> getAllEvents() {
        return repository.findAll().stream()
                .map(event -> mapper.toDto(event))
                .collect(Collectors.toList());
    }

    @Override
    public EventDetailDTO updateEvent(Long id, EventDetailDTO dto) {
        @SuppressWarnings("unused")
		EventDetail existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }
}
