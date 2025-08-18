package util.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus {
  private final List<Consumer<GameEvent>> subscribers = new CopyOnWriteArrayList<>();

  public void addSubscriber(Consumer<GameEvent> subscriber) {
    subscribers.add(subscriber);
  }

  public void publish(GameEvent event) {
    for (var subscriber : subscribers) {
      subscriber.accept(event);
    }
  }
}
