package com.smc.user.repository;

import com.smc.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findAllByNameContainsIgnoreCase(String name, Pageable pageable);

    Optional<Role> findByName(String name);
}
