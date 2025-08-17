package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.GameModel;
import model.entity.*;
import util.BonusType;
import util.GameSettings;
import util.Vector2D;

public class GameView {
  private final Pane root = new Pane();
  private final Canvas canvas = new Canvas(
          GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
  private final GraphicsContext gc = canvas.getGraphicsContext2D();
  private final GameModel model;

  public GameView(GameModel model) {
    this.model = model;
    root.getChildren().add(canvas);
  }

  public Pane getRoot() { return root; }

  public void render() {
    // фон
    gc.setFill(Color.BLACK);
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

    // все сущности
    for (Entity e : model.getEntities()) {
      if (e instanceof Unit)   gc.setFill(Color.GREEN);
      if (e instanceof Enemy)  gc.setFill(Color.RED);
      if (e instanceof Bullet) gc.setFill(Color.YELLOW);
      if (e instanceof Bonus b && b.type == BonusType.ADD_UNIT)  gc.setFill(Color.BLUE);
      if (e instanceof Bonus b && b.type == BonusType.INCREASE_FIRE_RATE)  gc.setFill(Color.PURPLE);
      if (e instanceof Bonus b && b.type == BonusType.INCREASE_BULLET_DAMAGE)  gc.setFill(Color.YELLOW);
      if (e instanceof Bonus b && b.type == BonusType.HEALING_BONUS)  gc.setFill(Color.WHITE);
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
    gc.fillText("Units: " +
            model.getEntities().stream().filter(e->e instanceof Unit).count(), 10,20);
  }
}
