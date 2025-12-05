package com.svym.inventory.service.userlocation;

import com.svym.inventory.service.dto.UserLocationDTO;
import com.svym.inventory.service.dto.UserLocationUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-locations")
public class UserLocationController {

    private final UserLocationService service;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserLocationDTO>> getUserLocationMappings(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserLocationMappings(userId));
    }

    @PutMapping
    public ResponseEntity<List<UserLocationDTO>> updateUserLocationMappings(
            @Valid @RequestBody UserLocationUpdateRequest request) {
        return ResponseEntity.ok(service.updateUserLocationMappings(request));
    }
}

