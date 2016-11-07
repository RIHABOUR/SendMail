package com.yuriikovalchuk.service.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.yuriikovalchuk.TestData.*;
import static org.junit.Assert.*;

public class MailTaskTest {

    private MailTask task;

    private JavaMailSenderImpl sender;

    private GreenMail mailServer;

    @Before
    public void setUp() {
        mailServer = new GreenMail(ServerSetupTest.SMTP);
        mailServer.start();
        mailServer.setUser(EMAIL, USER_NAME, USER_PASSWORD);

        sender = new JavaMailSenderImpl();
        sender.setPort(ServerSetupTest.SMTP.getPort());
        sender.setHost(LOCALHOST);
        sender.setUsername(USER_NAME);
        sender.setPassword(USER_PASSWORD);

        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        sender.setJavaMailProperties(properties);

        TEMP_ORDER.setUser(USER);
        TEMP_ORDER.setId(ORDER_ID);
        task = new MailTask(TEMP_ORDER, sender);
    }

    @After
    public void tearDown() {
        mailServer.stop();
    }

    @Test
    public void testCall() throws Exception {
        task.call();

        MimeMessage[] messages = mailServer.getReceivedMessages();

        assertNotNull(messages);
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];

        assertEquals(EMAIL, message.getFrom()[0].toString());
        assertEquals(SUBJECT, message.getSubject());
        assertEquals(CONTENT_TYPE, message.getContentType().split(";")[0]);
        assertEquals(CONTENT_VALUE, message.getContent().toString().trim());
    }

    @Test
    public void testFillMessage() throws Exception {
        MimeMessage emptyMessage = sender.createMimeMessage();

        assertNull(emptyMessage.getSubject());

        MimeMessage filledMessage = task.fillMessage(emptyMessage, TEMP_ORDER);

        assertEquals(SUBJECT, filledMessage.getSubject());
    }
}