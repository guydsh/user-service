package com.smc.user.service.impl;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.ResourceDto;
import com.smc.user.dto.RoleDto;
import com.smc.user.entity.Auditable;
import com.smc.user.entity.Resource;
import com.smc.user.entity.Role;
import com.smc.user.repository.RoleRepository;
import com.smc.user.service.IPageService;
import com.smc.user.service.IResourceService;
import com.smc.user.service.IRoleService;
import com.smc.user.vo.RoleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IPageService pageService;

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private RoleRepository dao;

    @Override
    public void addRole(RoleVo roleVo) {
        Role role = converToEntity(null, roleVo);
        dao.save(role);
    }

    @Override
    public void deleteRoleById(Long id) {
        Optional<Role> optionalRole = findById(id);
        optionalRole.ifPresent(role -> dao.delete(role));
    }

    @Override
    public RoleDto updateRole(Long id, RoleVo roleVo) {
        Role role = converToEntity(id, roleVo);
        return convertToDto(dao.save(role));
    }

    @Override
    public Optional<RoleDto> findRoleById(Long id) {
        RoleDto roleDto = null;

        Optional<Role> optionalRole = findById(id);
        if (optionalRole.isPresent()) {
            roleDto = convertToDto(optionalRole.get());
        }

        return Optional.ofNullable(roleDto);
    }

    @Override
    public PageDto<RoleDto> findAll(String keyword, Pageable pageable) {
        Page<Role> rolePage = dao.findAllByNameContainsIgnoreCase(keyword, pageable);
        return pageService.convertToPageDto(rolePage, this::convertToDto);
    }

    private Role converToEntity(Long id, RoleVo roleVo) {
        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);

        Set<Resource> resources = new HashSet<>();
        roleVo.getResourceIds().forEach(resourceId -> {
            Optional<Resource> optionalResource = resourceService.findById(resourceId);
            optionalResource.ifPresent(resources::add);
        });
        role.setResources(resources);

        if (id != null) {
            Optional<Role> optionalRole = findById(id);
            optionalRole.ifPresent(r -> {
                role.setId(r.getId());
                role.setCreatedBy(r.getCreatedBy());
                role.setCreatedTime(r.getCreatedTime());
                role.setUpdatedBy(r.getUpdatedBy());
                role.setUpdatedTime(new Date());
            });
        }

        return role;
    }

    @Override
    public RoleDto convertToDto(Role role) {
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(role, roleDto);

        Set<ResourceDto> resources = new HashSet<>();
        role.getResources().forEach(resource -> resources.add(resourceService.convertToDto(resource)));
        roleDto.setResources(resources);

        return roleDto;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public Long findRoleIdByRoleName(String name) {
        Optional<Role> optionalRole = dao.findByName(name);
        return optionalRole.map(Role::getId).orElse(null);
    }
}
