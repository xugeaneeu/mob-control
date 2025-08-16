package util;

public class GameSettings {
  public static final int WORLD_WIDTH = 800;
  public static final int WORLD_HEIGHT = 800;

  public static double SHOOT_INTERVAL = 0.2;
  public static final int UNIT_SPEED = 300;
  public static int UNIT_RADIUS = 10; //TODO: set radius
  public static int INIT_UNITS = 1;
  public static final Vector2D UNIT_START_VECTOR = new Vector2D(((double)GameSettings.WORLD_WIDTH/4)*3,
                                                                ((double)GameSettings.WORLD_HEIGHT/5)*4);
  public static final int MAX_UNITS_COLUMN = 6;

  public static int ENEMY_RADIUS = 10; //TODO: set radius
  public static int ENEMY_SPEED = 30; //TODO: set speed

  public static int BULLET_RADIUS = 10; //TODO: set radius
  public static int BULLET_SPEED = 400; //TODO: set speed
  public static int BULLET_DAMAGE = 1; //TODO: set damage

  public static int BONUS_RADIUS = 40; //TODO: set radius
  public static int BONUS_SPEED = 50; //TODO: set speed
  public static int BONUS_SPAWN_INTERVAL = 5; //TODO: set interval
  public static final Vector2D BONUS_START_VECTOR = new Vector2D(GameSettings.BONUS_RADIUS,
                                                                 GameSettings.BONUS_RADIUS);
  public static int BONUS_HEALTH = 15;
  public static final int BONUS_MAX_UNITS = 3;
  public static int BONUS_START_COUNTER = -5;
}
