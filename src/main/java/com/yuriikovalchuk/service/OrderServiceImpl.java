package com.yuriikovalchuk.service;

import com.yuriikovalchuk.dto.OrderDto;
import com.yuriikovalchuk.repository.buffer.ServiceOrderBuffer;
import com.yuriikovalchuk.repository.buffer.UserOrderBuffer;
import com.yuriikovalchuk.util.TooManyOrdersException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserOrderBuffer userOrderBuffer;

    @Autowired
    private ServiceOrderBuffer serviceOrderBuffer;

    @Override
    public void add(@NonNull OrderDto order) throws InterruptedException, TooManyOrdersException {
        userOrderBuffer.add(order);
        serviceOrderBuffer.add(order);
    }

    @Override
    public void delete(@NonNull OrderDto order) {
        serviceOrderBuffer.delete(order);

        /* Not required. Delete all out of date orders for current user from buffer. */
        userOrderBuffer.deleteOutOfIntervalOrdersByUser(order.getUserEmail());
    }

}
