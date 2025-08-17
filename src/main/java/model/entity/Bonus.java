package model.entity;

import util.BonusType;
import util.GameSettings;
import util.Vector2D;
import util.event.*;

public class Bonus extends Entity {
  public final BonusType type;

  public Bonus(Vector2D pos, Vector2D vel, EventBus bus, BonusType type) {
    super(pos, GameSettings.BONUS_RADIUS, GameSettings.BUILDINGS_HEALTH, bus);
    this.velocity = vel;
    this.type = type;
    switch (type) {
      case INCREASE_FIRE_RATE:
        health = 0;
        break;
      case ADD_UNIT:
        health = GameSettings.BONUS_START_COUNTER;
        break;
      case ATTACK_BONUS:
        this.radius = GameSettings.BONUS_ATTACK_RADIUS;
        break;
      case INCREASE_BULLET_DAMAGE:
        this.health = GameSettings.DAMAGE_BONUS_HEALTH;
    }
  }

  @Override
  protected void onUpdate(double dt) {
    // nothing here, just movement according to velocity
  }

  @Override
  public void decreaseHealth(int damage) {
    health += damage;
    if (type == BonusType.INCREASE_FIRE_RATE && health >= GameSettings.FIRE_RATE_BONUS_HEALTH) {
      eventBus.publish(new IncreaseFireRateEvent());
      this.toDestroy();
      return;
    }

    if (type == BonusType.ADD_UNIT && health >= GameSettings.BONUS_MAX_UNITS) {
      health = GameSettings.BONUS_MAX_UNITS;
      return;
    }

    if (type == BonusType.INCREASE_BULLET_DAMAGE && health >= GameSettings.DAMAGE_BONUS_HEALTH) {
      eventBus.publish(new IncreaseBulletDamage());
      this.toDestroy();
    }
  }

  public void applyBonus(Unit un) {
    switch (type) {
      case INCREASE_FIRE_RATE, INCREASE_BULLET_DAMAGE:
        if (health < GameSettings.FIRE_RATE_BONUS_HEALTH) {
          un.toDestroy();
          eventBus.publish(new RelocateEvent());
        }
        break;
      case ADD_UNIT:
        if (health < 0) {
          eventBus.publish(new KillUnitsEvent(-health));
          this.toDestroy();
        }
        if (health >= 0) {
          eventBus.publish(new AddUnitsEvent(health));
          this.toDestroy();
        }
    }
  }
}
