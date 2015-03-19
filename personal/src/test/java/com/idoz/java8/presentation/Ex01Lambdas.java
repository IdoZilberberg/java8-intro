package com.idoz.java8.presentation;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by izilberberg on 3/16/15.
 * For Java 8 presentation
 */
public class Ex01Lambdas {

  @Ignore
  @Test
  public void bigTest() {
    java7SortWithAnonymous();
    java8SortWithLambdas();
  }

  // Regular anonymous classes before Java 8
  @Test
  public void java7SortWithAnonymous() {

    final List<String> names =
            Arrays.asList("yellow", "orange", "red", "blue", "green");

    // anonymous Comparator
    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    });

    System.out.println(names);
  }

  @Test
  public void java8SortWithLambdas() {

    final List<String> names =
            Arrays.asList("yellow", "orange", "red", "blue", "green");

    // Lambdas - take 1
    // Removed the "new Comparator()"
    // You can still extract to variable of course

    Collections.sort(names, (String a, String b)->{return b.compareTo(a);});


    // Lambdas - take 2 (remove the "return")
    Collections.sort(names, (String a, String b) -> b.compareTo(a));

    // Lambdas - take 3a (remove the types, they are inferred)
    // Called target typing
    Collections.sort(names, (a, b) -> a.compareTo(b)); // note the warning!

            // Lambdas - take 4 (method references)
    Collections.sort(names, String::compareTo);

    System.out.println(names);
  }

  static void display(Supplier<Double> arg) {
    System.out.println(arg.get());
  }

  @Test
  public void moreLambdas() {

    // The original
    final Supplier<Double> randSupplierOrig = new Supplier<Double>() {
      @Override
      public Double get() {
        return Math.random();
      }
    };

    // lambda with no params
    final Supplier<Double> randSupplier = () -> Math.random();

    // this works because Math.random gets nothing and returns Double,
    // just like this Supplier
    final Supplier<Double> randSupplier2 = Math::random;

    display(randSupplier);
    display(() -> 100.0); // an inline lambda

    // lambda with no params and no return value
    final Runnable r = () -> System.out.println("Running!");
    r.run();

  }
}
