package util;

public class GameSettings {
  public static final int WORLD_WIDTH = 800;
  public static final int WORLD_HEIGHT = 800;

  public static double SHOOT_INTERVAL = 0.2;
  public static final int UNIT_SPEED = 300;
  public static int UNIT_RADIUS = 10;
  public static int UNIT_HEALTH = 1;
  public static int INIT_UNITS = 2;
  public static final Vector2D UNIT_START_VECTOR = new Vector2D(((double)GameSettings.WORLD_WIDTH/4)*3,
                                                                ((double)GameSettings.WORLD_HEIGHT/5)*4);
  public static final int MAX_UNITS_COLUMN = 6;
  public static final double RELOCATION_TIME = 3.0;

  public static final double WAVE_INTERVAL = 20.0;
  public static final int ENEMY_RADIUS = 10;
  public static final int ENEMY_SPEED = 20;
  public static int ENEMY_HEALTH = 1;

  public static final int BULLET_RADIUS = 5;
  public static int BULLET_SPEED = 400;
  public static int BULLET_DAMAGE = 1;

  public static final int BONUS_RADIUS = 40;
  public static final int BONUS_ATTACK_RADIUS = 100;
  public static final int BONUS_SPEED = 50;
  public static final int BONUS_SPAWN_INTERVAL = 5;
  public static final Vector2D BONUS_START_VECTOR = new Vector2D(GameSettings.BONUS_RADIUS,
                                                                 GameSettings.BONUS_RADIUS);
  public static int FIRE_RATE_BONUS_HEALTH = 15;
  public static int DAMAGE_BONUS_HEALTH = 20;
  public static int HEALING_BONUS_HEALTH = 20;
  public static final int BONUS_MAX_UNITS = 3;
  public static int BONUS_START_COUNTER = -5;

  public static final int SPIKE_LENGTH = 15;
  public static final Vector2D SPIKE_START_VECTOR = new Vector2D(0.0,
                                                                 GameSettings.WORLD_HEIGHT);
  public static final int AMOUNT_OF_SPIKES = WORLD_WIDTH / (2*SPIKE_LENGTH);

  public static final int CHAINSAW_LENGTH = 20;

  public static final int CASTLE_LENGTH = 20;
  public static final int CASTLE_WIDTH = GameSettings.WORLD_WIDTH/2;
  public static final int TOWER_WIDTH = 96;
  public static final int TOWER_HEIGHT = 100;
  public static final int BETWEEN_TOWER_WIDTH = 205;
  public static final int BETWEEN_TOWER_HEIGHT = 50;
  public static final int CASTLE_HEALTH = 15;

  public static final int BUILDINGS_HEALTH = 1;

  public static final double BAR_WIDTH  = 70;
  public static final double BAR_HEIGHT = 5;


  static public void toDefaults() {
    double SHOOT_INTERVAL = 0.2;
    int UNIT_RADIUS = 10;
    int UNIT_HEALTH = 1;
    int INIT_UNITS = 2;

    int ENEMY_HEALTH = 1;

    int BULLET_SPEED = 400;
    int BULLET_DAMAGE = 1;

    int FIRE_RATE_BONUS_HEALTH = 15;
    int DAMAGE_BONUS_HEALTH = 20;
    int HEALING_BONUS_HEALTH = 5;
    int BONUS_START_COUNTER = -5;
  }
}
