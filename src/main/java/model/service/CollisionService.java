package model.service;

import model.entity.*;
import util.BonusType;
import util.Vector2D;
import util.event.EventBus;
import util.event.RelocateEvent;

import java.util.List;

public class CollisionService {
  private final EventBus eventBus;

  public CollisionService(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void processCollision(List<Entity> entities) {
    int amountOfEntities = entities.size();

    for (int i = 0; i < amountOfEntities; i++) {
      Entity e1 = entities.get(i);
      if (!e1.isAlive()) continue;

      for (int j = i+1; j < amountOfEntities; j++) {
        Entity e2 = entities.get(j);
        if (!e2.isAlive() || !collide(e1,e2)) continue;

        //Bullet vs Enemy
        if (e1 instanceof Bullet bl && e2 instanceof Enemy en) {
          bl.toDestroy();
          en.toDestroy();
          continue;
        }
        if (e2 instanceof Bullet bl && e1 instanceof Enemy en) {
          bl.toDestroy();
          en.toDestroy();
          continue;
        }

        //Unit vs Enemy
        if (e1 instanceof Unit un && e2 instanceof Enemy en ) {
          un.toDestroy();
          en.toDestroy();
          eventBus.publish(new RelocateEvent());
          continue;
        }
        if (e2 instanceof Unit un && e1 instanceof Enemy en ) {
          un.toDestroy();
          en.toDestroy();
          eventBus.publish(new RelocateEvent());
          continue;
        }

        //Bullet vs Bonus
        if (e1 instanceof Bullet bl && e2 instanceof Bonus bn) {
          bn.incrementCounter(bl.getDamage());
          e1.toDestroy();
          continue;
        }
        if (e2 instanceof Bullet bl && e1 instanceof Bonus bn) {
          bn.incrementCounter(bl.getDamage());
          e2.toDestroy();
          continue;
        }

        //Unit vs Bonus
        if (e1 instanceof Unit un && e2 instanceof Bonus bn) {
          bn.applyBonus(un);
          continue;
        }
        if (e2 instanceof Unit un && e1 instanceof Bonus bn) {
          bn.applyBonus(un);
          continue;
        }

        //Bonus vs SpikeWall
        if (e1 instanceof Bonus bn && e2 instanceof SpikeWall) {
          processBonusPop(entities, bn);
          bn.toDestroy();
          continue;
        }
        if (e2 instanceof Bonus bn && e1 instanceof SpikeWall) {
          processBonusPop(entities, bn);
          bn.toDestroy();
          continue;
        }

        //Unit vs chainsaw
        if (e1 instanceof Unit un && e2 instanceof Chainsaw) {
          un.toDestroy();
          eventBus.publish(new RelocateEvent());
          continue;
        }
        if (e2 instanceof Unit un && e1 instanceof Chainsaw) {
          un.toDestroy();
          eventBus.publish(new RelocateEvent());
        }
      }
    }
  }

  private void processBonusPop(List<Entity> entities, Bonus bn) {
    Entity e = new Bonus(bn.getPosition(), Vector2D.zeroVector(), eventBus, BonusType.ATTACK_BONUS);
    for (Entity entity : entities) {
      if (entity.isAlive() && entity instanceof Unit un && collide(e,un)) {
        un.toDestroy();
        eventBus.publish(new RelocateEvent());
      }
    }
    e.toDestroy();
  }

  private boolean collide(Entity e1, Entity e2) {
    double dx = e1.getX() - e2.getX();
    double dy = e1.getY() - e2.getY();
    double totalRadius = e1.getRadius() + e2.getRadius();
    return (dx*dx + dy*dy) <= totalRadius * totalRadius;
  }
}
