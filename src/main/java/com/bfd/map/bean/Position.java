package com.bfd.map.bean;

/**
 * @author zhoun945@gmail
 * @Date 2018-03-27
 */
public class Position {

  private String name;
  private double swlng;
  private double swlat;
  private double nelng;
  private double nelat;

  public Position(String name, double swlng, double swlat, double nelng, double nelat) {
    super();
    this.name = name;
    this.swlng = swlng;
    this.swlat = swlat;
    this.nelng = nelng;
    this.nelat = nelat;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getSwlng() {
    return swlng;
  }

  public void setSwlng(double swlng) {
    this.swlng = swlng;
  }

  public double getSwlat() {
    return swlat;
  }

  public void setSwlat(double swlat) {
    this.swlat = swlat;
  }

  public double getNelng() {
    return nelng;
  }

  public void setNelng(double nelng) {
    this.nelng = nelng;
  }

  public double getNelat() {
    return nelat;
  }

  public void setNelat(double nelat) {
    this.nelat = nelat;
  }
}
