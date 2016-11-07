package com.yuriikovalchuk.service.mail;

import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.domain.Report;
import lombok.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Callable;

public class MailTask implements Callable<Report> {

    private Order order;

    private JavaMailSender mailSender;

    public MailTask(Order order, JavaMailSender mailSender) {
        this.order = order;
        this.mailSender = mailSender;
    }

    @Override
    public Report call() {

        Report report = new Report(order.getId(), order.getUser().getId());

        try {
            MimeMessage message = fillMessage(mailSender.createMimeMessage(), order);

            mailSender.send(message);

            report.setMessage("Success!");
        } catch (Exception e) {
            report.setMessage(e.getMessage());
        }

        return report;
    }

    public MimeMessage fillMessage(@NonNull MimeMessage message, @NonNull Order order) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        helper.setFrom(order.getFrom());

        String replyTo = order.getReplyTo();
        if (replyTo != null) {
            helper.setReplyTo(replyTo);
        }

        helper.setSubject(order.getSubject());
        helper.setTo(order.getTo().toArray(new String[order.getTo().size()]));

        boolean isContentTypeHtml = order.getContent().getType().equals("text/html");
        helper.setText(order.getContent().getValue(), isContentTypeHtml);

        int ccLength = order.getCc() == null ? 0 : order.getCc().size();
        helper.setCc(ccLength == 0 ? new String[0] : order.getCc().toArray(new String[ccLength]));

        int bccLength = order.getBcc() == null ? 0 : order.getBcc().size();
        helper.setBcc(bccLength == 0 ? new String[0] : order.getBcc().toArray(new String[bccLength]));

        return message;
    }

}
