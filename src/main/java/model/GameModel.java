package model;

import model.entity.Bullet;
import model.entity.Entity;
import model.entity.Unit;
import model.service.CollisionService;
import model.service.SpawnerService;
import util.GameSettings;
import util.event.*;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
  private final List<Entity> entities = new ArrayList<>();
  private final EventBus eventBus = new EventBus();
  SpawnerService spawner = new SpawnerService(this);
  CollisionService collision = new CollisionService(eventBus);

  private double waveAccumulator = 0.0;
  private int wave = 1;

  public GameModel() {
    eventBus.addSubscriber(event -> {
      if (event instanceof ShootEvent se) {
        addEntity(new Bullet(se.position, se.velocity, eventBus));
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof IncreaseFireRateEvent) {
        GameSettings.BULLET_SPEED = (int) ((double)GameSettings.BULLET_SPEED * 1.1);
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof AddUnitsEvent auEvent) {
        System.out.println("I will spawn " + auEvent.amountOfUnits + " units");
        spawner.spawnUnits(auEvent.amountOfUnits);
      }
    });

    eventBus.addSubscriber(event -> {
      if (event instanceof GameOverEvent) {
        System.out.println("Game over");
        // TODO: when state machine is ready, change state in GameController or MainApp idk
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

  public int countUnits() {
    int count = 0;
    for (Entity entity : entities) {
      if (entity instanceof Unit && entity.isAlive()) count++;
    }
    return count;
  }

  public EventBus getEventBus() {return eventBus;}

  private void updateWaves(double dt) {
    waveAccumulator += dt;
    if (waveAccumulator >= GameSettings.WAVE_INTERVAL) {
      GameSettings.ENEMY_HEALTH++;
      GameSettings.UNIT_HEALTH++; //TODO: сомнительное решение, здоровье лучше только у новых
      wave++;
      waveAccumulator -= GameSettings.WAVE_INTERVAL;
    }
  }

  public void update(double dt) {
    updateWaves(dt);
    spawner.update(dt);

    for (Entity entity : new ArrayList<>(entities)) {
      if (entity.isAlive()) entity.update(dt);
    }

    collision.processCollision(entities);

    entities.removeIf(entity -> !entity.isAlive());
  }
}
