package com.svym.inventory.service.payload.response;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private List<String> roles;

  public JwtResponse(String token, Long id, String firstName, String lastName, String email,
		List<String> roles) {
	super();
	this.token = token;
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	this.username = email;
	this.email = email;
	this.roles = roles;
}
  
}
