package util;

public enum Direction {
  LEFT(-1),
  NONE(0),
  RIGHT(1);

  private final int dx;

  Direction(int dx) {
    this.dx = dx;
  }

  public int getDx() {
    return dx;
  }
}
