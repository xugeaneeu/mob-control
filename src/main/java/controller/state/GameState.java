package controller.state;

import model.GameModel;
import model.entity.Entity;
import model.entity.Unit;
import util.Direction;
import util.event.EventBus;

public class GameState implements IState{
  private final EventBus eventBus;
  private GameModel model;
  private boolean left, right;

  public GameState(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void handleInput(boolean left, boolean right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public void onEnter() {
    model = new GameModel(eventBus);
  }

  @Override
  public void onExit() {
    model = null;
  }

  @Override
  public void update(double dt) {
    Direction dir = left
            ? Direction.LEFT
            : right
            ? Direction.RIGHT
            : Direction.NONE;

    for (Entity e : model.getEntities()) {
      if (e instanceof Unit u) {
        u.setDirection(dir);
      }
    }

    model.update(dt);
  }

  public GameModel getModel() {
    return model;
  }
}
