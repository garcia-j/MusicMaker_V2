package cs3500.music.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A Listener for the mouse. When the right button of the mouse is clicked, the MouseRunnable is run
 * using the MouseEvent that was just created from the click.
 */
public class MouseListener extends MouseAdapter {
  // MouseRunnable to be run when the right button of the mouse is clicked
  IMouseRunnable runnable;

  /**
   * Set the MouseListener's MouseRunnable to the given MouseRunnable.
   *
   * @param runnable The MouseRunnable to be set
   */
  public void setRunnable(IMouseRunnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void mouseClicked(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
      runnable.run(mouseEvent);
    }
  }
}
