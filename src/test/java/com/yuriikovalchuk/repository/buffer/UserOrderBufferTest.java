package com.yuriikovalchuk.repository.buffer;

import com.yuriikovalchuk.util.TooManyOrdersException;
import org.junit.Before;
import org.junit.Test;

import static com.yuriikovalchuk.TestData.*;

public class UserOrderBufferTest {

    private UserOrderBufferImpl userOrderBuffer;

    @Before
    public void setUp() throws Exception {
        userOrderBuffer = new UserOrderBufferImpl();
        userOrderBuffer.setInterval(TEST_INTERVAL);
        userOrderBuffer.setMaxOrderCountPerInterval(TEST_MAX_ORDER_PER_INTERVAL);
    }

    @Test
    public void testAdd() throws Exception {
        for (int i = 0; i < TEST_MAX_ORDER_PER_INTERVAL; i++) {
            userOrderBuffer.add(TEST_ORDER_DTO);
        }
    }

    @Test(expected = TooManyOrdersException.class)
    public void testAddToManyOrders() throws Exception {
        for (int i = 0; i < TEST_MAX_ORDER_PER_INTERVAL + 1; i++) {
            userOrderBuffer.add(TEST_ORDER_DTO);
        }
    }

}