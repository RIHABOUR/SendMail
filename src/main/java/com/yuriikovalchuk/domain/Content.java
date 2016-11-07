package com.yuriikovalchuk.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class Content {

    @NotNull
    @Pattern(regexp = "text/(html|plain)", message = "Incompatible content type.")
    private String type;

    @NotEmpty
    private String value;

}
