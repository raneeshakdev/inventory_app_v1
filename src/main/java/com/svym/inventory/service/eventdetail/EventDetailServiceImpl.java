package com.svym.inventory.service.eventdetail;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.EventDetailDTO;
import com.svym.inventory.service.entity.EventDetail;
import com.svym.inventory.service.entity.mapper.EventDetailMapper;
import com.svym.inventory.service.location.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventDetailServiceImpl implements EventDetailService {

    private final EventDetailRepository repository;
    private final LocationRepository locationRepository;

    @Override
    public EventDetailDTO createEvent(EventDetailDTO dto) {
        EventDetail entity = EventDetailMapper.toEntity(dto);
        if (dto.getLocationId() != null) {
			entity.setLocation(locationRepository.findById(dto.getLocationId())
					.orElseThrow(() -> new RuntimeException("Location not found")));
		}
        return EventDetailMapper.toDTO(repository.save(entity));
    }

    @Override
    public List<EventDetailDTO> getAllEvents() {
        return repository.findAll()
                .stream()
                .map(EventDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventDetailDTO getEventById(Long id) {
        EventDetail event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return EventDetailMapper.toDTO(event);
    }

    @Override
    public EventDetailDTO updateEvent(Long id, EventDetailDTO dto) {
        EventDetail existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        existing.setEventName(dto.getEventName());
        existing.setEventDate(dto.getEventDate());
        if (dto.getLocationId() != null) {
			existing.setLocation(locationRepository.findById(dto.getLocationId())
					.orElseThrow(() -> new RuntimeException("Location not found")));
		} else {
			existing.setLocation(null);
		}
        existing.setTotalParticipants(dto.getTotalParticipants());
        existing.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        existing.setEventDescription(dto.getEventDescription());

        return EventDetailMapper.toDTO(repository.save(existing));
    }

    @Override
    public void deleteEvent(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        repository.deleteById(id);
    }
}
