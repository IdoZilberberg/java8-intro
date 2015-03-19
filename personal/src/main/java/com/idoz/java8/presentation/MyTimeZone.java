package com.idoz.java8.presentation;

/**
 * Created by izilberberg on 3/18/15.
 */
public class MyTimeZone {
  String code, name;
  Double offsetJan, offsetJul, offsetStandard;

  public MyTimeZone(String[] params)  {
    this.code = params[0];
    this.name = params[1];
    this.offsetJan = Double.parseDouble(params[2]);
    this.offsetJul = Double.parseDouble(params[3]);
    this.offsetStandard = Double.parseDouble(params[4]);
  }

  public MyTimeZone(String code, String name, Double offsetJan, Double offsetJul, Double offsetStandard) {
    this.code = code;
    this.name = name;
    this.offsetJan = offsetJan;
    this.offsetJul = offsetJul;
    this.offsetStandard = offsetStandard;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public Double getOffsetJan() {
    return offsetJan;
  }

  public Double getOffsetJul() {
    return offsetJul;
  }

  public Double getOffsetStandard() {
    return offsetStandard;
  }

  @Override
  public String toString() {
    return "MyTimeZone{" +
            "code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", offsetJan=" + offsetJan +
            ", offsetJul=" + offsetJul +
            ", offsetStandard=" + offsetStandard +
            '}';
  }

}
