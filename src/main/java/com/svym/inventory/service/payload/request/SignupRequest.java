package com.svym.inventory.service.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  private String password;
  
  @NotBlank
  @Size(min = 2, max = 50)
  private String firstName;
  
  @NotBlank
  @Size(min = 1, max = 50)
  private String lastName; 
  
  private Set<String> roles;

}
