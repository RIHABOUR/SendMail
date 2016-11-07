package com.yuriikovalchuk.service.mail.buffered;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.domain.Report;
import com.yuriikovalchuk.service.mail.AbstractNonBlockingMailService;
import com.yuriikovalchuk.service.mail.MailTask;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

public class BufferedMailService extends AbstractNonBlockingMailService {

    protected TotalAmountOrderBuffer orderBuffer;

    public void setOrderBuffer(TotalAmountOrderBuffer orderBuffer) {
        this.orderBuffer = orderBuffer;
    }

    @Override
    public Integer call() throws Exception {

        CompletionService<Report> completionService = new ExecutorCompletionService<>(executorService);


        while (!(isStopped && submittedTasksWithoutResults == 0)) {

            Order order = null;

            /* Try to get order from repo if service is not stopped. */
            if (!isStopped) {
                order = orderRepository.poll(timeOut, timeUnit);
            }

            if (order == null && submittedTasksWithoutResults == 0) {
                continue;
            }

            /* Take completed task if we has tasks without result or buffer is full. */
            while (order == null && submittedTasksWithoutResults > 0 || order != null && !orderBuffer.add(order)) {
                Future<Report> done = completionService.take();
                submittedTasksWithoutResults--;

                /* Get result and save report to repository. Delete order from buffer. */
                Report report = done.get();
                reportRepository.add(report);
                orderBuffer.remove(report.getOrderId());

                /* Take just one result if we have no order. */
                if (order == null) {
                    break;
                }
            }

            if (order != null) {

                /* Create and submit task. */
                completionService.submit(new MailTask(order, mailSender));
                submittedTasksWithoutResults++;
                processedOrderCount++;
            }

        }

        /* Count of completed tasks with result. */
        return processedOrderCount;
    }

    public void stop() {
        isStopped = true;
    }

}
