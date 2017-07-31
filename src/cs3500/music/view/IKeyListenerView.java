package cs3500.music.view;

import java.awt.event.KeyListener;

/**
 * Interface for views that use key listener. Views that implement this interface are those that
 * respond to keyboard input from the user. The only method included is one to add a KeyListener
 * to the view.
 */
public interface IKeyListenerView {

  /**
   * Adds the KeyListener to the view so that it can respond to keyboard input.
   *
   * @param keyListener The KeyListener to add
   */
  void addKeyListener(KeyListener keyListener);
}
