package com.smc.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserProfileVo {

    @NotBlank(message = "contact number must not be blank")
    private String contactNumber;

    @NotBlank(message = "email must not be blank")
    private String email;

}
