package com.yuriikovalchuk.web;

import com.yuriikovalchuk.TestData;
import com.yuriikovalchuk.service.OrderService;
import com.yuriikovalchuk.service.UserService;
import com.yuriikovalchuk.util.OrderUtil;
import com.yuriikovalchuk.util.TooManyOrdersException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.yuriikovalchuk.TestData.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
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
    private OrderUtil orderUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MailSender mailSender;

    @Test
    public void testOrder() throws Exception {
        given(this.orderUtil.getTimeNowInMillis()).willReturn(TEST_TIME);
        given(this.orderUtil.orderToOrderDto(TEST_ORDER, TEST_TIME)).willReturn(TEST_ORDER_DTO);
        given(this.userService.getByEmail(TEST_EMAIL)).willReturn(TEST_USER);
        given(this.orderUtil.orderToSimpleMailMessage(TEST_ORDER)).willReturn(TEST_MAIL_MESSAGE);

        System.out.println(TEST_ORDER);

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(TestData.TEST_JSON))
                .andExpect(status().isOk()).andExpect(content().string("The email has been sent!"));
    }

    @Test
    public void testOrderUnsupportedMediaType() throws Exception {
        this.mvc.perform(post("/order").contentType(MediaType.TEXT_PLAIN).content(TestData.TEST_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testOrderBeanValidationError() throws Exception {
        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(TestData.TEST_JSON_WITHOUT_TO))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Field error in object 'order' on field 'to': rejected value [null]. may not be null"));
    }

    @Test
    public void testOrderAuthorizationFailed() throws Exception {
        given(this.orderUtil.getTimeNowInMillis()).willReturn(TEST_TIME);
        given(this.orderUtil.orderToOrderDto(TEST_ORDER, TEST_TIME)).willReturn(TEST_ORDER_DTO);
        given(this.userService.getByEmail(TEST_EMAIL)).willReturn(null);

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(TestData.TEST_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User with email: '" + TEST_EMAIL + "' is not authorized."));
    }

    @Test
    public void testOrderTooManyRequests() throws Exception {
        given(this.orderUtil.getTimeNowInMillis()).willReturn(TEST_TIME);
        given(this.orderUtil.orderToOrderDto(TEST_ORDER, TEST_TIME)).willReturn(TEST_ORDER_DTO);
        given(this.userService.getByEmail(TEST_EMAIL)).willReturn(TEST_USER);
        doThrow(new TooManyOrdersException("Server is overload!")).when(orderService).add(TEST_ORDER_DTO);

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(TestData.TEST_JSON))
                .andExpect(status().isTooManyRequests()).andExpect(content().string("Server is overload!"));
    }

    @Test
    public void testOrderSendingMailError() throws Exception {
        given(this.orderUtil.getTimeNowInMillis()).willReturn(TEST_TIME);
        given(this.orderUtil.orderToOrderDto(TEST_ORDER, TEST_TIME)).willReturn(TEST_ORDER_DTO);
        given(this.userService.getByEmail(TEST_EMAIL)).willReturn(TEST_USER);
        given(this.orderUtil.orderToSimpleMailMessage(TEST_ORDER)).willReturn(TEST_MAIL_MESSAGE);
        doThrow(new MailAuthenticationException("Authentication failed.")).when(mailSender).send(TEST_MAIL_MESSAGE);

        this.mvc.perform(post("/order").contentType(MediaType.APPLICATION_JSON).content(TestData.TEST_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Authentication failed."));
    }

}