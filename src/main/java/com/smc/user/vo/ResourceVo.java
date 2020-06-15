package com.smc.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResourceVo {
    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "url must not be blank")
    private String url;
}
