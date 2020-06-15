package com.smc.user.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smc.user.dto.AuthUserDto;
import com.smc.user.dto.ResponseResult;
import com.smc.user.service.impl.UserDetailsServiceImpl;
import com.smc.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        AuthUserDto user = userDetailsService.loadUserByUsername(((AuthUserDto) authentication.getPrincipal()).getUsername());

        Map<String, Object> result = new HashMap<>();
        String message;
        boolean successful = true;
        if (user == null) {
            message = "User not registered";
            successful = false;
        } else if (!user.getActive().booleanValue()) {
            message = "User is not active";
            successful = false;
        } else {
            message = "Authentication succeed";
            result.put("Authorization", "Bearer " + jwtUtil.generateToken(user));
        }

        if (successful) {
            response.getWriter().write(
                    new ObjectMapper().
                            writeValueAsString(
                                    ResponseResult.success(message, result)
                            )
            );
        } else {
            response.getWriter().write(
                    new ObjectMapper().
                            writeValueAsString(
                                    ResponseResult.fail(message)
                            )
            );
        }
    }
}
