package com.smc.user.controller;

import com.smc.user.dto.ResponseResult;
import com.smc.user.dto.UserDto;
import com.smc.user.entity.VerificationToken;
import com.smc.user.service.IRoleService;
import com.smc.user.service.IUserService;
import com.smc.user.service.IVerificationTokenService;
import com.smc.user.vo.RegistrationVo;
import com.smc.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/registration")
public class RegistrationController {

    @Autowired
    private IVerificationTokenService tokenService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping
    public ResponseResult<UserDto> register(@Valid @RequestBody RegistrationVo registrationVo) {
    	System.out.println("Come in register");

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(registrationVo, userVo);

        // make sure user can only register USER role account
        Long userRoleId = roleService.findRoleIdByRoleName("USER");
        if (userRoleId != null) {
            Set<Long> roleIds = new HashSet<>();
            roleIds.add(userRoleId);
            userVo.setRoleIds(roleIds);
        }
        
        System.out.println("userRoleId="+userRoleId);

        UserDto user = userService.addUser(userVo);

        VerificationToken verificationToken = tokenService.save(user.getId());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Complete Registration");
        email.setText("To confirm your account, please click here : "
            + "http://localhost:4200/account/verification?token="
            + verificationToken.getToken());

        javaMailSender.send(email);

        return ResponseResult.success("Account created successfully", user);
    }

    @PostMapping(value = "/confirmation")
    public ResponseResult confirmRegistration(@RequestParam(value = "token") String token) {
        VerificationToken verificationToken = tokenService.findByToken(token);
        if (verificationToken == null ||
                (verificationToken != null && verificationToken.getUser() == null)) {
            return ResponseResult.fail("User has been activated or the token is invalid");
        } else {
            userService.updateUserActive(verificationToken.getUser().getId(), true);
            return ResponseResult.success("User is activated", null);
        }
    }

}
