package com.yuriikovalchuk;

import com.yuriikovalchuk.domain.Content;
import com.yuriikovalchuk.domain.Order;
import com.yuriikovalchuk.domain.User;
import com.yuriikovalchuk.dto.OrderDto;
import org.springframework.mail.SimpleMailMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestData {

    public static final long TEST_INTERVAL = 6000L;

    public static final int TEST_MAX_ORDER_PER_INTERVAL = 5;

    public static final String TEST_EMAIL = "user@yahoo.com";

    public static final String TEST_EMAIL_2 = "admin@gmail.com";

    public static final List<String> TEST_EMAILS = Collections.singletonList(TEST_EMAIL_2);

    public static final User TEST_USER = new User(TEST_EMAIL);

    public static final long TEST_TIME = 10_000_000_000L;

    public static final int TEST_SIZE = 10;

    public static final OrderDto TEST_ORDER_DTO = new OrderDto(TEST_EMAIL, TEST_TIME, TEST_SIZE);

    public static final OrderDto TEST_ORDER_DTO_2 = new OrderDto(TEST_EMAIL_2, 10_000_000_100L, 45);

    public static final String TEST_SUBJECT = "test mail";

    public static final String TEST_CONTENT_TYPE = "text/html";

    public static final String TEST_CONTENT_VALUE = "email text";

    public static final Content TEST_CONTENT = new Content();

    public static final Order TEST_ORDER = new Order();

    public static final SimpleMailMessage TEST_MAIL_MESSAGE = new SimpleMailMessage();

    static {

        TEST_CONTENT.setType(TEST_CONTENT_TYPE);
        TEST_CONTENT.setValue(TEST_CONTENT_VALUE);

        TEST_ORDER.setFrom(TEST_EMAIL);
        TEST_ORDER.setTo(TEST_EMAILS);
        TEST_ORDER.setSubject(TEST_SUBJECT);
        TEST_ORDER.setContent(TEST_CONTENT);

        TEST_MAIL_MESSAGE.setFrom(TEST_EMAIL);
        TEST_MAIL_MESSAGE.setTo(TEST_EMAILS.toArray(new String[TEST_EMAILS.size()]));
        TEST_MAIL_MESSAGE.setSubject(TEST_SUBJECT);
        TEST_MAIL_MESSAGE.setText(TEST_CONTENT_VALUE);
    }

    public static final String TEST_JSON = "{\n" +
            "  \"from\": \"user@yahoo.com\",\n" +
            "  \"to\": [\"admin@gmail.com\"],\n" +
            "  \"subject\": \"test mail\",\n" +
            "  \"content\": {\n" +
            "    \"type\": \"text/html\",\n" +
            "    \"value\": \"email text\"\n" +
            "  }\n" +
            "}";

    public static final String TEST_JSON_WITHOUT_TO = "{\n" +
            "  \"from\": \"user@yahoo.com\",\n" +
            "  \"subject\": \"test mail\",\n" +
            "  \"content\": {\n" +
            "    \"type\": \"text/html\",\n" +
            "    \"value\": \"email text\"\n" +
            "  }\n" +
            "}";

}
