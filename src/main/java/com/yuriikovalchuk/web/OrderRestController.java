package com.yuriikovalchuk.web;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.dto.OrderDto;
import com.yuriikovalchuk.service.OrderService;
import com.yuriikovalchuk.service.UserService;
import com.yuriikovalchuk.util.OrderUtil;
import com.yuriikovalchuk.util.TooManyOrdersException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {

    @Autowired
    private OrderUtil orderUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> order(@NonNull @Valid @RequestBody Order order, Errors errors) {

        System.out.println(order);

        try {

            /* If bean validation failed convert errors to readable form (has too many needless info by default). */
            if (errors.hasErrors()) {
                List<String> errorMessages = errors.getFieldErrors().stream()
                        .map(fieldError -> fieldError.toString().split(";")[0] + ". " + fieldError.getDefaultMessage())
                        .collect(Collectors.toList());

                return ResponseEntity.badRequest().body(String.join("\n\r", errorMessages));
            }

            /* Convert bean to dto. */
            OrderDto orderDto = orderUtil.orderToOrderDto(order, orderUtil.getTimeNowInMillis());

            System.out.println(orderDto);

            /* User identification. */
            if (userService.getByEmail(orderDto.getUserEmail()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User with email: '" + orderDto.getUserEmail() + "' is not authorized.");
            }

            /* Try to add order to buffers. */
            try {
                orderService.add(orderDto);
            } catch (TooManyOrdersException e) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
            }

            /* Convert order to message. */
            SimpleMailMessage message = orderUtil.orderToSimpleMailMessage(order);

            /* Try to send message. Delete the order from buffer. */
            try {
                mailSender.send(message);
            }  finally {
                orderService.delete(orderDto);
            }

            /* Success! :) */
            return ResponseEntity.ok("The email has been sent!");

            /* If some sending mail exceptions or any other errors occurred. */
        } catch (Throwable t) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.getMessage());
        }

    }

}