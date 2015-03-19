package com.idoz.java8.presentation;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by izilberberg on 3/17/15.
 *
 * For Java 8 presentation
 */
public class Ex03Final {

  public static final String GEO_CITY_CSV = "src/main/resources/GeoLiteCity-Location.csv";

  // Demonstrate streams with the new java.time package
  @Test
  public void timezones() {

    final Instant now = Instant.now();
    final Set<String> availableTimeZones = ZoneId.getAvailableZoneIds();

    System.out.println(availableTimeZones.stream().count());

    Function<String, ZoneId> toZoneId = tzID -> ZoneId.of(tzID);
    Function<String, ZoneId> toZoneId2 = ZoneId::of;

    availableTimeZones.stream()
    //      .map(tzID -> ZoneId.of(tzID)) // variant 1: Explicit lambda
    //      .map(toZoneId2) // variant 2: From lambda variable
            .map(ZoneId::of) // variant 3: Method reference (static factory) - inferred type
            .filter(zone -> zone.getRules().isDaylightSavings(now))
            .forEach(tz -> System.out.println("TZ: " + tz));
  }

  @Test
  public void bigfile() throws IOException, InterruptedException {


    Stream<String> lines =
            Files.lines(new File(GEO_CITY_CSV).toPath(),
                    Charset.forName("ISO-8859-1"));
    List<String> linesList = lines
            .skip(2)
            .collect(Collectors.toList());

    final Instant startTime = Instant.now();
    //

    // debugging: if you remove the skip(2) above, it will fail
    // try it and use peek() to debug
    List<MyGeoCity> cities = linesList
            .stream()
            //.peek(System.out::println)
            //.limit(1000)
            .map(line -> line.split(","))
            .map(MyGeoCity::new)
            .collect(Collectors.toList());

    /////////////

    Map<String,List<MyGeoCity>> countryToCities =
            linesList.stream()
            .limit(1000)
            .map(line -> line.split(","))
            .map(MyGeoCity::new)
            .collect(Collectors.groupingBy(MyGeoCity::getCountry));

    System.out.println("Line count: " + cities.size());
    System.out.println("Country count: " +
            countryToCities.keySet().size());

    final Instant endTime = Instant.now();

    System.out.println(
            ChronoUnit.MILLIS.between(startTime,endTime) + " ms");








  }

}
