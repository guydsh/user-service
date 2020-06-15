package com.smc.user.service;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.RoleDto;
import com.smc.user.entity.Role;
import com.smc.user.vo.RoleVo;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRoleService {
    void addRole(RoleVo roleVo);
    void deleteRoleById(Long id);
    RoleDto updateRole(Long id, RoleVo roleVo);
    Optional<RoleDto> findRoleById(Long id);
    PageDto<RoleDto> findAll(String keyword, Pageable pageable);

    RoleDto convertToDto(Role role);
    Optional<Role> findById(Long id);
    Long findRoleIdByRoleName(String name);
}
