package com.yuriikovalchuk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jirutka.validator.collection.constraints.EachEmail;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {

    @NotNull
    @Email
    private String from;

    @Email
    @JsonProperty("reply_to")
    private String replyTo;

    @NotEmpty
    @EachEmail
    private List<String> to;

    @EachEmail
    private List<String> cc;

    @EachEmail
    private List<String> bcc;

    @NotEmpty
    private String subject;

    @NotNull
    @Valid
    private Content content;

    private int id;

    private User user;

    private long timeInMillis;

    private int size;

}
