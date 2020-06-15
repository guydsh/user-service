package com.smc.user.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class ResourceDto implements GrantedAuthority {
    private Long id;
    private String name;
    private String url;

    @Override
    public String getAuthority() {
        return url;
    }
}
