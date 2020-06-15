package com.smc.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_verification_token")
public class VerificationToken extends Auditable {

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public VerificationToken() {

    }

    public VerificationToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

}
