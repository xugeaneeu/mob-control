package util.event.game;

import util.event.GameEvent;

public class AddUnitsEvent implements GameEvent {
  public final int amountOfUnits;

  public AddUnitsEvent(int amountOfUnits) {
    this.amountOfUnits = amountOfUnits;
  }
}
