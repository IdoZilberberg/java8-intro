package com.idoz.java8.presentation;

import java.util.List;

/**
 * Created by izilberberg on 3/5/15.
 */
public class Track {

  private final int length;

  public Track(int length) {
    this.length = length;
  }

  public int getLength() {
    return length;
  }

  public static int getLength(List<Track> tracks) {
    return 0;
  }
}
