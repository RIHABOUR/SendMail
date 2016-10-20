package com.yuriikovalchuk.service;

import com.yuriikovalchuk.dto.OrderDto;
import com.yuriikovalchuk.util.TooManyOrdersException;

public interface OrderService {

    void add(OrderDto order) throws InterruptedException, TooManyOrdersException;

    void delete(OrderDto order);

}
