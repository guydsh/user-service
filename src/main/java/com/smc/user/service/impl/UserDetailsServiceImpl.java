package com.smc.user.service.impl;

import com.smc.user.dto.AuthUserDto;
import com.smc.user.dto.RoleDto;
import com.smc.user.entity.User;
import com.smc.user.repository.UserRepository;
import com.smc.user.service.IRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserRepository dao;

    @Override
    public AuthUserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserDto authUserDto = null;

        Optional<User> optionalUser;
        optionalUser = dao.findByUsername(username);

        if (optionalUser.isPresent()) authUserDto = convertToDto(optionalUser.get());
        return authUserDto;
    }

    private AuthUserDto convertToDto(User user) {
        AuthUserDto authUserDto = new AuthUserDto();
        BeanUtils.copyProperties(user, authUserDto);

        Set<RoleDto> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(roleService.convertToDto(role)));

        authUserDto.setRoles(roles);

        return authUserDto;
    }
}
