package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import model.GameModel;
import model.entity.*;
import util.BonusType;
import util.GameSettings;
import util.Vector2D;

public class PlayView {
  private final Pane root;
  private final Canvas canvas;
  private final GraphicsContext gc;

  private final AssetManager assetManager;
  private final Image background;
  private final Image castleImage;
  private final Image bulletImage;

  public PlayView() {
    root = new Pane();
    canvas = new Canvas(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    assetManager = new AssetManager();
    assetManager.preloadTexture();
    background = assetManager.getTexture("background");
    castleImage = assetManager.getTexture("castle");
    bulletImage = assetManager.getTexture("bullet");
  }

  public Pane getRoot() {
    return root;
  }

  public void render(GameModel model) {
    //фон
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());

    for (Entity e : model.getEntities()) {
      if (e instanceof Castle) {
        gc.drawImage(castleImage,
                    (double) GameSettings.WORLD_WIDTH/2, GameSettings.WORLD_HEIGHT - castleImage.getHeight());
        continue;
      } else if (e instanceof Bullet) {
        Vector2D pos = e.getPosition();
        gc.drawImage(bulletImage,
                     pos.x(), pos.y());
        continue;
      }

      if (e instanceof Unit)   gc.setFill(Color.GREEN);
      else if (e instanceof Enemy)  gc.setFill(Color.RED);
      else if (e instanceof Bonus b) {
        double maxHealth = 0, currentHealth;
        currentHealth = b.getHealth();
        if (b.type == BonusType.ATTACK_BONUS) {continue;}
        Color bonusColor = null;
        switch (b.type) {
          case ADD_UNIT             -> {
            bonusColor = Color.BLUE;
            maxHealth = -GameSettings.BONUS_START_COUNTER + GameSettings.BONUS_MAX_UNITS;
          }
          case INCREASE_FIRE_RATE   -> {
            bonusColor = Color.PURPLE;
            maxHealth = GameSettings.FIRE_RATE_BONUS_HEALTH;
          }
          case INCREASE_BULLET_DAMAGE -> {
            bonusColor = Color.ORANGE;
            maxHealth = GameSettings.DAMAGE_BONUS_HEALTH;
          }
          case HEALING_BONUS        -> {
            bonusColor = Color.WHITE;
            maxHealth = GameSettings.HEALING_BONUS_HEALTH;
          }
        }

        Vector2D pos = e.getPosition();
        drawHealthBar(gc,
                pos.x() - GameSettings.BAR_WIDTH/2,
                pos.y() - e.getRadius() - GameSettings.BAR_HEIGHT - 2,
                currentHealth / maxHealth
        );
        gc.setFill(bonusColor);
      } else { continue;}

      Vector2D p = e.getPosition();
      double r = e.getRadius();
      gc.fillOval(p.x() - r, p.y() - r, r*2, r*2);
    }

    // HUD (скоро будет в HUDView)
    gc.setFill(Color.WHITE);
    gc.fillText("Units: " + model.countUnits(), 10,20);
  }

  private void drawHealthBar(GraphicsContext gc, double x, double y, double ratio){
    ratio = Math.max(0, Math.min(1, ratio));

    gc.setStroke(Color.BLACK);
    gc.strokeRect(x, y, GameSettings.BAR_WIDTH, GameSettings.BAR_HEIGHT);

    gc.setFill(Color.rgb(50, 50, 50, 0.5));
    gc.fillRect(x, y, GameSettings.BAR_WIDTH, GameSettings.BAR_HEIGHT);

    LinearGradient grad = new LinearGradient(
            x, y,
            x + GameSettings.BAR_WIDTH, y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.RED),
            new Stop(1, Color.LIME)
    );
    gc.setFill(grad);

    gc.fillRect(x, y, GameSettings.BAR_WIDTH * ratio, GameSettings.BAR_HEIGHT);
  }
}
