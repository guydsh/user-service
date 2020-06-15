package com.smc.user.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IRbacService {
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
