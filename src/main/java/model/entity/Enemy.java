package model.entity;

import util.GameSettings;
import util.Vector2D;
import util.event.EventBus;

public class Enemy extends Entity {

  public Enemy(Vector2D pos, Vector2D vel, EventBus bus) {
    super(pos, GameSettings.ENEMY_RADIUS, bus);
    this.velocity = vel;
  }

  @Override
  protected void onUpdate(double dt) {
    // nothing here, just movement according to velocity
  }
}
