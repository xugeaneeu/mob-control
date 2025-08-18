package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import util.GameSettings;
import util.GameStatistic;
import util.event.EventBus;
import util.event.state.BackToMenuEvent;
import util.event.state.StartGameEvent;

public class StatsView {
  private final VBox root;

  private final Label timeLabel = new Label();
  private final Label enemyScoreLabel = new Label();
  private final Label bonusScoreLabel = new Label();
  private final Label bulletLabel = new Label();

  public StatsView(EventBus eventBus) {
    root = new VBox(15);
    root.setPrefSize(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: white;");

    Label header = new Label("Game Statistics");
    header.setStyle(
            "-fx-text-fill: black;" +
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;"
    );

    Button btnPlayAgain = new Button("Play again");
    btnPlayAgain.setPrefWidth(180);
    btnPlayAgain.setOnAction(_ -> eventBus.publish(new StartGameEvent()));

    Button btnMenu = new Button("Back to menu");
    btnMenu.setPrefWidth(180);
    btnMenu.setOnAction(_ -> eventBus.publish(new BackToMenuEvent()));

    String labelStyle = "-fx-text-fill: black; -fx-font-size: 18px;";
    timeLabel .setStyle(labelStyle);
    enemyScoreLabel.setStyle(labelStyle);
    bonusScoreLabel.setStyle(labelStyle);
    bulletLabel.setStyle(labelStyle);

    root.getChildren().addAll(
            header,
            timeLabel,
            enemyScoreLabel,
            bonusScoreLabel,
            bulletLabel,
            btnPlayAgain,
            btnMenu
    );
  }

  public VBox getRoot() {
    return root;
  }

  public void render(GameStatistic stats) {
    timeLabel.setText("Time played: " + stats.time() + "s");
    enemyScoreLabel.setText("Score played: " + stats.enemyScore());
    bonusScoreLabel.setText("Bonus amount: " + stats.bonusScore());
    bulletLabel.setText("Bullet level: " + stats.bulletLevel());
  }
}
