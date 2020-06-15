package com.smc.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_user")
public class User extends Auditable {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "active")
    private Boolean active = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Role> roles;

}
