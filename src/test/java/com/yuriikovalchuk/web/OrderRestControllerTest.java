package com.yuriikovalchuk.web;

import com.yuriikovalchuk.service.order.OrderService;
import com.yuriikovalchuk.util.exception.OrderLimitException;
import com.yuriikovalchuk.util.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.yuriikovalchuk.TestData.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderRestController.class)
@ContextConfiguration(locations = "classpath:spring-app.xml")
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void testOrder() throws Exception {
        given(orderService.add(ORDER)).willReturn(ORDER_ID);

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("The order has been added. Order id: " + ORDER_ID));
    }

    @Test
    public void testOrderBeanValidationError() throws Exception {
        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(JSON_WITHOUT_TO))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Field error in object 'order' on field 'to': " +
                                "rejected value [null]. may not be empty"));
    }

    @Test
    public void testOrderAuthorizationFailed() throws Exception {
        given(orderService
                .add(ORDER))
                .willThrow(new UserNotFoundException("User with email: '" + EMAIL_2 + "' is not authorized."));

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User with email: '" + EMAIL_2 + "' is not authorized."));
    }

    @Test
    public void testOrderTooManyRequests() throws Exception {
        given(orderService
                .add(ORDER))
                .willThrow(new OrderLimitException("Too many requests. Try in " + TIMEOUT + " milliseconds."));

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string("Too many requests. Try in " + TIMEOUT + " milliseconds."));
    }

    @Test
    public void testOrderServiceOverloaded() throws Exception {
        given(orderService.add(ORDER)).willThrow(new IllegalStateException("Service overloaded."));

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Service overloaded."));
    }

    @Test
    public void testOrderAnyOtherFail() throws Exception {
        given(orderService.add(ORDER)).willThrow(new RuntimeException("Something went wrong"));

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Something went wrong"));
    }

}