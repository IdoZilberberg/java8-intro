package com.idoz.java8.presentation;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.fail;

/**
 * Created by izilberberg on 3/16/15.
 *
 * For Java 8 presentation
 */
public class Ex02Streams {

  public static final String TIME_ZONES_TXT = "src/main/resources/timeZones.txt";

  @Ignore
  @Test
  public void bigTest() throws IOException {
    simplestStream();
    opTypes();
    createStreamFromListAndCollectBackToList();
    peekExample();
    sortExample();

    createStreamFromIO();
    reduceExample();
    flatMapExample1();
    flatMapExample2();
    flatMapExample3();
  }

  @Test
  public void simplestStream() throws IOException {

    Stream<String> namesStream = Stream.of("Hello", "world");
    namesStream.forEach(s-> System.out.println(s));
    // Can use method ref because Consumer signature same as println

    // this will fail if using same stream
    namesStream.forEach(System.out::println);

    List<String> list = namesStream.collect(Collectors.toList());
    list.forEach(System.out::println);

    // stream of ints - has special ops like average()
    final IntStream numbers = IntStream.of(1, 2, 3, 4, 5, 9, 5, 4, 100, 1, 10);

    // stream from I/O using the new Files.lines()
    final Stream<String> lines =
            Files.lines(new File(TIME_ZONES_TXT).toPath());

  }

  @Test
  public void opTypes() {
    final IntStream numbers = IntStream.of(1, 2, 3, 4, 5, 9, 5, 4, 100, 1, 10);
    final int sum = numbers
            .sorted() // intermediate, eager, stateful
            .distinct() // intermediate, eager, stateful
            .skip(1) // intermediate, lazy, stateless, short-circuit
            .limit(2) // intermediate, lazy, stateless, short-circuit
            .sum(); // terminal, eager, stateful

    System.out.println("Sum: " + sum);
  }

  @Test
  public void createStreamFromListAndCollectBackToList() {

    final List<String> strings = Arrays.asList("A", "B", "E", "D", "C", "A");

    final List<String> modifiedStrings = strings
            .stream()
            .distinct() // removes extra "A"
            .sorted((a, b) -> b.compareTo(a)) // reverse order
            .filter(s -> !s.equals("B")) // Predicate
            .map(s -> s.toLowerCase()) // Function
            .skip(1) // Removes "e"
            .collect(Collectors.toList());

    System.out.println(modifiedStrings);

  }

  @Test
  public void peekExample() {
    final IntStream ints1 = IntStream.of(2, 4, 6);
    final IntStream ints2 = IntStream.of(2, 4, 6, 8, 9, 11, 15);

    ints1
            .peek(System.out::println); // question: why nothing is printed?
    System.out.println("Finished with ints1");

    long sum = ints2
            .peek(System.out::println) // debugging!
            .sum();

    System.out.println("Sum: " + sum);

  }


  @Test
  public void sortExample() {

    // Create stream from list
    final List<String> namesList = Arrays.asList("Obama", "Bush Jr.", "Clinton", "Bush");
    final Stream<String> names = namesList.stream();


    names.sorted((a, b) -> a.compareTo(b)) // intermediate, eager
                       .forEach(System.out::println); // terminal

  }

  @Test
  public void createStreamFromIO() {

    final Stream<String> lines;
    try {
      lines = Files.lines(new File(TIME_ZONES_TXT).toPath());
    } catch(IOException e)  {
      fail("Oops!");
      return;
    }

    lines
            .skip(1) // skip the header line from the file - useful!
            .map(s -> s.split("\t")) // tab-separated string to String[]
            .map(MyTimeZone::new) // ctor accepts String[] for our convenience
            .filter(tz -> tz.getOffsetJan().equals(tz.getOffsetJul())) // accept timezones with no DST
            .filter(tz -> tz.getName().startsWith("Europe"))
            .forEach(System.out::println);
  }

  @Test
  public void reduceExample() {

    Stream<String> namesStream = Stream.of("Hello", "world", "this", "is", "a", "string.");

    System.out.println(namesStream.reduce("", (s1, s2) -> (s1 + " " + s2)));

    final int n = 15;
    long factorial = LongStream
            .rangeClosed(1, n) // rangeClosed() includes the last element; range() doesn't
            .reduce(1, (i1,i2)->(i1*i2));

    System.out.println(n + "! = " + factorial);
  }

  @Test
  public void flatMapExample1() {

    // http://stackoverflow.com/questions/22382453/java-8-streams-flatmap-method-example

    Stream<List<Integer>> integerListStream = Stream
            .of(
                    Arrays.asList(1, 2),
                    Arrays.asList(3, 4),
                    Arrays.asList(5)
            );

    // flatMap() converts each list to stream and internally unwraps the stream
    // It then concatenates all the resulting streams into one
    integerListStream
            //.flatMap((integerList) -> integerList.stream()) // variant 1: lambda expression
            .flatMap(List::stream) // variant 2: static method reference
            .forEach(System.out::println);
  }


  @Test
  public void flatMapExample2() {

    // http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
    List<Order> orders = new ArrayList<>();

    // create orders
    IntStream
      .range(1, 4)
      .forEach(i -> orders.add(new Order("Order" + i)));

    // create lines
    orders.forEach(f ->
       IntStream
          .range(1, 4)
          .forEach(i -> f.lines.add(new Line("Line" + i + " <- " + f.name)))
    );

    // print
    orders.stream()
            .flatMap(f -> f.lines.stream())
            .forEach(b -> System.out.println(b.name));

  }

  @Test
  public void flatMapExample3() {

    // http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/

    IntStream.range(1, 4)
            .mapToObj(i -> new Order("Order" + i)) // returns Stream<Order>
            .peek(order -> IntStream.range(1, 4)
                    .mapToObj(i -> new Line("Line" + i + " <- " + order.name))
                    .forEach(order.lines::add))
            .flatMap(order -> order.lines.stream())
            .forEach(line -> System.out.println(line.name));
  }




  class Order {
    String name;
    List<Line> lines = new ArrayList<>();

    Order(String name) {
      this.name = name;
    }
  }

  class Line {
    String name;

    Line(String name) {
      this.name = name;
    }
  }

}
