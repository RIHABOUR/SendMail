package com.yuriikovalchuk.service.mail.threadlimited;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.domain.Report;
import com.yuriikovalchuk.service.mail.AbstractNonBlockingMailService;
import com.yuriikovalchuk.service.mail.MailTask;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

public class ThreadLimitedMailService extends AbstractNonBlockingMailService {

    private int threadCount;

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public Integer call() throws Exception {

        CompletionService<Report> completionService = new ExecutorCompletionService<>(executorService);

        while (!(isStopped && submittedTasksWithoutResults == 0)) {

            Order order = null;

            if (!executorService.isShutdown()) {
                order = orderRepository.poll(timeOut, timeUnit);
            }

            if (order == null && submittedTasksWithoutResults == 0) {
                continue;
            }

            if (order == null && submittedTasksWithoutResults > 0 || submittedTasksWithoutResults > threadCount) {
                Future<Report> done = completionService.take();
                submittedTasksWithoutResults--;

                Report report = done.get();
                reportRepository.add(report);
            }

            if (order != null) {
                completionService.submit(new MailTask(order, mailSender));
                submittedTasksWithoutResults++;
                processedOrderCount++;
            }

        }

        return processedOrderCount;
    }

    @Override
    public void stop() {
        isStopped = true;
    }
}
