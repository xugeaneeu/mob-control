package app;

import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    GameController controller = new GameController();
    Scene scene = new Scene(controller.getView().getRoot());
    controller.initInputHandlers(scene);
    primaryStage.setScene(scene);
    primaryStage.show();
    controller.startGameLoop();
  }

  public static void main(String[] args) {
    launch();
  }
}
