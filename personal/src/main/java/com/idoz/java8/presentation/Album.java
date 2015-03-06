package com.idoz.java8.presentation;

import java.util.List;

/**
 * Created by izilberberg on 3/5/15.
 */
public class Album {

  private final List<Track> tracks;

  public Album() {
    this.tracks = null;
  }

  public Album(List<Track> tracks) {
    this.tracks = tracks;
  }

  public List<Track> getTracks() {
    return tracks;
  }
}
