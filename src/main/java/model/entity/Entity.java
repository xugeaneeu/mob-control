package model.entity;

import util.Vector2D;
import util.event.EventBus;

public abstract class Entity {
  protected final EventBus eventBus;
  protected Vector2D position;
  protected Vector2D velocity = Vector2D.zeroVector();
  protected double radius;
  protected int health;
  protected boolean alive = true;

  public Entity(Vector2D startPos, double radius, int health, EventBus eventBus) {
    this.position = startPos;
    this.radius   = radius;
    this.eventBus = eventBus;
    this.health   = health;
  }

  public final void update(double dt) {
    onUpdate(dt);
    position = position.add(velocity.mul(dt));
  }

  protected abstract void onUpdate(double dt);
  public abstract void decreaseHealth(int damage);

  public Vector2D getPosition() { return position; }
  public double   getX()        { return position.x(); }
  public double   getY()        { return position.y(); }
  public double   getRadius()   { return radius; }
  public boolean  isAlive()     { return alive; }
  public int      getHealth()   { return health; }

  public void toDestroy() {
    alive = false;
  }
}
