package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorModelImplReadOnly;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.MusicNote;
import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;
import cs3500.music.view.CompositeView;
import cs3500.music.view.GuiViewImpl;
import cs3500.music.view.IGuiView;
import cs3500.music.view.IKeyListenerView;
import cs3500.music.view.IMetaListenerView;
import cs3500.music.view.IMouseListenerView;
import cs3500.music.view.IPlayPauseView;
import cs3500.music.view.IView;
import cs3500.music.view.KeyboardPanel;
import cs3500.music.view.MidiViewImpl;

/**
 * Controller for the Music Editor. Controls an IView, a Music Editor model and a current beat. As
 * the current beat or model changes, it sends the command to change to the view to update.
 */
public class MusicEditorController implements IController {
  private IView view; // The view of the Music Editor
  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model; // The Music Editor model
  private int currentBeat; // The current beat being played

  // The maximum beat of the composition. Used to make updating the current beat faster.
  private int maxBeat;

  // Whether the song is being played (true) or is paused (false)
  private boolean play;

  // Whether the Music Editor is in practice mode
  private boolean practiceMode;

  // The active pitches at each beat of the piece
  private HashMap<Integer, Set<Pitch>> pitchMap;

  // The notes played at the current beat in practice mode
  private Set<Pitch> notesPlayed;

  /**
   * Constructor for the controller. Takes in a view and a model. Sets the default currentBeat to 0.
   *
   * @param view The Music Editor view
   * @param model The Music Editor model
   */
  public MusicEditorController(IView view, MusicEditorOperations model) {
    this.view = view;
    this.model = model;
    currentBeat = 0;
    play = false;
    maxBeat = Collections.max(this.model.getNotes().keySet()) + 1;
  }

  @Override
  public void startApplication() {
    if (view instanceof IKeyListenerView) {
      configureKeyBoardListener();
    }
    if (view instanceof IMouseListenerView) {
      configureMouseListener();
    }
    if (view instanceof IMetaListenerView) {
      configureMetaListener();
    }

    view.display(new MusicEditorModelImplReadOnly(this.model));
    if (view instanceof MidiViewImpl) {
      MidiViewImpl midiViewImpl = (MidiViewImpl) view;
      midiViewImpl.play(0);
      try {
        Thread.sleep(Collections.max(model.getNotes().keySet()) * model.getTempo() / 1000);
      } catch (Exception e) {
        return;
      }
      midiViewImpl.stop();
    }
  }

  @Override
  public int getCurrentBeat() {
    return this.currentBeat;
  }

  /**
   * Configures the Keyboard Listener for the controller by creating three maps containing
   * Runnables.
   */
  private void configureKeyBoardListener() {
    Map<Character, Runnable> keyTypes = new HashMap<>();
    Map<Integer, Runnable> keyPresses = new HashMap<>();
    Map<Integer, Runnable> keyReleases = new HashMap<>();

    keyPresses.put(KeyEvent.VK_LEFT, new MoveLeft());
    keyPresses.put(KeyEvent.VK_RIGHT, new MoveRight());
    keyPresses.put(KeyEvent.VK_END, new End());
    keyPresses.put(KeyEvent.VK_HOME, new Home());

    if (view instanceof IPlayPauseView) {
      keyTypes.put(' ', new PlayOrPause());
    }

    KeyboardListener keyboardListener = new KeyboardListener();
    keyboardListener.setKeyTypedMap(keyTypes);
    keyboardListener.setKeyPressedMap(keyPresses);
    keyboardListener.setKeyReleasedMap(keyReleases);

    IKeyListenerView keyListenerView = (IKeyListenerView) view;

    keyListenerView.addKeyListener(keyboardListener);
  }

  /**
   * Configures the MouseListener for the controller by creating a MouseRunnable to add notes and
   * adds it to the view.
   */
  private void configureMouseListener() {
    MouseListener mouseListener = new MouseListener();
    mouseListener.setRunnable(new AddNote());

    IMouseListenerView mouseListenerView = (IMouseListenerView) view;
    mouseListenerView.addMouseListener(mouseListener);
  }

  /**
   * Configures the MetaListener for the controller by creating a new Runnable to increment the
   * current beat when a MetaMessage is encountered. Adds it to the view.
   */
  private void configureMetaListener() {
    MetaListener metaListener = new MetaListener();
    metaListener.setRunnable(new MoveWhilePlaying());

    IMetaListenerView metaListenerView = (IMetaListenerView) view;
    metaListenerView.addMetaListener(metaListener);
  }

  /**
   * Runnable to decrement the current beat when the left arrow is pressed. Cannot be incremented
   * while the song it playing.
   */
  class MoveLeft implements Runnable {
    @Override
    public void run() {
      if (!play && currentBeat != 0) {
        currentBeat--;
        IGuiView guiView = (IGuiView) view;
        guiView.updateCurrentBeat(currentBeat);
      }
    }
  }

