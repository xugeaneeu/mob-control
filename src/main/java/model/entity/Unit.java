package model.entity;

import util.Direction;
import util.GameSettings;
import util.Vector2D;
import util.event.EventBus;
import util.event.ShootEvent;

public class Unit extends Entity {
  private Direction direction = Direction.NONE;
  private double shootTimer = 0.0;

  public Unit(Vector2D startPos, EventBus bus) {
    super(startPos, GameSettings.UNIT_RADIUS, GameSettings.UNIT_HEALTH, bus);
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  @Override
  protected void onUpdate(double dt) {
    shootTimer += dt;
    double vx = direction.getDx() * GameSettings.UNIT_SPEED;
    this.velocity = new Vector2D(vx, 0);

    if (shootTimer >= GameSettings.SHOOT_INTERVAL) {
      eventBus.publish(new ShootEvent(position, new Vector2D(0, -GameSettings.BULLET_SPEED)));
      shootTimer -= GameSettings.SHOOT_INTERVAL;
    }
  }

  @Override
  public void decreaseHealth(int damage) {
    health -= damage;
  }
}
