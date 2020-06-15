package com.smc.user.controller;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.ResourceDto;
import com.smc.user.dto.ResponseResult;
import com.smc.user.service.IResourceService;
import com.smc.user.vo.ResourceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/resources")
public class ResourceController {

    @Autowired
    private IResourceService resourceService;

    @GetMapping
    public ResponseResult<PageDto<ResourceDto>> getAll(
            @RequestParam(value = "q", required = false, defaultValue = "") String keyword,
            @PageableDefault(sort = {"updatedTime"}) Pageable pageable
    ) {

        PageDto<ResourceDto> resourcePage = resourceService.findAll(keyword, pageable);

        return ResponseResult.success("get resources successfully", resourcePage);
    }

    @PostMapping
    public ResponseResult create(@Valid @RequestBody ResourceVo resourceVo) {

        resourceService.addResource(resourceVo);

        return ResponseResult.success("create resource successfully", null);
    }

    @GetMapping(value = "/{id}")
    public ResponseResult<Object> getById(@PathVariable(value = "id") Long id) {

        Optional<ResourceDto> optional = resourceService.findResourceById(id);
        return optional.<ResponseResult<Object>>map(resourceDto -> ResponseResult.success("get resource successfully", resourceDto))
                .orElseGet(() -> ResponseResult.fail("failed to get resource " + id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult deleteById(@PathVariable(value = "id") Long id) {

        resourceService.deleteResourceById(id);

        return ResponseResult.success("delete resource " + id + " successfully", null);
    }

    @PutMapping(value = "/{id}")
    public ResponseResult<ResourceDto> updateById(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody ResourceVo resourceVo) {

        ResourceDto resourceDto = resourceService.updateResource(id, resourceVo);

        return ResponseResult.success("update resource successfully", resourceDto);
    }
}
