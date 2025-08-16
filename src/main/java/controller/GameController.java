package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import model.GameModel;
import model.entity.Unit;
import util.Direction;
import view.GameView;

public class GameController {
  private final GameModel model;
  private final GameView view;
  private boolean left, right;

  public GameController() {
    model = new GameModel();
    view = new GameView(model);
  }

  public void initInputHandlers(Scene scene) {
    scene.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.LEFT) {left = true;}
      if (e.getCode() == KeyCode.RIGHT) {right = true;}
    });

    scene.setOnKeyReleased(e -> {
      if (e.getCode() == KeyCode.LEFT) {left = false;}
      if (e.getCode() == KeyCode.RIGHT) {right = false;}
    });
  }

  public void processInput() {
    Direction dir = left ? Direction.LEFT
            : right ? Direction.RIGHT
            : Direction.NONE;
    model.getEntities().stream()
            .filter(e -> e instanceof Unit)
            .map(e -> (Unit) e)
            .forEach(u -> u.setDirection(dir));
  }

  public void startGameLoop() {
    new AnimationTimer() {
      private long prevTime = System.nanoTime();

      @Override
      public void handle(long now) {
        double dt = (now - prevTime) / 1e9;
        prevTime = now;

        processInput();

        model.update(dt);
        view.render();
      }
    }.start();
  }

  public GameView getView() {return view;}
}
