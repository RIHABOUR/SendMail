package com.yuriikovalchuk.service.mail.buffered;

import org.junit.Before;
import org.junit.Test;

import static com.yuriikovalchuk.TestData.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TotalAmountOrderBufferTest {

    private TotalAmountOrderBuffer orderBuffer;

    @Before
    public void setUp() throws Exception {
        orderBuffer = new TotalAmountOrderBuffer();
        orderBuffer.setBufferCapacity(SIZE);
        TEMP_ORDER.setId(ORDER_ID);
        TEMP_ORDER.setSize(SIZE);
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(orderBuffer.add(TEMP_ORDER));
        assertFalse(orderBuffer.add(TEMP_ORDER));
    }

    @Test
    public void testRemove() throws Exception {
        orderBuffer.add(TEMP_ORDER);
        assertTrue(orderBuffer.remove(TEMP_ORDER.getId()));
        assertFalse(orderBuffer.remove(TEMP_ORDER.getId()));
    }
}