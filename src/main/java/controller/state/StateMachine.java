package controller.state;

import util.event.EventBus;
import util.event.state.BackToMenuEvent;
import util.event.state.GameOverEvent;
import util.event.state.StartGameEvent;

import java.util.EnumMap;
import java.util.Map;

public class StateMachine {
  private final EventBus eventBus;
  private final Map<StateType, IState> states = new EnumMap<>(StateType.class);
  private IState currentState;
  private StateType currentStateType;

  public StateMachine(EventBus eventBus) {
    this.eventBus = eventBus;

    states.put(StateType.MENU, new MenuState(eventBus));
    states.put(StateType.GAME, new GameState(eventBus));
    states.put(StateType.STATISTICS, new StatisticState(eventBus));

    eventBus.addSubscriber(event -> {
      if (event instanceof StartGameEvent) {
        switchToState(StateType.GAME);
      } else if (event instanceof BackToMenuEvent) {
        switchToState(StateType.MENU);
      } else if (event instanceof GameOverEvent goe && currentStateType != StateType.STATISTICS) {
        StatisticState statistic = (StatisticState) states.get(StateType.STATISTICS);
        statistic.setStats(goe.getStats());
        switchToState(StateType.STATISTICS);
      }
    });

    switchToState(StateType.MENU);
  }

  private void switchToState(StateType stateType) {
    if (currentState != null) currentState.onExit();

    currentStateType = stateType;
    currentState = states.get(stateType);
    currentState.onEnter();
  }

  public void update(double dt) {
    currentState.update(dt);
  }

  public StateType getCurrentType() {
    return currentStateType;
  }

  public IState getCurrentState() {
    return currentState;
  }
}