  /**
   * Runnable to increment the current beat when the right arrow is pressed. Cannot be incremented
   * while the song it playing.
   */
  class MoveRight implements Runnable {
    @Override
    public void run() {
      if (!play && currentBeat != maxBeat) {
        currentBeat++;
        IGuiView guiView = (IGuiView) view;
        guiView.updateCurrentBeat(currentBeat);
      }
    }
  }

  /**
   * Runnable that increments the current beat while the song is playing.
   */
  class MoveWhilePlaying implements Runnable {
    @Override
    public void run() {
      if (currentBeat != maxBeat) {
        currentBeat++;
        IGuiView guiView = (IGuiView) view;
        guiView.updateCurrentBeat(currentBeat);
      }
    }
  }

  /**
   * Runnable that increments the current beat while the editor is in practice mode.
   */
  class MoveWhilePlayingPracticeMode implements Runnable {
    @Override
    public void run() {
      if (currentBeat != maxBeat
              &&
              pitchMap.get(currentBeat).equals(notesPlayed)) {
        currentBeat++;
        IGuiView guiView = (IGuiView) view;
        guiView.updateCurrentBeat(currentBeat);
      }
    }
  }

  /**
   * Runnable to put the current beat to the end of the composition when the end button is pressed.
   * This can only be done when the song is paused.
   */
  class End implements Runnable {
    @Override
    public void run() {
      if (!play) {
        currentBeat = maxBeat;
        IGuiView guiView = (IGuiView) view;
        guiView.updateCurrentBeat(currentBeat);
      }
    }
  }

  /**
   * Runnable to put the current beat to the beginning of the composition when the home button is
   * pressed. This can only be done when the song is paused.
   */
  class Home implements Runnable {
    @Override
    public void run() {
      if (!play) {
        currentBeat = 0;
        IGuiView guiView = (IGuiView) view;
        guiView.updateCurrentBeat(currentBeat);
      }
    }
  }

  class TogglePracticeMode implements Runnable {
    @Override
    public void run() {

    }
  }

  /**
   * Runnable to play or pause the view. If the view was being played, it is now paused and if the
   * view was paused, it is now being played.
   */
  class PlayOrPause implements Runnable {
    @Override
    public void run() {
      play = !play;
      IPlayPauseView playPauseView = (IPlayPauseView) view;
      if (play) {
        playPauseView.play(currentBeat);
      }
      else {
        playPauseView.pause();
      }
    }
  }

  /**
   * Mouse Runnable to add notes to the model. Notes are only added to the model if the mouse event
   * contains x and y coordinates that correspond to the keyboard keys in the GuiView.
   */
  class AddNote implements IMouseRunnable {
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
              y > halfHeight + KeyboardPanel.WHITE_KEY_HEIGHT
              ||
              play) {
        return;
      }
      Pitch pitch;
      if (y < halfHeight + KeyboardPanel.BLACK_KEY_HEIGHT + 14) {
        pitch = xToPitch(x);
      }
      else {
        int whiteKeyNum = (x - 20) / KeyboardPanel.WHITE_KEY_WIDTH;
        pitch = whiteKeyNumToPitch(whiteKeyNum);
      }
      model.addNote(new MusicNote(pitch, 1, currentBeat, 1, 100));
      currentBeat++;
      IGuiView guiView = (IGuiView) view;
      guiView.redraw(new MusicEditorModelImplReadOnly(model), currentBeat);

      if (view instanceof CompositeView) {
        CompositeView compositeView = (CompositeView) view;
        compositeView.addNote(new MusicNote(pitch, 1, currentBeat, 1, 100));
      }

      maxBeat = Collections.max(model.getNotes().keySet()) + 1;
    }
  }

  class PlayNodePracticeMode implements IMouseRunnable {
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
              y > halfHeight + KeyboardPanel.WHITE_KEY_HEIGHT
              ||
              play) {
        return;
      }
      Pitch pitch;
      if (y < halfHeight + KeyboardPanel.BLACK_KEY_HEIGHT + 14) {
        pitch = xToPitch(x);
      }
      else {
        int whiteKeyNum = (x - 20) / KeyboardPanel.WHITE_KEY_WIDTH;
        pitch = whiteKeyNumToPitch(whiteKeyNum);
      }

      if (pitchMap.get(currentBeat).contains(pitch)) {
        notesPlayed.add(pitch);
      }
    }
  }

  /**
   * Takes an x coordinate value and converts it to the Pitch corresponding to the key at that x
   * coordinate.
   *
   * @param x X Coordinate of the key
   * @return Pitch corresponding to the X Coordinate
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

    int octave = (x - 20) / 105 - 1;

    return new Pitch(noteName, octave);
  }

  /**
   * Takes a WhiteKeyNumber and converts it to the correct Pitch.
   *
   * @param whiteKeyNum The white key number
   * @return Pitch corresponding to the white key number
   * @throws IllegalArgumentException If an illegal white key number was entered
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
