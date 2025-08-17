package model.entity;

import util.GameSettings;
import util.Vector2D;
import util.event.EventBus;

public class Chainsaw extends Entity{

  public Chainsaw(Vector2D startPos, EventBus eventBus) {
    super(startPos, GameSettings.CHAINSAW_LENGTH, GameSettings.BUILDINGS_HEALTH, eventBus);
  }

  @Override
  protected void onUpdate(double dt) {
    // nothing on update, it's building
  }

  @Override
  public void decreaseHealth(int damage) {
    // nothing here, it's building
  }
}
