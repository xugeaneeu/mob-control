package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.GameSettings;
import util.event.EventBus;
import util.event.state.StartGameEvent;

public class MenuView {
  private final VBox root;

  public MenuView(EventBus eventBus) {
    root = new VBox();
    root.setPrefSize(GameSettings.WORLD_WIDTH, GameSettings.WORLD_HEIGHT);
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-background-color: white;"); // TODO: change to some gif with inf effect

    Text title = new Text("MOB CONTROL");
    Text menu = new Text("menu");
    title.setFill(Color.BLACK);
    title.setFont(Font.font(36));
    menu.setFill(Color.BLACK);
    menu.setFont(Font.font(20));

    Button btnStartGame = new Button("Start Game");
    btnStartGame.setPrefSize(100, 50);
    btnStartGame.setOnAction(_ -> eventBus.publish(new StartGameEvent()));

    root.getChildren().addAll(title, menu, btnStartGame);
  }

  public VBox getRoot() {
    return root;
  }
}
