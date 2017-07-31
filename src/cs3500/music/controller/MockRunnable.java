package cs3500.music.controller;

import java.awt.event.MouseEvent;

import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;
import cs3500.music.view.GuiViewImpl;
import cs3500.music.view.KeyboardPanel;

/**
 * Mock Runnable class used to log calls to Runnable objects used by the KeyboardListener for
 * testing purposes.
 */
public class MockRunnable implements Runnable, IMouseRunnable {
  // logs all calls to this Runnable
  public static StringBuilder EVENT_LOG;

  //The count of calls to the metaListener runnable.
  private int metaCount;

  //Represents which runnable this mock is mocking.
  private String action;

  /**
   * Constructor for mocking keyListener Runnables.
   */
  public MockRunnable(int keyEvent) {
    EVENT_LOG = new StringBuilder();
    switch (keyEvent) {
      case 37: action = "MoveLeft";
        break;
      case 39: action = "MoveRight";
        break;
      case 35: action = "End";
        break;
      case 36: action = "Home";
        break;
      default: break;
    }
  }

  /**
   * Constructor for mocking space key runnable.
   */
  public MockRunnable(char keyEvent) {
    EVENT_LOG = new StringBuilder();
    if (keyEvent == ' ') {
      action = "PlayOrPause";
    }
  }

  /**
   * Constructor for mocking either mouseListener or metaListener Runnables.
   * @param type The type of Runnable to create
   */
  public MockRunnable(String type) {
    EVENT_LOG = new StringBuilder();
    if (type.equals("mouse")) {
      action = "AddNote";
    }
    else if (type.equals("meta")) {
      metaCount = 0;
      action = "MoveWhilePlaying";
    }
  }

  @Override
  public void run() {
    if (action.equals("MoveWhilePlaying")) {
      metaCount++;
      EVENT_LOG = new StringBuilder();
      EVENT_LOG.append("MoveWhilePlaying count: " + metaCount);
    }
    else {
      EVENT_LOG.append(action + "\n");
    }
  }

  @Override
  public void run(MouseEvent mouseEvent) {
    int x = mouseEvent.getX();
    int y = mouseEvent.getY();
    int halfHeight = GuiViewImpl.HEIGHT / 2 + 10;

    if (x < KeyboardPanel.OFFSET
            ||
            x > KeyboardPanel.OFFSET + 70 * KeyboardPanel.WHITE_KEY_WIDTH
            ||
            y < halfHeight
            ||
            y > halfHeight + KeyboardPanel.WHITE_KEY_HEIGHT) {
      EVENT_LOG.append(action + " out of bounds\n");
      return;
    }

    Pitch pitch;
    if (y < halfHeight + KeyboardPanel.BLACK_KEY_HEIGHT + 14) {
      pitch = this.xToPitch(x);
    }
    else {
      int whiteKeyNum = (x - 20) / KeyboardPanel.WHITE_KEY_WIDTH;
      pitch = this.whiteKeyNumToPitch(whiteKeyNum);
    }

    EVENT_LOG.append(action + " " + pitch.toString());
  }

  /**
   * Method copied from MusicEditorController to determine which note is added from a mouse click.
   *
   * @param x The x coordinate of the key
   * @return The correct Pitch
   */
  private Pitch xToPitch(int x) {
    int realX = (x - 20) % 105;
    NoteName noteName;

    if (realX >= 0 && realX <= 11) {
      noteName = NoteName.C;
    }
    else if (realX >= 12 && realX <= 19) {
      noteName = NoteName.C_SHARP;
    }
    else if (realX >= 20 && realX <= 27) {
      noteName = NoteName.D;
    }
    else if (realX >= 28 && realX <= 33) {
      noteName = NoteName.D_SHARP;
    }
    else if (realX >= 34 && realX <= 46) {
      noteName = NoteName.E;
    }
    else if (realX >= 47 && realX <= 57) {
      noteName = NoteName.F;
    }
    else if (realX >= 58 && realX <= 64) {
      noteName = NoteName.F_SHARP;
    }
    else if (realX >= 65 && realX <= 72) {
      noteName = NoteName.G;
    }
    else if (realX >= 73 && realX <= 78) {
      noteName = NoteName.G_SHARP;
    }
    else if (realX >= 79 && realX <= 87) {
      noteName = NoteName.A;
    }
    else if (realX >= 88 && realX <= 94) {
      noteName = NoteName.A_SHARP;
    }
    else {
      noteName = NoteName.B;
    }

    int octave = (x - 20) / 105 + 1;

    return new Pitch(noteName, octave);
  }

  /**
   * Method copied from MusicEditorController to determine which note is added from a mouse click.
   *
   * @param whiteKeyNum The white key number to convert
   * @return The correct pitch corresponding to the white key
   * @throws IllegalArgumentException If the white key number was unexpected
   */
  private Pitch whiteKeyNumToPitch(int whiteKeyNum) throws IllegalArgumentException {
    NoteName noteName;
    switch (whiteKeyNum % 7) {
      case 0:
        noteName = NoteName.C;
        break;
      case 1:
        noteName = NoteName.D;
        break;
      case 2:
        noteName = NoteName.E;
        break;
      case 3:
        noteName = NoteName.F;
        break;
      case 4:
        noteName = NoteName.G;
        break;
      case 5:
        noteName = NoteName.A;
        break;
      case 6:
        noteName = NoteName.B;
        break;
      default:
        throw new IllegalArgumentException("Unexpected int");
    }

    int octave = whiteKeyNum / 7 - 1;
    return new Pitch(noteName, octave);
  }
}

