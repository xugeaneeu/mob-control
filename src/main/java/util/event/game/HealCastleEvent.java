package util.event.game;

import util.event.GameEvent;

public class HealCastleEvent implements GameEvent {
  public final int amountHP;

  public HealCastleEvent(int amountHP) {
    this.amountHP = amountHP;
  }
}
