package com.idoz.java8.presentation;

/**
 * Created by izilberberg on 3/18/15.
 */
public class MyGeoCity {

  public String locId, country, region, city, postalCode;

  public MyGeoCity(String[] params) {
    this.locId = params[0];
    this.country = params[1];
    this.region = params[2];
    this.city = params[3];
    this.postalCode = params[4];
  }

  public String getLocId() {
    return locId;
  }

  public String getCountry() {
    return country;
  }

  public String getRegion() {
    return region;
  }

  public String getCity() {
    return city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  @Override
  public String toString() {
    return "MyGeoCity{" +
            "id='" + locId + '\'' +
            ", country='" + country + '\'' +
            ", region='" + region + '\'' +
            ", city='" + city + '\'' +
            ", zip='" + postalCode + '\'' +
            '}';
  }
}
