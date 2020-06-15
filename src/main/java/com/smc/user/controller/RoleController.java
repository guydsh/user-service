package com.smc.user.controller;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.ResponseResult;
import com.smc.user.dto.RoleDto;
import com.smc.user.service.IRoleService;
import com.smc.user.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseResult<PageDto<RoleDto>> getAll(
            @RequestParam(value = "q", required = false, defaultValue = "") String keyword,
            @PageableDefault(sort = {"updatedTime"}) Pageable pageable) {

        PageDto<RoleDto> rolePage = roleService.findAll(keyword, pageable);

        return ResponseResult.success("get roles successfully", rolePage);
    }

    @PostMapping
    public ResponseResult create(@Valid @RequestBody RoleVo roleVo) {

        roleService.addRole(roleVo);

        return ResponseResult.success("create role successfully", null);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult getById(@PathVariable(value = "id") Long id) {
        Optional<RoleDto> optional = roleService.findRoleById(id);

        if (optional.isPresent()) {
            return ResponseResult.success("get role successfully", optional.get());
        } else {
            return ResponseResult.fail("failed to get the role " + id);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult deleteById(@PathVariable(value = "id") Long id) {

        roleService.deleteRoleById(id);

        return ResponseResult.success("delete the role successfully", null);
    }

    @PutMapping(value = "/{id}")
    public ResponseResult<RoleDto> updateById(@PathVariable(value = "id") Long id,
                                              @Valid @RequestBody RoleVo roleVo) {

        RoleDto roleDto = roleService.updateRole(id, roleVo);

        return ResponseResult.success("update role successfully", roleDto);
    }

}
