package controller.state;

import util.GameStatistic;
import util.event.EventBus;

public class StatisticState implements IState{
  private final EventBus eventBus;
  private GameStatistic stats;

  public StatisticState(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void setStats(GameStatistic stats) {
    this.stats = stats;
  }

  public GameStatistic getStats() {
    return stats;
  }

  @Override
  public void onEnter() {}

  @Override
  public void onExit() {
    stats = null;
  }

  @Override
  public void update(double dt) {}
}
