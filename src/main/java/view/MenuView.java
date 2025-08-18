package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
  private static final Path HS_FILE = Path.of("highscore.txt");
  private final BorderPane root = new BorderPane();
  private final Label highScoreLabel = new Label();
  private long best = readHighScoreFromFile();

  public MenuView(EventBus bus) {
    root.setPrefSize(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
    root.setStyle("-fx-background-color: white;");

    highScoreLabel.setText("High Score: " + best);
    highScoreLabel.setTextFill(Color.GOLD);
    highScoreLabel.setFont(Font.font("Verdana", 35));

    BorderPane.setMargin(highScoreLabel, new Insets(50, 0, 0, 0));
    BorderPane.setAlignment(highScoreLabel, Pos.TOP_CENTER);
    root.setTop(highScoreLabel);

    Text title = new Text("MOB CONTROL");
    title.setFill(Color.BLACK);
    title.setFont(Font.font("Verdana", 50));
    StackPane centerHolder = new StackPane(title);
    centerHolder.setAlignment(Pos.CENTER);
    root.setCenter(centerHolder);

    Button btnStart = new Button("START GAME");
    btnStart.setFont(Font.font("Verdana", 24));
    btnStart.setOnAction(_ -> bus.publish(new StartGameEvent()));
    btnStart.setPrefWidth(300);
    StackPane bottomHolder = new StackPane(btnStart);
    bottomHolder.setPadding(new Insets(0, 0, 50, 0));
    bottomHolder.setAlignment(Pos.BOTTOM_CENTER);
    root.setBottom(bottomHolder);

    bus.addSubscriber(evt -> {
      if (evt instanceof GameOverEvent goe) {
        long newScore = goe.getStats().enemyScore();
        if (newScore > best) {
          best = newScore;
          highScoreLabel.setText("High Score: " + best);
          writeHighScoreToFile(best);
        }
      }
    });
  }

  public BorderPane getRoot() {
    return root;
  }

  private long readHighScoreFromFile() {
    try {
      if (Files.exists(HS_FILE)) {
        String s = Files.readString(HS_FILE).trim();
        return Long.parseLong(s);
      }
    } catch (IOException|NumberFormatException ignored) { }
    return 0L;
  }

  private void writeHighScoreToFile(long score) {
    try {
      Files.writeString(HS_FILE, Long.toString(score));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}