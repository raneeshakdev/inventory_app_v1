package com.svym.inventory.service.eventdetail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.svym.inventory.service.dto.EventDetailDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventDetailController {

    private final EventDetailService service;

    @PostMapping
    public ResponseEntity<EventDetailDTO> create(@Valid @RequestBody EventDetailDTO dto) {
        return ResponseEntity.status(201).body(service.createEvent(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEventById(id));
    }

    @GetMapping
    public ResponseEntity<List<EventDetailDTO>> getAll() {
        return ResponseEntity.ok(service.getAllEvents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDetailDTO> update(@PathVariable Long id, @Valid @RequestBody EventDetailDTO dto) {
        return ResponseEntity.ok(service.updateEvent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
