package com.yuriikovalchuk.domain;

import lombok.Data;

@Data
public class Report {

    private final int orderId;

    private final int userId;

    private String message;

}
