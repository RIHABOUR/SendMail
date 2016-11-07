package com.yuriikovalchuk.util;

import org.junit.Before;
import org.junit.Test;

import static com.yuriikovalchuk.TestData.ORDER;
import static com.yuriikovalchuk.TestData.SIZE;
import static com.yuriikovalchuk.TestData.TEMP_ORDER;
import static org.junit.Assert.assertEquals;

public class OrderUtilTest {

    private OrderUtil orderUtil;

    @Before
    public void setUp() throws Exception {
        orderUtil = new OrderUtil();
    }

    @Test
    public void testCalculateSize() throws Exception {
        int actual = orderUtil.calculateSize(TEMP_ORDER);
        assertEquals(SIZE, actual);
    }
}