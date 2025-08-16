package util;

public record Vector2D(double x, double y) {

  public static Vector2D zeroVector() {
    return new Vector2D(0, 0);
  }

  public Vector2D add(Vector2D v) {
    return new Vector2D(x + v.x, y + v.y);
  }

  public Vector2D mul(double scalar) {
    return new Vector2D(x * scalar, y * scalar);
  }

  @Override
  public String toString() {
    return String.format("(%.2f, %.2f)", x, y);
  }
}
