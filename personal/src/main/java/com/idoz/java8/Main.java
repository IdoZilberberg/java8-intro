package com.idoz.java8;

import com.google.common.collect.Lists;
import com.idoz.java8.presentation.Album;
import com.idoz.java8.presentation.Track;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by izilberberg on 2/10/15 for the Java8 presentation
 */
public class Main {

  // Function - before Java 8
  Function<String, Integer> len = new Function<String, Integer>() {

    @Override
    public Integer apply(String string) {
      return string.length();
    }

  };

  //  Function using lambda expression - same thing
  Function<String, Integer> len2 = string -> string.length();

  // Method reference notation (double colons) for calling an existing function
  Function<String, Integer> len3 = String::length;

  // SUPPLIER: accepts nothing, returns something
  Supplier<Album> albumFactory = Album::new;
  Album album = albumFactory.get();

  // CONSUMER: accepts something, returns nothing.

  Consumer<String> printMe = (s) -> System.out.println(s);

  public static void main(String[] args) throws IOException {

    new Main().run();

  }

  public void run() throws IOException {

    Track track1a = new Track(5);
    Track track1b = new Track(3);
    Track track2a = new Track(15);
    Track track2b = new Track(13);

    List<Track> tracks1 = Lists.newArrayList(track1a, track1b);
    List<Track> tracks2 = Lists.newArrayList(track2a, track2b);

    Album album1 = new Album(tracks1);
    Album album2 = new Album(tracks2);

    List<Album> albums = Lists.newArrayList(album1, album2);

    int totalTime = albums.stream().map(Album::getTracks).mapToInt(Track::getLength).sum();
    System.out.println("totalTime=" + totalTime);


//    Files.lines(new File("src/main/resources/timeZones.txt").toPath())
//            .skip(1)
//            .map(s -> s.split("\t"))
//            .map(sarr->MyTimeZone::new)
//            .filter(tz -> tz.getOffsetJan().equals(tz.getOffsetJul()))
//            .forEach(System.out::println);

    //// New operations on regular maps:


    //// Stream.of() can be useful in testing
    Stream<String> countryCodes = Stream.of("IL","UK", "US", "FR", "CA");
    countryCodes.filter(s -> s.startsWith("U")).sorted().forEach(printMe);



  }


}
