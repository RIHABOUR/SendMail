package com.yuriikovalchuk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jirutka.validator.collection.constraints.EachEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @NotNull
    @Email
    private String from;

    @Email
    @JsonProperty("reply_to")
    private String replyTo;

    @NotNull
    @EachEmail
    private List<String> to;

    @EachEmail
    private List<String> cc;

    @EachEmail
    private List<String> bcc;

    @NotNull
    private String subject;

    @NotNull
    private Content content;

}
