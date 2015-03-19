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
    filterExample();
    mappingExample();
    sortingExample();
    sumExample();
    collectExample();
    opTypes();
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
    //namesStream.forEach(s-> System.out.println(s));
    // Can use method ref because Consumer signature same as println

    // this will fail if using same stream
    //namesStream.forEach(System.out::println);

//    List<String> list = namesStream.collect(Collectors.toList());
//    list.forEach(System.out::println);

    // stream of ints - has special ops like average()
//    final IntStream numbers =
//            IntStream.of(1, 2, 3, 4, 5, 9, 5, 4, 100, 1, 10);

    // stream from I/O using the new Files.lines()
    final Stream<String> lines =
          Files.lines(new File(TIME_ZONES_TXT).toPath());

  }

  @Test
  public void filterExample() {
    Stream<String> namesStream = Stream.of("One", "Two", "Three", "Four");

    namesStream
      .filter(s -> s.startsWith("T"))
      .forEach(System.out::println);

  }

  @Test
  public void mappingExample() {
    Stream<String> namesStream = Stream.of("One", "Two", "Three", "Four");

    namesStream
      .map(s -> s.substring(1))
      .forEach(System.out::println);


  }

  @Test
  public void sortingExample()  {
    Stream<String> namesStream = Stream.of("One", "Two", "Three", "Four");
    namesStream
            //.sorted() // natural order
            // custom order
      .sorted((s1, s2) -> (s1.substring(1).compareTo(s2.substring(1))))
      .forEach(System.out::println);
  }

  @Test
  public void sumExample()  {
    final IntStream numbers =
            IntStream.of(1, 2, 3, 4, 5, 9, 5, 4, 100, 1, 10);
    int sum = numbers.sum();
    System.out.println("Sum: " + sum);
  }

  @Test
  public void matchExample()  {
    Stream<String> namesStream = Stream.of("One", "Two", "Three", "Four");

    //namesStream.forEach(System.out::println);

    // note the "short-circuit" feature of anyMatch()
    boolean found = namesStream
            .peek(System.out::println)
            .skip(1)
            .sorted()
            .anyMatch(s -> s.startsWith("Tw"));

    //System.out.println(found);

  }

  @Test
  public void collectExample() {

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
  public void opTypes() {

//    final IntStream num2 = IntStream.of(1,2,3);
//    num2.forEach(System.out::println);
//
    final IntStream numbers = IntStream.of(1, 8, 3, 4, 5, 9, 5, 4, 100, 1, 10);
    numbers
            .peek(i -> System.out.println(i))
            .sorted() // intermediate, eager, stateful
            .distinct() // intermediate, eager, stateful
            .skip(2) // intermediate, lazy, stateless, short-circuit
            .limit(2) // intermediate, lazy, stateless, short-circuit
            .sum(); // terminal, eager, stateful, actually runs all!
    ;
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

    // associative
    // 1x2x3x4x5x6 = (1x2)x(3x4)x(5x6) = 2x12x30 = 720
    final int n = 15;
    long factorial = LongStream
            .rangeClosed(1, n) // rangeClosed() includes the last element; range() doesn't

            .parallel() // parallel version of LongStream

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
            .flatMap(list->list.stream()) // variant 2: static method reference
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
