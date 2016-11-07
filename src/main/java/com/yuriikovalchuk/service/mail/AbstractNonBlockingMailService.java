package com.yuriikovalchuk.service.mail;

import com.yuriikovalchuk.repository.order.OrderRepository;
import com.yuriikovalchuk.repository.report.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNonBlockingMailService implements Callable<Integer> {

    protected boolean isStopped;

    protected int submittedTasksWithoutResults;

    protected int processedOrderCount;

    protected long timeOut;

    protected TimeUnit timeUnit;

    protected JavaMailSender mailSender;

    protected ExecutorService executorService;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ReportRepository reportRepository;

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public abstract Integer call() throws Exception;

    public abstract void stop();
}
