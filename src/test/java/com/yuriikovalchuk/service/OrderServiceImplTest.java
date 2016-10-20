package com.yuriikovalchuk.service;

import com.yuriikovalchuk.repository.buffer.ServiceOrderBuffer;
import com.yuriikovalchuk.repository.buffer.UserOrderBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.yuriikovalchuk.TestData.TEST_EMAIL;
import static com.yuriikovalchuk.TestData.TEST_ORDER_DTO;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private ServiceOrderBuffer serviceOrderBuffer;

    @Mock
    private UserOrderBuffer userOrderBuffer;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void testAdd() throws Exception {
        orderService.add(TEST_ORDER_DTO);
        verify(userOrderBuffer).add(TEST_ORDER_DTO);
        verify(serviceOrderBuffer).add(TEST_ORDER_DTO);
        verifyNoMoreInteractions(userOrderBuffer, serviceOrderBuffer);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNull() throws Exception {
        orderService.add(null);
    }

    @Test
    public void testDelete() throws Exception {
        orderService.delete(TEST_ORDER_DTO);
        verify(serviceOrderBuffer).delete(TEST_ORDER_DTO);
        verify(userOrderBuffer).deleteOutOfIntervalOrdersByUser(TEST_EMAIL);
        verifyNoMoreInteractions(serviceOrderBuffer, userOrderBuffer);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteNull() throws Exception {
        orderService.delete(null);
    }
}