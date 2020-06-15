package com.smc.user.service.impl;

import com.smc.user.dto.PageDto;
import com.smc.user.dto.ResourceDto;
import com.smc.user.entity.Resource;
import com.smc.user.repository.ResourceRepository;
import com.smc.user.service.IPageService;
import com.smc.user.service.IResourceService;
import com.smc.user.vo.ResourceVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements IResourceService {
    @Autowired
    private IPageService pageService;

    @Autowired
    private ResourceRepository dao;

    @Override
    public void addResource(ResourceVo resourceVo) {
        Resource resource = convertToEntity(null, resourceVo);
        dao.save(resource);
    }

    @Override
    public void deleteResourceById(Long id) {
        Optional<Resource> optionalResource = findById(id);
        optionalResource.ifPresent(resource -> dao.delete(resource));
    }

    @Override
    public ResourceDto updateResource(Long id, ResourceVo resourceVo) {
        Resource resource = convertToEntity(id, resourceVo);
        return convertToDto(dao.save(resource));
    }

    @Override
    public Optional<ResourceDto> findResourceById(Long id) {
        ResourceDto resourceDto = null;

        Optional<Resource> resourceOptional = findById(id);
        if (resourceOptional.isPresent()) {
            resourceDto = convertToDto(resourceOptional.get());
        }

        return Optional.ofNullable(resourceDto);
    }

    @Override
    public PageDto<ResourceDto> findAll(String keyword, Pageable pageable) {
        Page<Resource> resourcePage = dao.findAllByNameContainsIgnoreCase(keyword, pageable);
        return pageService.convertToPageDto(resourcePage, this::convertToDto);
    }

    private Resource convertToEntity(Long id, ResourceVo resourceVo) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceVo, resource);

        if (id != null) {
            Optional<Resource> optionalResource = findById(id);
            optionalResource.ifPresent(r -> {
                resource.setId(r.getId());
                resource.setCreatedBy(r.getCreatedBy());
                resource.setCreatedTime(r.getCreatedTime());
                resource.setUpdatedBy(r.getUpdatedBy());
                resource.setUpdatedTime(new Date());
            });
        }

        return resource;
    }

    @Override
    public ResourceDto convertToDto(Resource resource) {
        ResourceDto resourceDto = new ResourceDto();
        BeanUtils.copyProperties(resource, resourceDto);

        return resourceDto;
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return dao.findById(id);
    }
}
