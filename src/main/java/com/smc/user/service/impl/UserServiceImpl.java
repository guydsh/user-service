package com.smc.user.service.impl;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.RoleDto;
import com.smc.user.dto.UserDto;
import com.smc.user.entity.Role;
import com.smc.user.entity.User;
import com.smc.user.repository.UserRepository;
import com.smc.user.service.IPageService;
import com.smc.user.service.IRoleService;
import com.smc.user.service.IUserService;
import com.smc.user.vo.PasswordVo;
import com.smc.user.vo.UserProfileVo;
import com.smc.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IPageService pageService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserRepository dao;

    @Override
    public UserDto addUser(UserVo userVo) {
        User user = convertToEntity(null, userVo);
        Optional<User> existingUser = dao.findByUsername(user.getUsername());
        if (existingUser.isPresent()) throw new RuntimeException(("User " + user.getUsername()) + " has been registered, please change the username, and try again.");
        return convertToDto(dao.save(user));
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> optionalUser = dao.findById(id);
        optionalUser.ifPresent(user -> dao.delete(user));
    }

    @Override
    public UserDto updateUser(Long id, UserVo userVo) {
        User user = convertToEntity(id, userVo);
        return convertToDto(dao.save(user));
    }

    @Override
    public Optional<UserDto> findUserById(Long id) {
        UserDto userDto = null;

        Optional<User> optionalUser = dao.findById(id);
        if (optionalUser.isPresent()) {
            userDto = convertToDto(optionalUser.get());
        }

        return Optional.ofNullable(userDto);
    }

    @Override
    public PageDto<UserDto> findAll(String keyword, Pageable pageable) {
        Page<User> userPage = dao.findAllUsersByKeywordWithPagination(keyword, pageable);
        return pageService.convertToPageDto(userPage, this::convertToDto);
    }

    @Override
    public void updateUserActive(Long id, boolean active) {
        Optional<User> optionalUser = dao.findById(id);
        optionalUser.ifPresent(user -> {
            user.setActive(active);
            dao.save(user);
        });
    }

    @Override
    public UserDto updateUserProfile(Long id, UserProfileVo userProfileVo) {
        Optional<User> optionalUser = dao.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setContactNumber(userProfileVo.getContactNumber());
            user.setEmail(userProfileVo.getEmail());
            return convertToDto(dao.save(user));
        }
        return null;
    }

    @Override
    public void updateUserPassword(Long id, PasswordVo passwordVo) {
        Optional<User> optionalUser = dao.findById(id);
        optionalUser.ifPresent(user -> {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(passwordVo.getPassword()));
            dao.save(user);
        });
    }

    private User convertToEntity(Long id, UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(userVo.getPassword()));

        Set<Role> roles = new HashSet<>();
        userVo.getRoleIds().forEach(roleId -> {
            Optional<Role> optionalRole = roleService.findById(roleId);
            optionalRole.ifPresent(roles::add);
        });
        user.setRoles(roles);

        if (id != null) {
            Optional<User> optionalUser = dao.findById(id);
            optionalUser.ifPresent(u -> {
                user.setId(u.getId());
                // if update, no password change
                user.setPassword(u.getPassword());
                user.setCreatedBy(u.getCreatedBy());
                user.setCreatedTime(u.getCreatedTime());
                user.setUpdatedBy(u.getUpdatedBy());
                user.setUpdatedTime(new Date());
            });
        }

        return user;
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        Set<RoleDto> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(roleService.convertToDto(role)));
        userDto.setRoles(roles);

        return userDto;
    }
}
