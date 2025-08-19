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
  private final Image unitSheet;
  private final Image enemySheet;

  private static final int   UNIT_FRAME_COUNT      = 8;
  private static final double UNIT_FRAME_DURATION  = 0.1;
  private static final int   ENEMY_FRAME_COUNT     = 8;
  private static final double ENEMY_FRAME_DURATION = 0.1;

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
    unitSheet = assetManager.getTexture("unit_sheets");
    enemySheet = assetManager.getTexture("enemy_sheets");
  }

  public Pane getRoot() {
    return root;
  }

  public void render(GameModel model) {
    double elapsedTime = GameModel.getTime();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    gc.drawImage(background, 0, 0, canvas.getWidth(), canvas.getHeight());

    for (Entity e : model.getEntities()) {
      Vector2D pos = e.getPosition();
      switch (e) {
        case Castle _ -> gc.drawImage(castleImage,
                (double) GameSettings.WORLD_WIDTH / 2, GameSettings.WORLD_HEIGHT - castleImage.getHeight());
        case Bullet _ -> gc.drawImage(bulletImage,
                pos.x(), pos.y());
        case Unit _ -> drawSpriteSheetFrame(gc,
                unitSheet,
                UNIT_FRAME_COUNT,
                UNIT_FRAME_DURATION,
                elapsedTime,
                pos);
        case Enemy _ -> {
          if (inCastle(pos)) continue;

          drawSpriteSheetFrame(gc,
                  enemySheet,
                  ENEMY_FRAME_COUNT,
                  ENEMY_FRAME_DURATION,
                  elapsedTime,
                  pos);
        }
        case Bonus b -> {
          double maxHealth = 0, currentHealth;
          currentHealth = b.getHealth();
          if (b.type == BonusType.ATTACK_BONUS) {
            continue;
          }
          Color bonusColor = null;
          switch (b.type) {
            case ADD_UNIT -> {
              bonusColor = Color.BLUE;
              maxHealth = -GameSettings.BONUS_START_COUNTER + GameSettings.BONUS_MAX_UNITS;
              currentHealth = b.getHealth() - GameSettings.BONUS_START_COUNTER;
            }
            case INCREASE_FIRE_RATE -> {
              bonusColor = Color.PURPLE;
              maxHealth = GameSettings.FIRE_RATE_BONUS_HEALTH;
            }
            case INCREASE_BULLET_DAMAGE -> {
              bonusColor = Color.ORANGE;
              maxHealth = GameSettings.DAMAGE_BONUS_HEALTH;
            }
            case HEALING_BONUS -> {
              bonusColor = Color.WHITE;
              maxHealth = GameSettings.HEALING_BONUS_HEALTH;
            }
          }

          drawHealthBar(gc,
                  pos.x() - GameSettings.BAR_WIDTH / 2,
                  pos.y() - e.getRadius() - GameSettings.BAR_HEIGHT - 2,
                  currentHealth / maxHealth
          );
          gc.setFill(bonusColor);
          Vector2D p = e.getPosition();
          double r = e.getRadius();
          gc.fillOval(p.x() - r, p.y() - r, r * 2, r * 2);
        }
        default -> {
        }
      }
    }

    // HUD (скоро будет в HUDView)
    gc.setFill(Color.WHITE);
    gc.fillText("Units: " + model.countUnits(), 10,20);
  }

  private static boolean inCastle(Vector2D pos) {
    double castleStartX = (double) GameSettings.WORLD_WIDTH / 2;

    double leftTowerEnd = castleStartX + GameSettings.TOWER_WIDTH + GameSettings.UNIT_RADIUS;
    double rightTowerStart = castleStartX + GameSettings.TOWER_WIDTH + GameSettings.BETWEEN_TOWER_WIDTH
                             - GameSettings.UNIT_RADIUS;

    if ((pos.x() <= leftTowerEnd) || (pos.x() >= rightTowerStart)) {
      if (pos.y() >= GameSettings.WORLD_HEIGHT - GameSettings.TOWER_HEIGHT) {
        return true;
      }
    }

    if ((pos.x() >= leftTowerEnd) || (pos.x() <= rightTowerStart)) {
      return pos.y() >= GameSettings.WORLD_HEIGHT - GameSettings.BETWEEN_TOWER_HEIGHT;
    }
    return false;
  }

  private void drawSpriteSheetFrame(GraphicsContext gc,
                                    Image sheet,
                                    int frameCount,
                                    double frameDur,
                                    double elapsed,
                                    Vector2D pos)
  {
    double sheetW = sheet.getWidth();
    double sheetH = sheet.getHeight();
    double fw = sheetW  / frameCount;
    double fh = sheetH;

    int idx = (int)(elapsed / frameDur) % frameCount;

    gc.drawImage(sheet,
            idx * fw, 0,
            fw, fh,
            pos.x() - fw/2,
            pos.y() - fh/2,
            fw, fh);
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
