package com.svym.inventory.service.eventdetail;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.dto.EventDetailDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@Validated
@RequiredArgsConstructor
public class EventDetailController {

	private final EventDetailService service;

	@PostMapping
	public ResponseEntity<EventDetailDTO> createEvent(@Valid @RequestBody EventDetailDTO dto) {
		return new ResponseEntity<>(service.createEvent(dto), HttpStatus.CREATED);
	}

	@GetMapping
	public List<EventDetailDTO> getAllEvents() {
		return service.getAllEvents();
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventDetailDTO> getEventById(@PathVariable Long id) {
		return ResponseEntity.ok(service.getEventById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EventDetailDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDetailDTO dto) {
		return ResponseEntity.ok(service.updateEvent(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
		service.deleteEvent(id);
		return ResponseEntity.noContent().build();
	}
}
