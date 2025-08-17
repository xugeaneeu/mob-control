package controller;

import controller.state.GameState;
import controller.state.StateMachine;
import controller.state.StateType;
import controller.state.StatisticState;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import model.GameModel;
import util.GameStatistic;
import util.event.EventBus;
import util.event.state.StartGameEvent;
import view.GameView;

public class GameController {
  private final GameView view;
  private final EventBus eventBus;
  private final StateMachine stateMachine;
  private boolean left, right;

  public GameController() {
    eventBus = new EventBus();
    stateMachine = new StateMachine(eventBus);
    view = new GameView(eventBus);
  }

  public GameView getView() {return view;}

  public void initInputHandlers(Scene scene) {
    scene.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.LEFT) {left = true;}
      if (e.getCode() == KeyCode.RIGHT) {right = true;}
      if (e.getCode() == KeyCode.SPACE && stateMachine.getCurrentType() == StateType.MENU) {
        eventBus.publish(new StartGameEvent());
      }
    });

    scene.setOnKeyReleased(e -> {
      if (e.getCode() == KeyCode.LEFT) {left = false;}
      if (e.getCode() == KeyCode.RIGHT) {right = false;}
    });
  }

  public void startGameLoop() {
    new AnimationTimer() {
      private long prevTime = System.nanoTime();

      @Override
      public void handle(long now) {
        double dt = (now - prevTime) / 1e9;
        prevTime = now;

        if (stateMachine.getCurrentType() == StateType.GAME) {
          GameState gameState = (GameState) stateMachine.getCurrentState();
          gameState.handleInput(left, right);
        }

        stateMachine.update(dt);

        StateType st = stateMachine.getCurrentType();
        GameModel modelForRender = null;
        GameStatistic statsForRender = null;

        if (st == StateType.GAME) {
          modelForRender = ((GameState) stateMachine.getCurrentState()).getModel();
        } else if (st == StateType.STATISTICS) {
          statsForRender =
                  ((StatisticState) stateMachine.getCurrentState()).getStats();
        }

        view.render(st, modelForRender, statsForRender);
      }
    }.start();
  }

}
