package cs3500.music.controller;

import java.awt.event.MouseEvent;

/**
 * A MouseRunnable uses a passed in mouseEvent to in its run function. This allows the
 * MouseListener to pass a MouseEvent to the controller.
 */
public interface IMouseRunnable {

  /**
   * Runs the MouseRunnable using the MouseEvent.
   *
   * @param mouseEvent The MouseEvent to be run with
   */
  void run(MouseEvent mouseEvent);
}
