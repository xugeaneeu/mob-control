package util.event.game;

import util.Vector2D;
import util.event.GameEvent;

public class ShootEvent implements GameEvent {
  public final Vector2D position;
  public final Vector2D velocity;

  public ShootEvent(Vector2D position, Vector2D velocity) {
    this.position = position;
    this.velocity = velocity;
  }
}
