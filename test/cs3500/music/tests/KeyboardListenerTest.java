package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import cs3500.music.controller.KeyboardListener;
import cs3500.music.controller.MockRunnable;
import cs3500.music.view.CompositeView;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the KeyboardListener class. Uses a KeyboardListener initialized with a mock runnable
 * that logs the appropriate action taken on specific keyEvents. A robot is used to simulate key
 * presses.
 */
public class KeyboardListenerTest {
  private Robot robot;

  /**
   * Initializes the keyboardView, robot, and keyListener with the mock runnable. Appropriate maps
   * are created for mapping key events to their respective mock runnable.
   */
  @Before
  public void doBefore() {
    KeyboardListener keyboardListener;
    CompositeView keyListenerView;
    Map<Character, Runnable> keyTypes = new HashMap<>();
    Map<Integer, Runnable> keyPresses = new HashMap<>();
    Map<Integer, Runnable> keyReleases = new HashMap<>();

    keyPresses.put(KeyEvent.VK_LEFT, new MockRunnable(KeyEvent.VK_LEFT));
    keyPresses.put(KeyEvent.VK_RIGHT, new MockRunnable(KeyEvent.VK_RIGHT));
    keyPresses.put(KeyEvent.VK_END, new MockRunnable(KeyEvent.VK_END));
    keyPresses.put(KeyEvent.VK_HOME, new MockRunnable(KeyEvent.VK_HOME));

    keyTypes.put(' ', new MockRunnable(' '));

    keyboardListener = new KeyboardListener();
    keyboardListener.setKeyTypedMap(keyTypes);
    keyboardListener.setKeyPressedMap(keyPresses);
    keyboardListener.setKeyReleasedMap(keyReleases);

    keyListenerView = new CompositeView();
    keyListenerView.addKeyListener(keyboardListener);
    keyListenerView.initialize();

    try {
      robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests that pressing the left key results in a call to MoveLeft.
   */
  @Test
  public void testLeftKey() {
    robot.keyPress(KeyEvent.VK_LEFT);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_LEFT);

    assertTrue(MockRunnable.EVENT_LOG.toString().equals("MoveLeft\n"));
  }

  /**
   * Tests that pressing the right key results in a call to MoveRight.
   */
  @Test
  public void testRightKey() {
    robot.keyPress(KeyEvent.VK_RIGHT);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_RIGHT);

    assertTrue(MockRunnable.EVENT_LOG.toString().equals("MoveRight\n"));
  }

  /**
   * Tests that pressing the home key results in a call to Home.
   */
  @Test
  public void testHomeKey() {
    robot.keyPress(KeyEvent.VK_HOME);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_HOME);

    assertTrue(MockRunnable.EVENT_LOG.toString().equals("Home\n"));
  }

  /**
   * Tests that pressing the end key results in a call to End.
   */
  @Test
  public void testEndKey() {
    robot.keyPress(KeyEvent.VK_END);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_END);

    assertTrue(MockRunnable.EVENT_LOG.toString().equals("End\n"));
  }

  /**
   * Tests that pressing the space key results in a call to PlayOrPause.
   */
  @Test
  public void testSpaceKey() {
    robot.keyPress(KeyEvent.VK_SPACE);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_SPACE);

    assertTrue(MockRunnable.EVENT_LOG.toString().equals("PlayOrPause\n"));
  }

  /**
   * Tests that pressing an unmapped key does not result in a call to any runnable.
   */
  @Test
  public void testUnmappedKey() {
    robot.keyPress(KeyEvent.VK_UP);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_UP);

    assertTrue(MockRunnable.EVENT_LOG.toString().isEmpty());
  }

  /**
   * Tests that pressing multiple keys simultaneously results in all of their appropriate runnables
   * being called.
   */
  @Test
  public void testAllKeys() {
    robot.keyPress(KeyEvent.VK_LEFT);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_LEFT);
    robot.keyPress(KeyEvent.VK_RIGHT);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_RIGHT);
    robot.keyPress(KeyEvent.VK_HOME);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_HOME);
    robot.keyPress(KeyEvent.VK_END);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_END);
    robot.keyPress(KeyEvent.VK_SPACE);
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_SPACE);

    assertTrue(MockRunnable.EVENT_LOG.toString().contains("MoveLeft")
            && MockRunnable.EVENT_LOG.toString().contains("MoveRight")
            && MockRunnable.EVENT_LOG.toString().contains("Home")
            && MockRunnable.EVENT_LOG.toString().contains("End")
            && MockRunnable.EVENT_LOG.toString().contains("PlayOrPause"));
  }
}