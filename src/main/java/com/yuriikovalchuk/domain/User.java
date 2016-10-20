package com.yuriikovalchuk.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class User {

    @NotNull
    private final String email;

}
