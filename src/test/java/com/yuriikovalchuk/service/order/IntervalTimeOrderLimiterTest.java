package com.yuriikovalchuk.service.order;

import com.yuriikovalchuk.service.order.limited.IntervalTimeOrderLimiter;
import com.yuriikovalchuk.util.exception.OrderLimitException;
import org.junit.Before;
import org.junit.Test;

import static com.yuriikovalchuk.TestData.*;

public class IntervalTimeOrderLimiterTest {

    private IntervalTimeOrderLimiter orderLimiter;

    @Before
    public void setUp() throws Exception {
        orderLimiter = new IntervalTimeOrderLimiter();
        orderLimiter.setInterval(INTERVAL);
        orderLimiter.setMaxOrderCountPerInterval(MAX_ORDER_PER_INTERVAL);
        TEMP_ORDER.setUser(USER);
    }

    @Test
    public void testAdd() throws Exception {
        for (int i = 0; i < MAX_ORDER_PER_INTERVAL; i++) {
            orderLimiter.add(TEMP_ORDER);
        }
    }

    @Test(expected = OrderLimitException.class)
    public void testAddToManyOrders() throws Exception {
        for (int i = 0; i < MAX_ORDER_PER_INTERVAL + 1; i++) {
            orderLimiter.add(TEMP_ORDER);
        }
    }

}