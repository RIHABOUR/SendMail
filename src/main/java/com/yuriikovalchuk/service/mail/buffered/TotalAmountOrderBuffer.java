package com.yuriikovalchuk.service.mail.buffered;

import com.yuriikovalchuk.domain.Order;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TotalAmountOrderBuffer {

    private Map<Integer, Order> orders = new HashMap<>();

    private int bufferSize;

    private int bufferCapacity;

    public void setBufferCapacity(int bufferCapacity) {
        this.bufferCapacity = bufferCapacity;
    }

    public boolean add(@NonNull Order order) {

        boolean hasSpace = bufferCapacity >= bufferSize + order.getSize();

        if (hasSpace) {
            orders.put(order.getId(), order);
            bufferSize += order.getSize();
        }

        return hasSpace;
    }

    public boolean remove(int orderId) {

        Order removed = orders.remove(orderId);

        if (removed != null) {
            bufferSize -= removed.getSize();
        }

        return removed != null;
    }

}
