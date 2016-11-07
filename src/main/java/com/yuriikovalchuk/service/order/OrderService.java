package com.yuriikovalchuk.service.order;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.util.exception.OrderLimitException;

public interface OrderService {

    int add(Order order) throws OrderLimitException;

}
