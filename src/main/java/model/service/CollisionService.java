package model.service;

import model.entity.*;
import util.BonusType;
import util.GameSettings;
import util.GameStatistic;
import util.Vector2D;
import util.event.EventBus;
import util.event.state.GameOverEvent;
import util.event.game.RelocateEvent;

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

        if (e2 instanceof Castle || e1 instanceof Castle) {
          if (!e2.isAlive() || !collideCircleRectangle(e1,e2)) continue;

          if (e1 instanceof Castle cl && e2 instanceof Enemy en) {
            processHealthIssue(e1, e2);
            if (!cl.isAlive()) eventBus.publish(new GameOverEvent(new GameStatistic()));
            continue;
          }
          if (e2 instanceof Castle cl && e1 instanceof Enemy) {
            processHealthIssue(e1, e2);
            if (!cl.isAlive()) eventBus.publish(new GameOverEvent(new GameStatistic()));
          }

          continue;
        }

        if (!e2.isAlive() || !collideCircles(e1,e2)) continue;

        //Bullet vs Enemy
        if (e1 instanceof Bullet bl && e2 instanceof Enemy en) {
          processHealthIssue(bl, en);
          continue;
        }
        if (e2 instanceof Bullet bl && e1 instanceof Enemy en) {
          processHealthIssue(bl, en);
          continue;
        }

        //Unit vs Enemy
        if (e1 instanceof Unit un && e2 instanceof Enemy en ) {
          processHealthIssue(un, en);
          if (!un.isAlive()) eventBus.publish(new RelocateEvent());
          continue;
        }
        if (e2 instanceof Unit un && e1 instanceof Enemy en ) {
          processHealthIssue(un, en);
          if (!un.isAlive()) eventBus.publish(new RelocateEvent());
          continue;
        }

        //Bullet vs Bonus
        if (e1 instanceof Bullet bl && e2 instanceof Bonus bn) {
          bn.decreaseHealth(bl.getHealth());
          bl.toDestroy();
          continue;
        }
        if (e2 instanceof Bullet bl && e1 instanceof Bonus bn) {
          bn.decreaseHealth(bl.getHealth());
          bl.toDestroy();
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

        //Unit vs Chainsaw
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

  private static void processHealthIssue(Entity e1, Entity e2) {
    if (e1.getHealth() >= e2.getHealth()) {
      e1.decreaseHealth(e2.getHealth());
      if (e1.getHealth() == 0) e1.toDestroy();
      e2.toDestroy();
    } else {
      e2.decreaseHealth(e1.getHealth());
      e1.toDestroy();
    }
  }

  private void processBonusPop(List<Entity> entities, Bonus bn) {
    Entity e = new Bonus(bn.getPosition(), Vector2D.zeroVector(), eventBus, BonusType.ATTACK_BONUS);
    for (Entity entity : entities) {
      if (entity.isAlive() && entity instanceof Unit un && collideCircles(e,un)) {
        un.toDestroy();
        eventBus.publish(new RelocateEvent());
      }
    }
    e.toDestroy();
  }

  private boolean collideCircles(Entity e1, Entity e2) {
    double dx = e1.getX() - e2.getX();
    double dy = e1.getY() - e2.getY();
    double totalRadius = e1.getRadius() + e2.getRadius();
    return (dx*dx + dy*dy) <= totalRadius * totalRadius;
  }

  private boolean collideCircleRectangle(Entity e1, Entity e2) {
    Castle cl = (e1 instanceof Castle) ? (Castle) e1 : (Castle) e2;
    Entity en  = !(e1 instanceof Castle) ? e1 : e2;

    double closestX = clamp(en.getX(), cl.getX(), cl.getX() + GameSettings.CASTLE_WIDTH);
    double closestY = clamp(en.getY(), cl.getY(), cl.getY() + GameSettings.CASTLE_WIDTH);

    Vector2D line = new Vector2D(en.getX() - closestX, en.getY() - closestY);
    return line.x() * line.x() + line.y() * line.y() <= cl.getRadius() * cl.getRadius();
  }

  private double clamp(double value, double min, double max) {
    if (value < min) return min;
    return Math.min(value, max);
  }
}
