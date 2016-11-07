package com.yuriikovalchuk;

import com.yuriikovalchuk.domain.Content;
import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.domain.User;
import org.springframework.mail.SimpleMailMessage;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestData {

    public static final long INTERVAL = 6000L;

    public static final int MAX_ORDER_PER_INTERVAL = 5;

    public static final String EMAIL = "user@yahoo.com";

    public static final String EMAIL_2 = "admin@gmail.com";

    public static final List<String> EMAILS = Collections.singletonList(EMAIL_2);

    public static final User USER = new User(EMAIL);

    public static final long TIME = 10_000_000_000L;

    public static final int ORDER_ID = 1;

    public static final String SUBJECT = "test mail";

    public static final String CONTENT_TYPE = "text/html";

    public static final String CONTENT_VALUE = "email text";

    public static final Content CONTENT = new Content();

    public static final Order ORDER = new Order();

    public static final Order TEMP_ORDER = new Order();

    public static final int SIZE = CONTENT_VALUE.length() * EMAILS.size();

    public static final SimpleMailMessage MAIL_MESSAGE = new SimpleMailMessage();

    public static final long TIMEOUT = 1000;

    public static final int CAPACITY = 1;

    public static final long MAIL_TIMEOUT = 15;

    public static final TimeUnit MAIL_TIMEUNIT = TimeUnit.SECONDS;

    public static final String USER_PASSWORD = "abcdef123";

    public static final String USER_NAME = "hascode";

    public static final String LOCALHOST = "127.0.0.1";

    static {
        CONTENT.setType(CONTENT_TYPE);
        CONTENT.setValue(CONTENT_VALUE);

        ORDER.setFrom(EMAIL);
        ORDER.setTo(EMAILS);
        ORDER.setSubject(SUBJECT);
        ORDER.setContent(CONTENT);

        TEMP_ORDER.setFrom(EMAIL);
        TEMP_ORDER.setTo(EMAILS);
        TEMP_ORDER.setSubject(SUBJECT);
        TEMP_ORDER.setContent(CONTENT);

        MAIL_MESSAGE.setFrom(EMAIL);
        MAIL_MESSAGE.setTo(EMAILS.toArray(new String[EMAILS.size()]));
        MAIL_MESSAGE.setSubject(SUBJECT);
        MAIL_MESSAGE.setText(CONTENT_VALUE);
    }

    public static final String JSON = "{\n" +
            "  \"from\": \"user@yahoo.com\",\n" +
            "  \"to\": [\"admin@gmail.com\"],\n" +
            "  \"subject\": \"test mail\",\n" +
            "  \"content\": {\n" +
            "    \"type\": \"text/html\",\n" +
            "    \"value\": \"email text\"\n" +
            "  }\n" +
            "}";

    public static final String JSON_WITHOUT_TO = "{\n" +
            "  \"from\": \"user@yahoo.com\",\n" +
            "  \"subject\": \"test mail\",\n" +
            "  \"content\": {\n" +
            "    \"type\": \"text/html\",\n" +
            "    \"value\": \"email text\"\n" +
            "  }\n" +
            "}";

}
