package com.yuriikovalchuk.service.order.limited;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.repository.order.OrderRepository;
import com.yuriikovalchuk.service.order.OrderService;
import com.yuriikovalchuk.service.user.UserService;
import com.yuriikovalchuk.util.OrderUtil;
import com.yuriikovalchuk.util.exception.OrderLimitException;
import com.yuriikovalchuk.util.exception.UserNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LimitedOrderService implements OrderService {

    @Autowired
    private OrderUtil orderUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository repository;

    private IntervalTimeOrderLimiter orderLimiter;

    public void setOrderLimiter(IntervalTimeOrderLimiter orderLimiter) {
        this.orderLimiter = orderLimiter;
    }

    @Override
    public int add(@NonNull Order order) throws OrderLimitException, UserNotFoundException, IllegalStateException {

        /* Set order's time in millis. */
        order.setTimeInMillis(orderUtil.getCurrentTime());

        /* Calculate orders size. */
        order.setSize(orderUtil.calculateSize(order));

        /* User identification. Set user to order.*/
        order.setUser(userService.getByEmail(order.getFrom()));

        /* Try to add the order to time limiter. */
        orderLimiter.add(order);

        return repository.add(order);

    }

}
