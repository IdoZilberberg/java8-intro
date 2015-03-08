package com.idoz.stuff;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by izilberberg on 1/13/15.
 */
public class Main {

  public static void main(String[] args) {

    //randomStuff();
    //datetime();
    timezones();
  }

  private static void datetime() {
    DateTime dt = new DateTime();
    DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
    String str = fmt.print(dt);
    System.out.println(str);
  }

  private static void randomStuff() {
    int j = 0;
    for (int i = 0; i < 1000000; i++) {
      if ((int) (Math.random() * 100) % 100 == 0)
        j++;
    }
    System.out.println(j);
    List<String> myList = Lists.newArrayList("IDO");
    System.out.println(myList);
  }

  private static void timezones() {

    DateTime now = new DateTime();
    DateTimeZone tzNY = DateTimeZone.forID("America/New_York");
    DateTimeZone tzNZ = DateTimeZone.forID("Pacific/Auckland");
    DateTimeZone tzUK = DateTimeZone.forID("Europe/London");
    System.out.println(tzNY.getID() + ": " + tzNY.getOffset(now.getMillis())/3600);
    System.out.println(tzNZ.getID() + ": " + tzNZ.getOffset(now.getMillis())/3600);
    System.out.println(tzUK.getID() + ": " + tzUK.getOffset(now.getMillis())/3600);

    final DateTimeFormatter offsetFormatter = new DateTimeFormatterBuilder()
            .appendLiteral("GMT")
            .appendTimeZoneOffset(null, true, 2, 4)
            .toFormatter();

    Function toTimeZoneData = new Function<String, TimeZoneData>() {
      @Override
      public TimeZoneData apply(final String from) {
        final DateTimeZone tz = DateTimeZone.forID(from);
        final String tzOffset = offsetFormatter.withZone(tz).print(now.getMillis());
        final String tzDesc = tz.getID();
        return new TimeZoneData(tzOffset, tzDesc);
      }
    };

    Set<TimeZoneData> allZonesUnsorted = Sets.newHashSet(Iterables.transform(DateTimeZone.getAvailableIDs(), toTimeZoneData));

    List<TimeZoneData> allZones = Lists.newArrayList(allZonesUnsorted);
    Collections.sort(allZones);

    for( TimeZoneData data : allZones ) {
      System.out.println(data.tzOffset + " " + data.tzDescription);
    }


//    System.out.println(cOffsetFormatter.withZone(tzNY).print(now.getMillis()));
//    System.out.println(cOffsetFormatter.withZone(tzNZ).print(now.getMillis()));
//    System.out.println(cOffsetFormatter.withZone(tzUK).print(now.getMillis()));



  }

  public static class TimeZoneData implements Comparable<TimeZoneData> {

    private final String tzOffset;
    private final String tzDescription;

    public TimeZoneData(String tzOffset, String tzDescription) {
      this.tzOffset = tzOffset;
      this.tzDescription = tzDescription;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof TimeZoneData)) return false;

      TimeZoneData that = (TimeZoneData) o;

      if (!tzDescription.equals(that.tzDescription)) return false;
      if (!tzOffset.equals(that.tzOffset)) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = tzOffset.hashCode();
      result = 31 * result + tzDescription.hashCode();
      return result;
    }

    @Override
    public int compareTo(final TimeZoneData other) {
      int compareOffsets = tzOffset.compareTo(other.tzOffset);
      if( compareOffsets!=0 ) {
        return compareOffsets;
      }

      return tzDescription.compareTo(other.tzDescription);

    }
  }




}
