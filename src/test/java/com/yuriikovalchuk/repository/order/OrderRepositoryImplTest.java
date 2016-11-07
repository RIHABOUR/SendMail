package com.yuriikovalchuk.repository.order;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;
import static com.yuriikovalchuk.TestData.*;

public class OrderRepositoryImplTest {

    private OrderRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        repository = new OrderRepositoryImpl();
        repository.setOrders(new LinkedBlockingQueue<>(CAPACITY));
    }

    @Test
    public void testAdd() throws Exception {
        int actualOrderId = repository.add(TEMP_ORDER);
        assertEquals(ORDER_ID, actualOrderId);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddServiceOverloaded() throws Exception {
        repository.add(TEMP_ORDER);
        repository.add(TEMP_ORDER);
    }
}