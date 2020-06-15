package com.smc.user.service;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.UserDto;
import com.smc.user.vo.PasswordVo;
import com.smc.user.vo.UserProfileVo;
import com.smc.user.vo.UserVo;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserService {

    UserDto addUser(UserVo userVo);
    void deleteUserById(Long id);
    UserDto updateUser(Long id, UserVo userVo);
    Optional<UserDto> findUserById(Long id);
    PageDto<UserDto> findAll(String keyword, Pageable pageable);
    void updateUserActive(Long id, boolean active);
    UserDto updateUserProfile(Long id, UserProfileVo userProfileVo);
    void updateUserPassword(Long id, PasswordVo passwordVo);
}
