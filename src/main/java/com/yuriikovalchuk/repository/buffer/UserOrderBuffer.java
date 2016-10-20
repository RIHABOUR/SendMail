package com.yuriikovalchuk.repository.buffer;

import com.yuriikovalchuk.dto.OrderDto;
import com.yuriikovalchuk.util.TooManyOrdersException;

public interface UserOrderBuffer {

    void add(OrderDto order) throws TooManyOrdersException;

    void deleteOutOfIntervalOrdersByUser(String userEmail);

}
