package model;

import model.entity.Bullet;
import model.entity.Entity;
import model.entity.Unit;
import model.service.CollisionService;
import model.service.SpawnerService;
import util.GameSettings;
import util.event.AddUnitsEvent;
import util.event.EventBus;
import util.event.IncreaseFireRateEvent;
import util.event.ShootEvent;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
  private final List<Entity> entities = new ArrayList<>();
  private final EventBus eventBus = new EventBus();
  SpawnerService spawner = new SpawnerService();
  CollisionService collision = new CollisionService();

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
        spawner.spawnUnits(this, auEvent.amountOfUnits);
      }
    });

    spawner.initSpawn(this);
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

  public void update(double dt) {
//    System.out.println(dt);
    spawner.update(dt, this);

    for (Entity entity : new ArrayList<>(entities)) {
      if (entity.isAlive()) entity.update(dt);
    }

    collision.processCollision(entities);

    entities.removeIf(entity -> !entity.isAlive());
  }
}
