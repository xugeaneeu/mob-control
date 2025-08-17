package controller.state;

import util.event.EventBus;

public class MenuState implements IState{
  private final EventBus eventBus;

  public MenuState(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void onEnter() {}

  @Override
  public void onExit() {}

  @Override
  public void update(double dt) {}
}
