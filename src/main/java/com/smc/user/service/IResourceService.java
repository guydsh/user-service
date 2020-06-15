package com.smc.user.service;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.ResourceDto;
import com.smc.user.entity.Resource;
import com.smc.user.vo.ResourceVo;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IResourceService {
    void addResource(ResourceVo resourceVo);
    void deleteResourceById(Long id);
    ResourceDto updateResource(Long id, ResourceVo resourceVo);
    Optional<ResourceDto> findResourceById(Long id);
    PageDto<ResourceDto> findAll(String keyword, Pageable pageable);

    ResourceDto convertToDto(Resource resource);
    Optional<Resource> findById(Long id);
}
