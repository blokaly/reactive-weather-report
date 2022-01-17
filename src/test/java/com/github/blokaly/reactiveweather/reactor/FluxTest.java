package com.github.blokaly.reactiveweather.reactor;

import java.time.Duration;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxTest {

  @Test
  public void simpleFlux() {
    Flux<String> messages = Flux.just("Alex", "Ben", "Charlie").log();

    messages.subscribe(new Subscriber<String>() {
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

    StepVerifier.create(messages)
        .expectNext("Alex")
        .expectNext("Ben")
        .expectNext("Charlie")
        .verifyComplete();
  }

  @Test
  public void simpleFluxLambda() {
    Flux<String> messages = Flux.just("Alex", "Ben", "Charlie").log();

    messages.subscribe(
        System.out::println,
        Throwable::printStackTrace,
        () -> System.out.println("===== Execution completed =====")
    );

    StepVerifier.create(messages)
        .expectNext("Alex")
        .expectNext("Ben")
        .expectNext("Charlie")
        .verifyComplete();
  }


  @Test
  public void simpleFluxError() {
    Flux<String> messages = Flux.error(new RuntimeException());

    messages.subscribe(
        System.out::println,
        Throwable::printStackTrace,
        () -> System.out.println("===== Execution completed =====")
    );

    StepVerifier.create(messages)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  public void emptyFlux() {
    Flux emptyFlux = Flux.empty().log();

    StepVerifier.create(emptyFlux)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  public void neverFlux() {
    Flux neverFlux = Flux.never().log();

    StepVerifier
        .create(neverFlux)
        .expectTimeout(Duration.ofSeconds(2))
        .verify();
  }

  @Test
  public void handleError() {
    Flux<String> names = Flux.just("Alex", "Ben")
        .concatWith(Flux.error(new RuntimeException("Exception occurred..!")))
        .concatWith(Flux.just("Charlie"));

    StepVerifier.create(names)
        .expectNext("Alex")
        .expectNext("Ben")
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  public void rangeFlux() {
    Flux<Integer> range$ = Flux.range(10, 10)
        .filter(num -> Math.floorMod(num, 2) == 1)
        .log();

    StepVerifier.create(range$)
        //.expectNextCount(5)
        .expectNext(11, 13, 15, 17, 19)
        .verifyComplete();
  }


  @Test
  public void transformUsingMap() {

    Flux<String> people = Flux.fromIterable(Arrays.asList("Alex", "Ben", "Charlie"))
        .map(String::toUpperCase)
        .log();

    StepVerifier.create(people)
        .expectNext("ALEX")
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  public void transformUsingFlatMap() {

    Flux<String> people = Flux.just("Alex", "Ben", "Charlie")
        .flatMap(this::asyncCapitalize)
        .log();

    StepVerifier.create(people)
        .expectNext("ALEX")
        .expectNext("BEN")
        .expectNext("CHARLIE")
        .verifyComplete();
  }

  Mono<String> asyncCapitalize(String person) {
    return Mono.just(person.toUpperCase());
  }
}
