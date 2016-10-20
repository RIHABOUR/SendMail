package com.yuriikovalchuk.repository.buffer;

import com.yuriikovalchuk.dto.OrderDto;
import com.yuriikovalchuk.util.TooManyOrdersException;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserOrderBufferImpl implements UserOrderBuffer {

    private Map<String, LinkedList<Long>> orderTimesByUser = new ConcurrentHashMap<>();

    private long interval;

    private int maxOrderCountPerInterval;

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setMaxOrderCountPerInterval(int maxOrderCountPerInterval) {
        this.maxOrderCountPerInterval = maxOrderCountPerInterval;
    }

    @Override
    public void add(@NonNull OrderDto order) throws TooManyOrdersException {

        LinkedList<Long> orderTimes = orderTimesByUser.computeIfAbsent(order.getUserEmail(), k -> new LinkedList<>());

        synchronized (orderTimes) {

            if (orderTimes.size() == maxOrderCountPerInterval) {

                long differenceBetweenCurrentOrderAndFirst = order.getTimeInMillis() - orderTimes.getFirst();

                if (differenceBetweenCurrentOrderAndFirst >= interval) {
                    orderTimes.removeFirst();
                } else  {
                    long timeout = interval - differenceBetweenCurrentOrderAndFirst;
                    throw new TooManyOrdersException("Server is overloaded. Try in " + timeout + " milliseconds.");
                }
            }

            orderTimes.addLast(order.getTimeInMillis());
        }
    }

    @Override
    public void deleteOutOfIntervalOrdersByUser(@NonNull String userEmail) {

        LinkedList<Long> orderTimes = orderTimesByUser.get(userEmail);

        if (orderTimes != null) {

            synchronized (orderTimes) {

                long timeNowInMillis = new Date().getTime();

                while (orderTimes.size() > 0 && timeNowInMillis - orderTimes.getFirst() > interval) {
                    orderTimes.removeFirst();
                }
            }
        }
    }

}
