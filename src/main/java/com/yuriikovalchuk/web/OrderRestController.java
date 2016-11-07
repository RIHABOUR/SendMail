package com.yuriikovalchuk.web;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.service.order.OrderService;
import com.yuriikovalchuk.util.exception.OrderLimitException;
import com.yuriikovalchuk.util.exception.UserNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> order(@NonNull @Valid @RequestBody Order order, Errors errors) {

        try {
            /* If bean validation failed. */
            if (errors.hasErrors()) {
                List<String> messages = errors.getFieldErrors().stream()
                        .map(fieldError -> fieldError.toString().split(";")[0] + ". " + fieldError.getDefaultMessage())
                        .collect(Collectors.toList());

                return ResponseEntity.badRequest().body(String.join("\n\r", messages));
            }

            /* Add order and get its id. */
            int orderId = orderService.add(order);

            /* Success! :) */
            return ResponseEntity.ok("The order has been added. Order id: " + orderId);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (OrderLimitException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service overloaded.");
        } catch (Throwable t) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.getMessage());
        }

    }

}