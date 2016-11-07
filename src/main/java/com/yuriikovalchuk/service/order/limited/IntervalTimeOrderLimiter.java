package com.yuriikovalchuk.service.order.limited;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.util.exception.OrderLimitException;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IntervalTimeOrderLimiter {

    private Map<Integer, LinkedList<Long>> orderTimesByUserId = new ConcurrentHashMap<>();

    private long interval;

    private int maxOrderCountPerInterval;

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setMaxOrderCountPerInterval(int maxOrderCountPerInterval) {
        this.maxOrderCountPerInterval = maxOrderCountPerInterval;
    }

    public void add(@NonNull Order order) throws OrderLimitException {

        LinkedList<Long> times = orderTimesByUserId.computeIfAbsent(order.getUser().getId(), k -> new LinkedList<>());

        synchronized (times) {

            if (times.size() == maxOrderCountPerInterval) {

                long difference = order.getTimeInMillis() - times.getFirst();

                if (difference < interval) {
                    long timeout = interval - difference;
                    throw new OrderLimitException("Too many requests. Try in " + timeout + " milliseconds.");
                } else {
                    times.removeFirst();
                }

            }

            times.addLast(order.getTimeInMillis());
        }

    }

}
