package com.yuriikovalchuk.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@Data
public class OrderDto {

    @NotNull
    @Email
    private final String userEmail;

    private final long timeInMillis;

    private final int size;

}
