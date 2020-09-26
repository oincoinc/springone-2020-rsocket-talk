package com.example.demo;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Controller
public class RSocketServerApplication {

	private static final Logger log = LoggerFactory.getLogger(RSocketServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RSocketServerApplication.class, args);
	}

	@MessageMapping("request-response")
	Mono<Message> requestResponse(final Message message/*, @AuthenticationPrincipal UserDetails user*/) {
		log.info("Received request-response message: {}", message);
		return Mono.just(new Message("You said: " + message.getMessage()));
	}

	@MessageMapping("fire-and-forget")
	public Mono<Void> fireAndForget(final Message message/*, @AuthenticationPrincipal UserDetails user*/) {
		log.info("Received fire-and-forget request: {}", message);
		return Mono.empty();
	}

	@MessageMapping("request-stream")
	Flux<Message> stream(final Message message/*, @AuthenticationPrincipal UserDetails user*/) {
		log.info("Received stream request: {}", message);
		return Flux
				// create a new indexed Flux emitting one element every second
				.interval(Duration.ofSeconds(1))
				// create a Flux of new Messages using the indexed Flux
				.map(index -> new Message("You said: " + message.getMessage() + ". Response #" + index))
				// show what's happening
				.log();
	}

	@MessageMapping("stream-stream")
	Flux<Message> channel(final Flux<Integer> settings/*, @AuthenticationPrincipal UserDetails user*/) {
		log.info("Received stream-stream (channel) request...");
		return settings
				.doOnNext(setting -> log.info("Requested interval is {} seconds.", setting))
				.doOnCancel(() -> log.warn("The client cancelled the channel."))
				.switchMap(setting -> Flux.interval(Duration.ofSeconds(setting))
						.map(index -> new Message("Stream Response #" + index)))
				.log();
	}
}