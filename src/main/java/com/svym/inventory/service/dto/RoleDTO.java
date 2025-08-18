package com.svym.inventory.service.dto;

import com.svym.inventory.service.entity.ERole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Integer id;

    private ERole name;

    private String displayName;
}
