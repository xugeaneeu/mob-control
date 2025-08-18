package model.entity;

import util.GameSettings;
import util.Vector2D;
import util.event.EventBus;

public class Enemy extends Entity {
  public final int startHealth;

  public Enemy(Vector2D pos, Vector2D vel, EventBus bus) {
    super(pos, GameSettings.ENEMY_RADIUS, GameSettings.ENEMY_HEALTH, bus);
    this.velocity = vel;
    this.startHealth = GameSettings.ENEMY_HEALTH;
  }

  @Override
  protected void onUpdate(double dt) {
    // nothing here, just movement according to velocity
  }

  @Override
  public void decreaseHealth(int damage) {
    health -= damage;
  }
}
