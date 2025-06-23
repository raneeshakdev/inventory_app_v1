package com.svym.inventory.service.eventdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.EventDetail;

public interface EventDetailRepository extends JpaRepository<EventDetail, Long> {
}
