package com.yuriikovalchuk.repository.order;

import com.yuriikovalchuk.domain.Order;
import lombok.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderRepositoryImpl implements OrderRepository {

    private AtomicInteger counter = new AtomicInteger();

    private BlockingQueue<Order> orders;

    public void setOrders(BlockingQueue<Order> orders) {
        this.orders = orders;
    }

    @Override
    public int add(@NonNull Order order) throws IllegalStateException {
        int orderId = counter.incrementAndGet();
        order.setId(orderId);
        orders.add(order);
        return orderId;
    }

    @Override
    public Order poll(long timeout, TimeUnit unit) throws InterruptedException {
        return orders.poll(timeout, unit);
    }
}
