package com.yuriikovalchuk.util;

import com.yuriikovalchuk.dto.OrderDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.Assert.*;
import static com.yuriikovalchuk.TestData.*;

public class OrderUtilTest {

    private OrderUtil orderUtil;

    @Before
    public void setUp() throws Exception {
        orderUtil = new OrderUtil();
    }

    @Test
    public void testOrderToOrderDto() throws Exception {
        OrderDto actual = orderUtil.orderToOrderDto(TEST_ORDER, TEST_TIME);
        assertEquals(TEST_ORDER_DTO, actual);
    }

    @Test
    public void testOrderToSimpleMailMessage() throws Exception {
        SimpleMailMessage actual = orderUtil.orderToSimpleMailMessage(TEST_ORDER);
        assertEquals(TEST_MAIL_MESSAGE.getFrom(), actual.getFrom());
        assertArrayEquals(TEST_MAIL_MESSAGE.getTo(), actual.getTo());
        assertEquals(TEST_MAIL_MESSAGE.getSubject(), actual.getSubject());
        assertEquals(TEST_MAIL_MESSAGE.getText(), actual.getText());
    }
}