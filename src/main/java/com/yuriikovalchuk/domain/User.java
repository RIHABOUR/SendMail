package com.yuriikovalchuk.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@Data
public class User {

    private int id;

    @NotNull
    @Email
    private String email;

    public User(String email) {
        this.email = email;
    }

}
