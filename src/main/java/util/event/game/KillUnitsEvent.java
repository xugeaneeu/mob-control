package util.event.game;

import util.event.GameEvent;

public class KillUnitsEvent implements GameEvent {
  public int amount;

  public KillUnitsEvent(int amount) {
    this.amount = amount;
  }
}
