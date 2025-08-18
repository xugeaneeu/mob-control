package model;

import model.entity.*;
import model.service.CollisionService;
import model.service.SpawnerService;
import util.GameSettings;
import util.GameStatistic;
import util.event.*;
import util.event.game.*;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
  private final List<Entity> entities = new ArrayList<>();
  private final EventBus eventBus;
  private final SpawnerService spawner;
  private final CollisionService collision;

  private static double playTime = 0.0;
  private static long enemyScore = 0;
  private static long bonusScore = 0;

  private double waveAccumulator = 0.0;

  public GameModel(EventBus eventBus) {
    this.eventBus = eventBus;
    collision = new CollisionService(eventBus);
    spawner = new SpawnerService(this);

    eventBus.addSubscriber(event -> {
      if (event instanceof ShootEvent se) {
        addEntity(new Bullet(se.position, se.velocity, eventBus));
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof IncreaseFireRateEvent) {
        GameSettings.BULLET_SPEED = (int) ((double)GameSettings.BULLET_SPEED * 1.1);
        bonusScore++;
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof AddUnitsEvent auEvent) {
        spawner.spawnUnits(auEvent.amountOfUnits);
        bonusScore++;
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof KillUnitsEvent kue) {
        int toKill = kue.amount;
        for (int i = entities.size() - 1; i >= 0 && toKill > 0; i--) {
          Entity e = entities.get(i);
          if (e instanceof Unit) {
            e.toDestroy();
            toKill--;
          }
        }
        eventBus.publish(new RelocateEvent());
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof IncreaseBulletDamageEvent) {
        GameSettings.BULLET_DAMAGE++;
        bonusScore++;
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof HealCastleEvent hce) {
        getCastle().decreaseHealth(-hce.amountHP);
        bonusScore++;
      }
    });

    spawner.initSpawn();
  }

  public void addEntity(Entity entity) {entities.add(entity);}

  public List<Entity> getEntities() {return entities;}

  public Entity getHeadUnit() {
    for (Entity e : entities) {
      if (e instanceof Unit) {return e;}
    }
    return null;
  }

  public Castle getCastle() {
    for (Entity e : entities) {
      if (e instanceof Castle) {return (Castle) e;}
    }
    return null;
  }

  public int countUnits() {
    int count = 0;
    for (Entity entity : entities) {
      if (entity instanceof Unit && entity.isAlive()) count++;
    }
    return count;
  }

  public EventBus getEventBus() {return eventBus;}
  public GameStatistic getStatistic() {
    return new GameStatistic(playTime, enemyScore, bonusScore, GameSettings.BULLET_DAMAGE);
  }
  static public double getTime() {return playTime;}
  static public long getEnemyScore() {return enemyScore;}
  static public long getBonusScore() {return bonusScore;}

  private void updateWaves(double dt) {
    waveAccumulator += dt;
    if (waveAccumulator >= GameSettings.WAVE_INTERVAL) {
      GameSettings.ENEMY_HEALTH++;
      GameSettings.UNIT_HEALTH++;
      GameSettings.HEALING_BONUS_HEALTH = (int) ((double) GameSettings.HEALING_BONUS_HEALTH/
                                                          (GameSettings.ENEMY_HEALTH - 1)*
                                                          GameSettings.ENEMY_HEALTH);
      waveAccumulator -= GameSettings.WAVE_INTERVAL;
    }
  }

  public void update(double dt) {
    updateWaves(dt);
    playTime += dt;
    spawner.update(dt);

    for (Entity entity : new ArrayList<>(entities)) {
      if (entity.isAlive()) entity.update(dt);
    }

    collision.processCollision(entities);

    cleanUpDead(entities);
  }

  private void cleanUpDead(List<Entity> entities) {
    entities.removeIf(e -> {
      if (!e.isAlive()) {
        if (e instanceof Enemy en) {
          enemyScore += en.startHealth;
        }
        return true;
      }
      return false;
    });
  }

  public void statsToDefaults() {
    playTime = 0.0;
    enemyScore = 0;
    bonusScore = 0;
  }
}
