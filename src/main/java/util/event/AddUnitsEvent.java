package util.event;

public class AddUnitsEvent implements GameEvent {
  public final int amountOfUnits;

  public AddUnitsEvent(int amountOfUnits) {
    this.amountOfUnits = amountOfUnits;
  }
}
