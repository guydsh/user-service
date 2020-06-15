package com.smc.user.service.impl;

import com.smc.user.entity.User;
import com.smc.user.entity.VerificationToken;
import com.smc.user.repository.UserRepository;
import com.smc.user.repository.VerificationTokenRepository;
import com.smc.user.service.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements IVerificationTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository repository;

    @Override
    public VerificationToken save(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            return null;
        } else {
            User user = optionalUser.get();
            String token = UUID.randomUUID().toString();

            return repository.save(new VerificationToken(user, token));
        }
    }

    @Override
    public VerificationToken findByToken(String token) {
        Optional<VerificationToken> optionalVerificationToken = repository.findByToken(token);

        return optionalVerificationToken.orElse(null);
    }
}
