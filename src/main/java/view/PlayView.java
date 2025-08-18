package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.GameModel;
import model.entity.*;
import util.GameSettings;
import util.Vector2D;

public class PlayView {
  private final Pane root;
  private final Canvas canvas;
  private final GraphicsContext gc;

  public PlayView() {
    root = new Pane();
    canvas = new Canvas(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);
  }

  public Pane getRoot() {
    return root;
  }

  public void render(GameModel model) {
    //фон
    gc.setFill(Color.BLACK);
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

    // все сущности
    for (Entity e : model.getEntities()) {
      if (e instanceof Unit)   gc.setFill(Color.GREEN);
      if (e instanceof Enemy)  gc.setFill(Color.RED);
      if (e instanceof Bullet) gc.setFill(Color.YELLOW);
      if (e instanceof Bonus b) {
        switch (b.type) {
          case ADD_UNIT             -> gc.setFill(Color.BLUE);
          case INCREASE_FIRE_RATE   -> gc.setFill(Color.PURPLE);
          case INCREASE_BULLET_DAMAGE -> gc.setFill(Color.ORANGE);
          case HEALING_BONUS        -> gc.setFill(Color.WHITE);
        }
      }
      if (e instanceof Castle) {
        gc.setFill(Color.BLUE);
        Vector2D pos = e.getPosition();
        gc.fillRect(pos.x(), pos.y(), GameSettings.CASTLE_WIDTH, GameSettings.CASTLE_LENGTH);
        continue;
      }

      Vector2D p = e.getPosition();
      double r = e.getRadius();
      gc.fillOval(p.x() - r, p.y() - r, r*2, r*2);
    }

    // HUD (скоро будет в HUDView)
    gc.setFill(Color.WHITE);
    gc.fillText("Units: " + model.countUnits(), 10,20);
  }
}
