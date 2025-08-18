package view;

import controller.state.StateType;
import javafx.scene.layout.Pane;
import model.GameModel;
import util.GameStatistic;
import util.event.EventBus;

public class GameView {
  private final Pane root = new Pane();

  private final MenuView menuView;
  private final PlayView playView;
  private final StatsView statsView;

  public GameView(EventBus eventBus) {
    menuView = new MenuView(eventBus);
    playView = new PlayView();
    statsView = new StatsView(eventBus);


    root.getChildren().addAll(
            menuView.getRoot(),
            playView.getRoot(),
            statsView.getRoot()
    );
  }

  public Pane getRoot() { return root; }

  public void render(StateType state, GameModel model, GameStatistic stats) {
    menuView.getRoot().setVisible(state == StateType.MENU);
    playView.getRoot().setVisible(state == StateType.GAME);
    statsView.getRoot().setVisible(state == StateType.STATISTICS);

    if (state == StateType.GAME && model != null) {
      playView.render(model);
    }
    if (state == StateType.STATISTICS && stats != null) {
      statsView.render(stats);
    }
  }
}
