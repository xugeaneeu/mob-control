package model.service;

import model.GameModel;
import model.entity.*;
import util.BonusType;
import util.GameSettings;
import util.Vector2D;
import util.event.GameOverEvent;
import util.event.RelocateEvent;

import java.util.Random;

public class SpawnerService {
  private final Random rnd = new Random();
  private final GameModel model;

  public SpawnerService(GameModel model) {
    this.model = model;
    model.getEventBus().addSubscriber(event -> {
      if (event instanceof RelocateEvent) {
        needRelocate = true;
      }
    });
  }

  private final double enemyDiameter = 2 * GameSettings.ENEMY_RADIUS;
  private final double unitDiameter = 2 * GameSettings.UNIT_RADIUS;

  private int curRow, curCol;

  private double enemySpawnAccumulator = 0.0;
  private final double enemySpawnInterval = (double)(2 * GameSettings.ENEMY_RADIUS) / GameSettings.ENEMY_SPEED;

  private double bonusSpawnAccumulator = 0.0;

  private boolean needRelocate = false;
  private double relocationTimer = 0.0;

  public void spawnEnemyLine(GameModel model) {
    int count = (int) (GameSettings.WORLD_WIDTH * 0.5 / enemyDiameter);

    for (int i = 0; i < count; i++) {
      double x = (double) GameSettings.WORLD_WIDTH / 2 + i * enemyDiameter;
      Vector2D position = new Vector2D(x, 0);
      Vector2D velocity = new Vector2D(0, GameSettings.ENEMY_SPEED);
      model.addEntity(new Enemy(position, velocity, model.getEventBus()));
    }
  }

  public void spawnBonus(BonusType type) {
    Vector2D offset = new Vector2D(rnd.nextDouble() * ((double) GameSettings.WORLD_WIDTH/2 - 2*GameSettings.BONUS_RADIUS), 0);
    Vector2D position = GameSettings.BONUS_START_VECTOR.add(offset);
    Vector2D velocity = new Vector2D(0, GameSettings.BONUS_SPEED);
    model.addEntity(new Bonus(position, velocity, model.getEventBus(), type));
  }

  private void setCurRowAndColumn(int aliveUnits) {
    curRow = (int) Math.ceil((double)aliveUnits/GameSettings.MAX_UNITS_COLUMN) - 1;
    curCol = (aliveUnits-1) % GameSettings.MAX_UNITS_COLUMN;
  }

  public void spawnUnits(int amountOfUnits) {
    Entity headUnit = model.getHeadUnit();
    Vector2D headPos = (headUnit == null) ? GameSettings.UNIT_START_VECTOR : headUnit.getPosition();
    int aliveUnits = model.countUnits();

    setCurRowAndColumn(aliveUnits);

    double spawnPosX, spawnPosY;
    for (int i = 0; i < amountOfUnits; i++) {
      if (curCol == (GameSettings.MAX_UNITS_COLUMN-1)) {
        spawnPosX = headPos.x();
        spawnPosY = headPos.y() + (curRow+1) * unitDiameter;
        curCol = 0;
        curRow++;
      } else {
        spawnPosX = headPos.x() + (curCol+1) * unitDiameter;
        spawnPosY = headPos.y() + curRow * unitDiameter;
        curCol++;
      }

      Vector2D newUnitPosition = new Vector2D(spawnPosX, spawnPosY);
      Entity newUnit = new Unit(newUnitPosition, model.getEventBus());
      model.addEntity(newUnit);
    }
  }

  public void initSpawn() {
    int initAmountOfUnits = GameSettings.INIT_UNITS; //TODO: get from game state (specified by level)
    spawnUnits(initAmountOfUnits);
    spawnSpikes();
  }

  private void spawnSpikes() {
    for (int i = 0; i < GameSettings.AMOUNT_OF_SPIKES; i++) {
      Vector2D offset = new Vector2D(i*GameSettings.SPIKE_LENGTH, 0);
      Vector2D pos = GameSettings.SPIKE_START_VECTOR.add(offset);
      Entity spike = new SpikeWall(pos, model.getEventBus());
      model.addEntity(spike);
    }
  }

  private void relocateUnits() {
    int amountOfUnits = model.countUnits();
    if (amountOfUnits == 0) {
      model.getEventBus().publish(new GameOverEvent());
    }
    Vector2D headPos = model.getHeadUnit().getPosition();

    for (Entity e : model.getEntities()) {
      if (e instanceof Unit) e.toDestroy();
    }

    model.addEntity(new Unit(headPos, model.getEventBus()));
    spawnUnits(amountOfUnits-1);
  }

  public void update(double dt) {
    enemySpawnAccumulator += dt;
    if (enemySpawnAccumulator >= enemySpawnInterval) {
      enemySpawnAccumulator -= enemySpawnInterval;
      spawnEnemyLine(model);
    }

    bonusSpawnAccumulator += dt;
    if (bonusSpawnAccumulator >= GameSettings.BONUS_SPAWN_INTERVAL) {
      bonusSpawnAccumulator -= GameSettings.BONUS_SPAWN_INTERVAL;
      BonusType nextBonus = rnd.nextBoolean() ? BonusType.INCREASE_FIRE_RATE : BonusType.ADD_UNIT;
      spawnBonus(nextBonus);
    }

    if (needRelocate) {
      relocationTimer += dt;
      if (relocationTimer >= GameSettings.RELOCATION_TIME) {
        relocateUnits();
        needRelocate = false;
        relocationTimer = 0.0;
      }
    }
  }
}
