package com.smc.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
public class RoleVo {
    @NotBlank(message = "name must not be blank")
    private String name;

    private Set<Long> resourceIds = new HashSet<>();
}
