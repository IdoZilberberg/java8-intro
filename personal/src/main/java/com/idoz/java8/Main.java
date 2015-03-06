package com.idoz.java8;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.idoz.java8.presentation.Album;
import com.idoz.java8.presentation.Track;

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

  // Using concise method reference notation (double colons) when the function you describe is an existing legacy function
  Function<String, Integer> len3 = String::length;

  // SUPPLIER: accepts nothing, returns something
  Supplier<Album> albumFactory = Album::new;
  Album album = albumFactory.get();

  // CONSUMER: accepts something, returns nothing.

  Consumer<String> consumer = (s) -> System.out.println(s);

  public static void main(String[] args) {

    new Main().run();

  }

  public void run() {

    Track track1a = new Track(5);
    Track track1b = new Track(3);
    Track track2a = new Track(15);
    Track track2b = new Track(13);

    List<Track> tracks1 = Lists.newArrayList(track1a, track1b);
    List<Track> tracks2 = Lists.newArrayList(track2a, track2b);

    Album album1 = new Album(tracks1);
    Album album2 = new Album(tracks2);

    List<Album> albums = Lists.newArrayList(album1, album2);

    albums.stream().map(Album::getTracks).mapToInt(Track::getLength).sum();

    //// New operations on regular maps:

    Map<String, String> englishToFrench = new HashMap<>();
    englishToFrench.put("one", "un");
    englishToFrench.put("two", "deux");

    englishToFrench.forEach((id, str) -> System.out.println(str));
    Stream.of(englishToFrench).map((key, value) -> value).forEach(consumer);

  }

}
