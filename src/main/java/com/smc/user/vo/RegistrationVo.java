package com.smc.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class RegistrationVo {
    @NotBlank(message = "username must not be blank")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;

    @NotBlank(message = "contact number must not be blank")
    private String contactNumber;

    @NotBlank(message = "email must not be blank")
    private String email;
}
