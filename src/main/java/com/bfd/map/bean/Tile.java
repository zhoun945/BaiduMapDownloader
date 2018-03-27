package com.bfd.map.bean;

/**
 * @author zhoun945@gmail
 * @Date 2018-03-27
 */
public class Tile {

  private Integer z;
  private Integer x;
  private Integer y;

  public Tile() {
  }

  public Tile(Integer z, Integer x, Integer y) {
    this.z = z;
    this.x = x;
    this.y = y;
  }

  public Integer getZ() {
    return z;
  }

  public void setZ(Integer z) {
    this.z = z;
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  @Override
  public String toString() {
    return "Tile{" +
        "z=" + z +
        ", x=" + x +
        ", y=" + y +
        '}';
  }

}
