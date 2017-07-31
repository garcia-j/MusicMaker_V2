package cs3500.music.view;

import java.awt.event.MouseListener;

/**
 * Interface for views that use MouseListeners. Views that need to respond to a MouseEvent will
 * implement this interface.
 */
public interface IMouseListenerView {

  /**
   * Add the passed in MouseListener to the view.
   *
   * @param mouseListener The MouseListener to add
   */
  void addMouseListener(MouseListener mouseListener);
}
