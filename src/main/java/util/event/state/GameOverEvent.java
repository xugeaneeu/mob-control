package util.event.state;

import util.GameStatistic;
import util.event.GameEvent;

public class GameOverEvent implements GameEvent {
  private final GameStatistic stats;

  public GameOverEvent(GameStatistic stats) {
    this.stats = stats;
  }

  public GameStatistic getStats() {
    return stats;
  }
}
