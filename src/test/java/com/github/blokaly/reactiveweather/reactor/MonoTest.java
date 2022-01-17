package com.github.blokaly.reactiveweather.reactor;

import java.time.Duration;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

  @Test
  void simpleMono() {
    Mono<String> message = Mono.just("Hello")
        .map(msg -> msg.concat(" World!")).log();

    message.subscribe(new Subscriber<String>() {
      @Override
      public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
      }

      @Override
      public void onNext(String s) {
        System.out.println("onNext: " + s);
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("====== Execution Completed ======");
      }
    });

    StepVerifier.create(message)
        .expectNext("Hello World!")
        .verifyComplete();
  }

  @Test
  void simpleMonoLambda() {
    Mono<String> message = Mono.just("Hello")
        .map(msg -> msg.concat(" World!")).log();

    message.subscribe(
        System.out::println, // value consumer
        Throwable::printStackTrace, // error consumer
        () -> System.out.println("===== Execution completed =====") // complete consumer
    );

    StepVerifier.create(message)
        .expectNext("Hello World!")
        .expectNextCount(0) //no next element
        .verifyComplete();
  }

  @Test
  void simpleMonoError() {
    Mono<String> message = Mono.error(new RuntimeException("Check error mono"));

    message.subscribe(
        System.out::println, // value consumer
        Throwable::printStackTrace, // error consumer
        () -> System.out.println("===== Execution completed =====") // complete consumer
    );

    StepVerifier.create(message)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  public void emptyMono() {
    Mono empty = Mono.empty().log();

    StepVerifier.create(empty)
        .expectNextCount(0) //optional
        .verifyComplete();
  }

  @Test
  public void noSignalMono() {

    Mono<String> noSignal = Mono.never();

    StepVerifier.create(noSignal)
        .expectTimeout(Duration.ofSeconds(5))
        .verify();
  }

  @Test
  public void fromSupplier() {
    Supplier<String> stringSupplier = () -> "Hello World";
    Mono<String> sMono = Mono.fromSupplier(stringSupplier).log();

    StepVerifier.create(sMono)
        .expectNext("Hello World")
        .verifyComplete();
  }

  @Test
  public void filterMono() {
    Supplier<String> stringSupplier = () -> "Hello";

    Mono<String> filteredMono = Mono.fromSupplier(stringSupplier)
        .filter(str -> str.length() > 5)
        .log();

    StepVerifier.create(filteredMono)
        .verifyComplete();
  }
}
