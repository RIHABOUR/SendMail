package com.yuriikovalchuk.util;


import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.dto.OrderDto;
import lombok.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderUtil {

    public OrderDto orderToOrderDto(@NonNull Order order, long orderTimeInMillis) {

        String userEmail = order.getFrom();

        int ccLength = order.getCc() == null ? 0 : order.getCc().size();
        int bccLength = order.getBcc() == null ? 0 : order.getBcc().size();

        int size = order.getContent().getValue().length() * (order.getTo().size() + ccLength + bccLength);

        return new OrderDto(userEmail, orderTimeInMillis, size);
    }

    public SimpleMailMessage orderToSimpleMailMessage(@NonNull Order order) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(order.getFrom());
        message.setReplyTo(order.getReplyTo());
        message.setTo(order.getTo().toArray(new String[order.getTo().size()]));

        int ccLength = order.getCc() == null ? 0 : order.getCc().size();
        message.setCc(ccLength == 0 ? new String[0] : order.getCc().toArray(new String[ccLength]));

        int bccLength = order.getBcc() == null ? 0 : order.getBcc().size();
        message.setBcc(bccLength == 0 ? new String[0] : order.getBcc().toArray(new String[bccLength]));

        message.setSubject(order.getSubject());
        message.setText(order.getContent().getValue());

        return message;
    }

    public long getTimeNowInMillis() {
        return new Date().getTime();
    }

}
