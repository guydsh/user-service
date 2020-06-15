package com.smc.user.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String contactNumber;
    private String email;
    private Boolean active;
    private Set<RoleDto> roles;
}
