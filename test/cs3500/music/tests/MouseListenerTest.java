package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

import cs3500.music.controller.IMouseRunnable;
import cs3500.music.controller.MockRunnable;
import cs3500.music.controller.MouseListener;
import cs3500.music.view.CompositeView;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the MouseListener class. Uses a MouseListener initialized with a mock Runnable that
 * logs when a specific note is added, and when a mouse click happened but was outside of the
 * expected area. A robot is used to simulate mouse clicks.
 */
public class MouseListenerTest {
  private Robot robot;

  /**
   * Initializes the robot, mouseView, and the mouseListener with a mock runnable.
   */
  @Before
  public void doBefore() {
    MouseListener mouseListener;
    CompositeView mouseView;
    IMouseRunnable mouseRunnable = new MockRunnable("mouse");

    mouseListener = new MouseListener();
    mouseListener.setRunnable(mouseRunnable);

    mouseView = new CompositeView();
    mouseView.addMouseListener(mouseListener);
    mouseView.initialize();

    try {
      robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests that clicking a note causes the mouseListener to call its runnable. Two clicks are
   * required as there are cases when running the test from certain buttons in the IDE will cause
   * the first click to only toggle a switch to the MusicEditor window.
   */
  @Test
  public void testMouseEvent() {
    robot.mouseMove(420, 400);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    assertTrue(MockRunnable.EVENT_LOG.toString().contains("AddNote A4"));
  }

  /**
   * Tests adding multiple notes with the mouse.
   */
  @Test
  public void testMouseEventMultipleNotes() {
    robot.mouseMove(420, 400);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    assertTrue(MockRunnable.EVENT_LOG.toString().contains("AddNote A4"));
    robot.mouseMove(423, 400);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    assertTrue(MockRunnable.EVENT_LOG.toString().contains("AddNote A#4"));
  }

  /**
   * Tests that clicking somewhere out of bounds of the keyboard causes the mouseListener to call
   * its runnable, aware that it is not a valid click. Two clicks are required as there are cases
   * when running the test from certain buttons in the IDE will cause the first click to only toggle
   * a switch to the MusicEditor window.
   */
  @Test
  public void testMouseEventOutOfBounds() {
    robot.mouseMove(10, 400);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    assertTrue(MockRunnable.EVENT_LOG.toString().contains("AddNote out of bounds"));
  }
}