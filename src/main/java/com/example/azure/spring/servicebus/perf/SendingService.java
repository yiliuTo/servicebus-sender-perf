package com.example.azure.spring.servicebus.perf;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import io.micrometer.core.annotation.Timed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class SendingService {

	@Autowired
	private ServiceBusSenderAsyncClient sender;

	static ServiceBusMessage serviceBusMessage = new ServiceBusMessage(("1-" + UUID.randomUUID()).getBytes(UTF_8));


	@Async
	@Timed("mytimer")
	public CompletableFuture<?> sendAsync(Executor executor) {
		// @Timed will record the execution time of this method,
		// from the start and until the returned CompletableFuture
		// completes normally or exceptionally.
		// compared with azure-messaging-servicebus-track2-perf, it uses block().
		return CompletableFuture.supplyAsync(() -> sender.sendMessage(serviceBusMessage)
				.block(), executor);
//			.subscribe(unused -> System.out.println("Message sent."),
//					error -> System.err.println("Error occurred while publishing message batch: " + error),
//					() -> {
//						System.out.println("Thread - [" + Thread.currentThread()
//								.getName() + "] - " + LocalTime.now() + ": Message send complete.");
//					}), executor);
	}

}