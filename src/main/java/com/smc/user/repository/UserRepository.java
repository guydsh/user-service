package com.smc.user.repository;

import com.smc.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndActiveTrue(String username);

    @Query(value = "SELECT u FROM User u WHERE UPPER(u.username) like CONCAT('%',UPPER(:keyword),'%')" +
            "OR UPPER(u.email) like CONCAT('%',UPPER(:keyword),'%')")
    Page<User> findAllUsersByKeywordWithPagination(@Param("keyword") String keyword, Pageable pageable);
}
