package com.smc.user.service;

import com.smc.user.entity.VerificationToken;

public interface IVerificationTokenService {
    VerificationToken save(Long userId);
    VerificationToken findByToken(String token);
}
