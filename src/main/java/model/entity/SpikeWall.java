package model.entity;

import util.GameSettings;
import util.Vector2D;
import util.event.EventBus;

public class SpikeWall extends Entity{

  public SpikeWall(Vector2D startPos, EventBus bus) {
    super(startPos, GameSettings.SPIKE_LENGTH, GameSettings.BUILDINGS_HEALTH, bus);
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
