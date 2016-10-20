package com.yuriikovalchuk.repository.buffer;

import com.yuriikovalchuk.dto.OrderDto;

public interface ServiceOrderBuffer {

    void add(OrderDto order) throws InterruptedException;

    void delete(OrderDto order);

}
