package com.yuriikovalchuk.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Content {

    @NotNull
    private String type;

    @NotNull
    private String value;

}
