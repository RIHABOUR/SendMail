package com.yuriikovalchuk.util;

import com.yuriikovalchuk.domain.Order;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderUtil {

    public int calculateSize(@NonNull Order order) {

        int toSize = order.getTo().size();

        int ccSize = order.getCc() == null ? 0 : order.getCc().size();

        int bccSize = order.getBcc() == null ? 0 : order.getBcc().size();

        return order.getContent().getValue().length() * (toSize + ccSize + bccSize);

    }

    public long getCurrentTime() {
        return new Date().getTime();
    }

}
