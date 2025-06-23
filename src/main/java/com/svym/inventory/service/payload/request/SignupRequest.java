package com.svym.inventory.service.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
  
  @NotBlank
  @Size(min = 2, max = 50)
  private String firstName;
  
  @NotBlank
  @Size(min = 1, max = 50)
  private String lastName; 

}
