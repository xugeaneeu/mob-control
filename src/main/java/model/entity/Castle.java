package model.entity;

import util.GameSettings;
import util.Vector2D;
import util.event.EventBus;

public class Castle extends Entity {

  public Castle(Vector2D startPos, EventBus eventBus) {
    super(startPos, GameSettings.CASTLE_LENGTH, GameSettings.CASTLE_HEALTH, eventBus);
  }

  @Override
  protected void onUpdate(double dt) {
    //nothing here, it's building
  }

  @Override
  public void decreaseHealth(int damage) {
    health -= damage;
  }
}
