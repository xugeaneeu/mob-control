package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.GameSettings;
import util.event.EventBus;
import util.event.state.GameOverEvent;
import util.event.state.StartGameEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MenuView {
  private static final Path HSFile = Path.of("highscore.txt");
  private final VBox root;
  private final Label highScoreLabel = new Label();

  private long best = readHighScoreFromFile();

  public MenuView(EventBus eventBus) {
    root = new VBox();
    root.setPrefSize(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-background-color: white;");

    Text title = new Text("MOB CONTROL");
    title.setFill(Color.BLACK);
    title.setFont(Font.font("Verdana", 36));

    highScoreLabel.setTextFill(Color.GOLD);
    highScoreLabel.setFont(Font.font("Verdana", 30));
    highScoreLabel.setText("High Score: " + best);


    Button btnStartGame = new Button("Start Game");
    btnStartGame.setFont(Font.font("Verdana", 20));
    btnStartGame.setOnAction(_ -> eventBus.publish(new StartGameEvent()));

    root.getChildren().addAll(title, highScoreLabel, btnStartGame);

    eventBus.addSubscriber(event -> {
      if (event instanceof GameOverEvent goe) {
        long newScore = goe.getStats().enemyScore();
        if (newScore > best) {
          best = newScore;
          highScoreLabel.setText("High score: " + best);
          writeHighScoreToFile(best);
        }
      }
    });
  }

  public VBox getRoot() {
    return root;
  }

  private long readHighScoreFromFile() {
    try {
      if (Files.exists(HSFile)) {
        String s = Files.readString(HSFile).trim();
        return Long.parseLong(s);
      }
    } catch (IOException | NumberFormatException ignored) {}
    return 0L;
  }

  private void writeHighScoreToFile(long score) {
    try {
      Files.writeString(HSFile, Long.toString(score));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
