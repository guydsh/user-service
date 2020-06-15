package com.smc.user.controller;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.ResponseResult;
import com.smc.user.dto.UserDto;
import com.smc.user.service.IUserService;
import com.smc.user.vo.PasswordVo;
import com.smc.user.vo.UserProfileVo;
import com.smc.user.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseResult<PageDto<UserDto>> getAll(
            @RequestParam(value = "q", defaultValue = "", required = false) String keyword,
            @PageableDefault(sort = {"updatedTime"}) Pageable pageable) {

        PageDto<UserDto> userPage = userService.findAll(keyword, pageable);

        return ResponseResult.success("Create users successfully", userPage);
    }

    @PostMapping
    public ResponseResult create(@Valid @RequestBody UserVo userVo) {

        userService.addUser(userVo);

        return ResponseResult.success("Create user successfully", null);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult<Object> findById(@PathVariable(value = "id") Long id) {
        Optional<UserDto> optional = userService.findUserById(id);

        if (optional.isPresent()) {
            return ResponseResult.success("get user successfully", optional.get());
        } else {
            return ResponseResult.fail("failed to get user");
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult deleteById(@PathVariable(value = "id") Long id) {

        userService.deleteUserById(id);

        return ResponseResult.success("delete user successfully", null);
    }

    @PutMapping(value = "/{id}")
    public ResponseResult<UserDto> updateById(@PathVariable(value = "id") Long id,
                                              @Valid @RequestBody UserVo userVo) {
        UserDto userDto = userService.updateUser(id, userVo);

        return ResponseResult.success("Update user successfully", userDto);
    }

    @PutMapping(value = "/{id}/activation")
    public ResponseResult updateActivationById(@PathVariable(value = "id") Long id,
                                               @RequestParam(value = "active") boolean active) {

        String message = active ? "User is activated" : "User is deactivated";
        userService.updateUserActive(id, active);

        return ResponseResult.success(message, null );
    }

    @PutMapping(value = "/{id}/profiles")
    public ResponseResult<UserDto> updateUserProfileById(@PathVariable(value = "id") Long id,
                                                @Valid @RequestBody UserProfileVo userProfileVo) {

        UserDto userDto = userService.updateUserProfile(id, userProfileVo);

        return ResponseResult.success("Update profile successfully", userDto);
    }

    @PutMapping(value = "/{id}/password")
    public ResponseResult updateUserPasswordById(@PathVariable(value = "id") Long id,
                                                 @Valid @RequestBody PasswordVo passwordVo) {

        userService.updateUserPassword(id, passwordVo);

        return ResponseResult.success("Update password successfully", null);
    }

}
