package controller.state;

public interface IState {
  void onEnter();
  void onExit();
  void update(double dt);
}
