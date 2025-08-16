package model.entity;

import util.BonusType;
import util.GameSettings;
import util.Vector2D;
import util.event.AddUnitsEvent;
import util.event.EventBus;
import util.event.IncreaseFireRateEvent;
import util.event.RelocateEvent;

public class Bonus extends Entity {
  public final BonusType type;
  private int counter;

  public Bonus(Vector2D pos, Vector2D vel, EventBus bus, BonusType type) {
    super(pos, GameSettings.BONUS_RADIUS, bus);
    this.velocity = vel;
    this.type = type;
    switch (type) {
      case INCREASE_FIRE_RATE:
        counter = 0;
        break;
      case ADD_UNIT:
        counter = GameSettings.BONUS_START_COUNTER;
        break;
      case ATTACK_BONUS:
        this.radius = GameSettings.BONUS_ATTACK_RADIUS;
    }
  }

  @Override
  protected void onUpdate(double dt) {
    // nothing here, just movement according to velocity
  }

  //TODO: need to refactor later (it updates with specific logic according to bonus type)
  public void incrementCounter(int amountOfDamage) {
    counter += amountOfDamage;
    if (type == BonusType.INCREASE_FIRE_RATE && counter >= GameSettings.BONUS_HEALTH) {
      eventBus.publish(new IncreaseFireRateEvent());
      this.toDestroy();
      return;
    }

    if (type == BonusType.ADD_UNIT && counter >= GameSettings.BONUS_MAX_UNITS) {
      counter = GameSettings.BONUS_MAX_UNITS;
    }
  }

  public void applyBonus(Unit un) {
    switch (type) {
      case INCREASE_FIRE_RATE:
        if (counter < GameSettings.BONUS_HEALTH) {
          un.toDestroy();
          eventBus.publish(new RelocateEvent());
        }
        break;
      case ADD_UNIT:
        if (counter < 0) {
          // TODO: publish delete -counter units and relocate event
          this.toDestroy();
        }
        if (counter >= 0) {
          eventBus.publish(new AddUnitsEvent(counter));
          this.toDestroy();
        }
    }
  }
}
