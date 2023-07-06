package trabalho;

import javax.swing.*;

public class Router {

  private static Router instance;
  private JFrame currentFrame;

  private Router() {
  }

  public static Router getInstance() {
    if (instance == null) {
      instance = new Router();
    }
    return instance;
  }

  public void goTo(JFrame nextFrame) {
    if (this.currentFrame != null) {
      this.currentFrame.setVisible(false);
    }
    this.currentFrame = nextFrame;
    this.currentFrame.setVisible(true);
  }

}