package com.svym.inventory.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.svym.inventory.service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String username);

  Boolean existsByEmail(String email);

Object existsByFirstNameAndLastName(String firstName, String lastName);
}
