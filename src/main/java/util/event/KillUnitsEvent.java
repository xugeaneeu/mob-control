package util.event;

public class KillUnitsEvent implements GameEvent{
  public int amount;

  public KillUnitsEvent(int amount) {
    this.amount = amount;
  }
}
