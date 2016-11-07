package com.yuriikovalchuk.service.order;

import com.yuriikovalchuk.repository.order.OrderRepository;
import com.yuriikovalchuk.service.order.limited.IntervalTimeOrderLimiter;
import com.yuriikovalchuk.service.order.limited.LimitedOrderService;
import com.yuriikovalchuk.service.user.UserService;
import com.yuriikovalchuk.util.OrderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static com.yuriikovalchuk.TestData.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class LimitedOrderServiceTest {

    @Mock
    private OrderUtil orderUtil;

    @Mock
    private UserService userService;

    @Mock
    private OrderRepository repository;

    @Mock
    private IntervalTimeOrderLimiter orderLimiter;

    @InjectMocks
    private LimitedOrderService orderService;

    @Test
    public void testAdd() throws Exception {
        given(orderUtil.getCurrentTime()).willReturn(TIME);
        given(orderUtil.calculateSize(TEMP_ORDER)).willReturn(SIZE);
        given(userService.getByEmail(EMAIL)).willReturn(USER);
        given(repository.add(TEMP_ORDER)).willReturn(ORDER_ID);

        int actual = orderService.add(TEMP_ORDER);
        assertEquals(ORDER_ID, actual);

        verify(orderUtil).getCurrentTime();
        verify(orderUtil).calculateSize(TEMP_ORDER);
        verify(userService).getByEmail(EMAIL);
        verify(orderLimiter).add(TEMP_ORDER);
        verify(repository).add(TEMP_ORDER);
        verifyNoMoreInteractions(orderUtil, userService, orderLimiter, repository);
    }

}