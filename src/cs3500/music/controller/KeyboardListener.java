package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Represents a keyboard listener. Provides support for three types of methods: Methods for when
 * a key is typed, methods for when a key is pressed, and methods for when a key is released. Each
 * method is mapped to its given character/integer value in KeyListener.
 */
public class KeyboardListener implements KeyListener {
  private Map<Character, Runnable> keyTypedMap; // Map of characters typed to functions
  private Map<Integer, Runnable> keyPressedMap; // Map of key typed to functions
  private Map<Integer, Runnable> keyReleasedMap; // Map of key released to functions

  /**
   * Default Constructor for KeyboardListener.
   */
  public KeyboardListener() {
    // Blank default constructor (was in lecture)
  }

  /**
   * Sets the Map for typed keys to the passed in Map.
   *
   * @param map The new map
   */
  public void setKeyTypedMap(Map<Character, Runnable> map) {
    this.keyTypedMap = map;
  }

  /**
   * Sets the Map for pressed keys to the passed in Map.
   *
   * @param map The new map
   */
  public void setKeyPressedMap(Map<Integer, Runnable> map) {
    this.keyPressedMap = map;
  }

  /**
   * Sets the Map for released keys to the passed in Map.
   *
   * @param map The new map
   */
  public void setKeyReleasedMap(Map<Integer, Runnable> map) {
    this.keyReleasedMap = map;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (keyTypedMap.containsKey(e.getKeyChar())) {
      keyTypedMap.get(e.getKeyChar()).run();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (keyPressedMap.containsKey(e.getKeyCode())) {
      keyPressedMap.get(e.getKeyCode()).run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (keyReleasedMap.containsKey(e.getKeyCode())) {
      keyReleasedMap.get(e.getKeyCode()).run();
    }
  }
}
