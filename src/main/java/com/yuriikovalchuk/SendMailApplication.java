package com.yuriikovalchuk;

import com.yuriikovalchuk.service.mail.threadlimited.ThreadLimitedMailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
@ImportResource({"spring-app.xml", "spring-mail.xml"})
public class SendMailApplication {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ConfigurableApplicationContext context = SpringApplication.run(SendMailApplication.class, args);

//		BufferedMailService bufferedMailService = context.getBean(BufferedMailService.class);
		ThreadLimitedMailService threadLimitedMailService = context.getBean(ThreadLimitedMailService.class);

		ExecutorService executorService = Executors.newSingleThreadExecutor();
//		Future<Integer> result = executorService.submit(bufferedMailService);
		Future<Integer> result = executorService.submit(threadLimitedMailService);


		Thread.sleep(100000);

//		bufferedMailService.stop();
		threadLimitedMailService.stop();

		while (!result.isDone()) {
			Thread.sleep(8000);
		}

		Integer taskCount = result.get();
		System.out.println(taskCount);

	}

}
