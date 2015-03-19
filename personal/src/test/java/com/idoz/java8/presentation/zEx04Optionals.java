package com.idoz.java8.presentation;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

/**
 * Created by izilberberg on 3/17/15.
 * For Java 8 presentation
 * WORK IN PROGRESS!!!
 */
public class zEx04Optionals {

  @Test
  public void simpleOptionals() {

    // full stream - ok
    final IntStream numbers = IntStream.of(1, 2, 3, 4);
    System.out.println(numbers.average());
    System.out.println(numbers.average().getAsDouble());

    // empty stream
    final IntStream numbers2 = IntStream.of(); // or .empty()
    OptionalDouble maybeNumber = numbers2.average();
    System.out.println(maybeNumber);

    System.out.println(maybeNumber.orElse(0.0));

    if(maybeNumber.isPresent()) {
      System.out.println("Has a value");
    } else  {
      System.out.println("No value");
    }

    maybeNumber.ifPresent(d-> System.out.println("Value is " +d ));


  }


  ///////////////

  @Test
  public void filterWithoutOptionals() {
    List<String> strings = new ArrayList<>();
    strings.add("Hello");
    strings.add(null);
    strings.add("World");

    int totalLength = strings
            .stream()
            .mapToInt(String::length) // to IntStream so we get sum()
            .sum();

    System.out.println(totalLength);

  }

  @Ignore
  @Test
  public void filterWithOptionals() {
    List<String> strings = new ArrayList<>();
    strings.add("Hello");
    strings.add(null);
    strings.add("World");

    strings
            .stream()
            .map(s -> Optional.ofNullable(s))
            //.mapToInt(o->o.isPresent()?o.get().length():0)
            //.mapToInt(o->o.get().length()) // not good
            //.flatMapToInt(String::length)
            .forEach(System.out::println);
    //System.out.println(totalLength);
  }

  @Test
  public void flatMapOptionals()  {

    Outer outer = new Outer();
    Nested nested = new Nested();
    Inner inner = new Inner();
    inner.foo = "HELLO";

    // change this and run
    outer.nested = nested;
    nested.inner = inner;

    Outer outer2 = new Outer();
    Nested nested2 = new Nested();
    Inner inner2 = new Inner();
    inner2.foo = "HELLO2";

    // change this and run
    //outer2.nested = nested2;
    //nested2.inner = inner2;

    // null-checks
    if (outer != null && outer.nested != null && outer.nested.inner != null) {
      System.out.println(outer.nested.inner.foo);
    }

    // flatMap returns the value of Optional if non-empty
    // o/w EMPTY
    Optional.of(outer2)
            .flatMap(o -> Optional.ofNullable(o.nested))
            .flatMap(n -> Optional.ofNullable(n.inner))
            .flatMap(i -> Optional.ofNullable(i.foo))
            .ifPresent(System.out::println);

  }

  class Outer {
    Nested nested;
  }

  class Nested {
    Inner inner;
  }

  class Inner {
    String foo;
  }

}
