package com.yuriikovalchuk.repository.buffer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.yuriikovalchuk.TestData.*;

public class ServiceOrderBufferImplTest {

    private ServiceOrderBufferImpl serviceOrderBuffer;

    @Before
    public void setUp() throws Exception {
        serviceOrderBuffer = new ServiceOrderBufferImpl();
        serviceOrderBuffer.setServerOrderBufferCapacity(50);
    }

    @Test
    public void testAddAndDelete() throws Exception {
        serviceOrderBuffer.add(TEST_ORDER_DTO);

        Thread thread = new Thread(() -> {
            try {
                serviceOrderBuffer.add(TEST_ORDER_DTO_2);
            } catch (InterruptedException ignore) {/*NOP*/}
        });

        thread.start();

        Thread.sleep(1000);

        assertEquals(Thread.State.WAITING, thread.getState());

        serviceOrderBuffer.delete(TEST_ORDER_DTO);

        Thread.sleep(1000);

        assertEquals(Thread.State.TERMINATED, thread.getState());

    }
}