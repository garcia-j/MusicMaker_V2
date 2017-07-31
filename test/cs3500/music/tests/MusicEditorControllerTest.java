package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import cs3500.music.controller.MusicEditorController;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.MusicNote;
import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;
import cs3500.music.view.CompositeView;
import cs3500.music.view.IView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the MusicEditor's Controller.
 */
public class MusicEditorControllerTest {
  private MusicEditorController controller;
  private MusicEditorOperations model;
  private Robot robot;

  /**
   * Initializes the different types of views and the robot used to simulate mouse clicks and key
   * presses.
   */
  @Before
  public void doBefore() {
    IView keyView;
    keyView = new CompositeView();
    model = new MusicEditorModelImpl();
    try {
      robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }

    model.addNote(new MusicNote(new Pitch(NoteName.A_SHARP, 1), 10, 10, 1, 1));
    controller = new MusicEditorController(keyView, model);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests that the controller initialized with a keyboardView behaves appropriately when the
   * right key is pressed.
   */
  @Test
  public void testKeyInitializationMoveRight() {
    controller.startApplication();

    robot.keyPress(KeyEvent.VK_RIGHT);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_RIGHT);

    assertEquals(1, controller.getCurrentBeat());
  }

  /**
   * Tests that the controller initialized with a keyboardView behaves appropriately when the
   * left key is pressed. The beat is first moved to the right and then moved back to 0.
   */
  @Test
  public void testKeyInitializationMoveLeft() {
    controller.startApplication();

    robot.keyPress(KeyEvent.VK_RIGHT);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_RIGHT);
    assertEquals(1, controller.getCurrentBeat());
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyPress(KeyEvent.VK_LEFT);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_LEFT);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests that the controller initialized with a keyboardView behaves appropriately when the
   * end key is pressed.
   */
  @Test
  public void testKeyInitializationEND() {
    controller.startApplication();

    robot.keyPress(KeyEvent.VK_END);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    robot.keyRelease(KeyEvent.VK_END);
    assertEquals(20, controller.getCurrentBeat());
  }

  /**
   * Tests that the controller behaves accordingly when all keys are pressed consecutively.
   * @throws InterruptedException If the thread encounters a problem when sleeping
   */
  @Test
  public void testAllKeys() throws InterruptedException {
    controller.startApplication();

    robot.keyPress(KeyEvent.VK_RIGHT);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_RIGHT);
    assertEquals(1, controller.getCurrentBeat());
    Thread.sleep(2000);

    robot.keyPress(KeyEvent.VK_END);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_END);
    assertEquals(20, controller.getCurrentBeat());
    Thread.sleep(2000);

    robot.keyPress(KeyEvent.VK_LEFT);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_LEFT);
    assertEquals(19, controller.getCurrentBeat());
    Thread.sleep(2000);

    robot.keyPress(KeyEvent.VK_HOME);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_HOME);
    assertEquals(0, controller.getCurrentBeat());
    Thread.sleep(2000);

    robot.keyPress(KeyEvent.VK_SPACE);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_SPACE);
    assertEquals(20, controller.getCurrentBeat());
    Thread.sleep(2000);
  }

  /**
   * Tests that the controller behaves accordingly when the home key is pressed.
   * @throws InterruptedException If the thread encounters a problem when sleeping
   */
  @Test
  public void testKeyInitializationHome() throws InterruptedException {
    controller.startApplication();

    robot.keyPress(KeyEvent.VK_END);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_END);
    assertEquals(20, controller.getCurrentBeat());

    robot.keyPress(KeyEvent.VK_HOME);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_HOME);
    assertEquals(0, controller.getCurrentBeat());
  }

  /**
   * Tests that pressing the space key disallows left/right movement through the piece.
   * @throws InterruptedException If the thread encounters a problem when sleeping
   */
  @Test
  public void testKeyInitializationPlay() throws InterruptedException {
    controller.startApplication();

    robot.keyPress(KeyEvent.VK_SPACE);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_SPACE);
    robot.keyPress(KeyEvent.VK_LEFT);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_LEFT);
    assertEquals(20, controller.getCurrentBeat());
    robot.keyPress(KeyEvent.VK_SPACE);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_SPACE);
    robot.keyPress(KeyEvent.VK_LEFT);
    Thread.sleep(2000);
    robot.keyRelease(KeyEvent.VK_LEFT);
    assertEquals(19, controller.getCurrentBeat());
  }

  /**
   * Tests that the controller behaves accordingly when a new note is added through a mouse click.
   * @throws InterruptedException If the thread encounters a problem when sleeping
   */
  @Test
  public void testMouseInitializationAddNote() throws InterruptedException {
    controller.startApplication();
    robot.mouseMove(420, 400);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.delay(100);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.delay(100);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    MusicNote note = new MusicNote(new Pitch(NoteName.A, 2), 1, 0, 1, 100);

    assertTrue(model.getNotesAtBeat(0).contains(note));
  }
}