package model.entity;

import util.GameSettings;
import util.Vector2D;
import util.event.EventBus;

public class Bullet extends Entity {

  public Bullet(Vector2D pos, Vector2D vel, EventBus bus) {
    super(pos, GameSettings.BULLET_RADIUS, bus);
    this.velocity = vel;
  }

  @Override
  protected void onUpdate(double dt) {
    if (this.position.y() < 0) {
      this.toDestroy();
    }
  }

  //TODO: refactor later according to bullet level
  public int getDamage() {
    return GameSettings.BULLET_DAMAGE;
  }
}
