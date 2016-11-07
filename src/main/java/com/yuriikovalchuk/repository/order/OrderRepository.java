package com.yuriikovalchuk.repository.order;

import com.yuriikovalchuk.domain.Order;

import java.util.concurrent.TimeUnit;

public interface OrderRepository {

    int add(Order order) throws IllegalStateException;

    Order poll(long timeout, TimeUnit unit) throws InterruptedException;

}
