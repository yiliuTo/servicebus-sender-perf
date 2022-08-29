package com.example.azure.spring.servicebus.perf;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

		MeterRegistry meterRegistry = applicationContext.getBean(MeterRegistry.class);
		SendingService sendingService = applicationContext.getBean(SendingService.class);
		CountDownLatch countdownLatch = new CountDownLatch(1);

		// Compared with azure-messaging-servicebus-track2-perf, it uses single thread for testing.
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
		for (int i = 0; i < 100; i++) {
			sendingService.sendAsync(scheduledThreadPool);
		}

		countdownLatch.await(30, TimeUnit.SECONDS);
		Timer mytimer = meterRegistry.get("mytimer").timer();
		double rst = mytimer.count() / mytimer.totalTime(TimeUnit.SECONDS);
		System.out.println("==============Rst: " + rst);
		System.out.println("Shutting down executor...");
		scheduledThreadPool.shutdown();
		boolean isDone;
		do {
			isDone = scheduledThreadPool.awaitTermination(1, TimeUnit.MINUTES);
			System.out.println("awaitTermination...");
		}
		while (!isDone);

		System.out.println("Finished all threads");
	}

}
