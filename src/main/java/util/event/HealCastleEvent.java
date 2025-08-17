package util.event;

public class HealCastleEvent implements GameEvent {
  public final int amountHP;

  public HealCastleEvent(int amountHP) {
    this.amountHP = amountHP;
  }
}
