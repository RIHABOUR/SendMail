package com.yuriikovalchuk.repository.buffer;

import com.yuriikovalchuk.dto.OrderDto;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class ServiceOrderBufferImpl implements ServiceOrderBuffer {

    private Set<OrderDto> activeOrders = new HashSet<>();
    private int bufferCapacity;
    private int bufferSize = 0;

    public void setServerOrderBufferCapacity(int serverOrderBufferCapacity) {
        this.bufferCapacity = serverOrderBufferCapacity;
    }

    @Override
    public synchronized void add(@NonNull OrderDto order) throws InterruptedException {

        while (bufferSize + order.getSize() > bufferCapacity) {
            this.wait();
        }
        activeOrders.add(order);
        bufferSize += order.getSize();
    }

    @Override
    public synchronized void delete(@NonNull OrderDto order) {

        if (activeOrders.remove(order)) {
            bufferSize -= order.getSize();
            this.notifyAll();
        }
    }

}
