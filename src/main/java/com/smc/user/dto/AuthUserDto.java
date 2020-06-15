package com.smc.user.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class AuthUserDto implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Boolean active;
    private Set<RoleDto> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<ResourceDto> resources = new HashSet<>();

        roles.forEach(role -> resources.addAll(role.getResources()));
        return resources;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
