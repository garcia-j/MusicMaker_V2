package cs3500.music.view;

import java.awt.Graphics;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.NoteName;

/**
 * Represents a keyboard containing ten octaves. Keeps track of all the notes of the Music Editor
 * and the current beat that is being played. Notes that are currently being played are shown as
 * orange keys.
 */
public class KeyboardPanel extends JPanel implements
        IGuiPanel<MusicEditorOperations<IMusicNote<IPitch>, IPitch>> {
  public static final int OFFSET = 20; // The left offset of the panel
  public static final int WHITE_KEY_WIDTH = 15; // The width of a white key in pixels
  public static final int WHITE_KEY_HEIGHT = 200; // The height of a white key in pixels

  // The width of a black key in pixels
  private static final int BLACK_KEY_WIDTH = WHITE_KEY_WIDTH / 2;

  // The height of a black key in pixels
  public static final int BLACK_KEY_HEIGHT = WHITE_KEY_HEIGHT / 2;

  // The model of the view containing notes
  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model;

  // The current beat that is being played
  private int currentBeat;

  /**
   * Constructor for KeyboardPanel. Sets the model to a new readonly model
   */
  public KeyboardPanel() {
    this.model = null;
    this.currentBeat = 0;
  }

  @Override
  public void updateCurrentBeat(int currentBeat) {
    this.currentBeat = currentBeat;
  }

  @Override
  public void redraw(MusicEditorOperations<IMusicNote<IPitch>, IPitch> model, int currentBeat) {
    this.model = model;
    this.currentBeat = currentBeat;
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    TreeMap<Integer, Set<IMusicNote<IPitch>>> notes =
            new TreeMap<Integer, Set<IMusicNote<IPitch>>>(model.getNotes());
    Set<IMusicNote<IPitch>> currentNotes = notes.get(currentBeat);

    if (currentNotes == null) {
      currentNotes = new HashSet<IMusicNote<IPitch>>();
    }

    for (int i = 0; i < 70; i++) {
      g.setColor(Color.WHITE);
      g.fillRect(i * WHITE_KEY_WIDTH + OFFSET, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
      g.setColor(Color.BLACK);
      g.drawRect(i * WHITE_KEY_WIDTH + OFFSET, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
    }

    g.setColor(Color.BLACK);
    for (int i = 0; i < 70; i++) {
      if (i % 7 != 0 && (i - 3) % 7 != 0) {
        g.fillRect(i * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + OFFSET, 0,
                BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
      }
    }

    Set<IPitch> whiteKeys = new HashSet<IPitch>();
    Set<IPitch> blackKeys = new HashSet<IPitch>();
    for (IMusicNote<IPitch> n : currentNotes) {
      IPitch pitch = n.getPitch();
      switch (pitch.getNoteName()) {
        case A_SHARP:
          blackKeys.add(pitch);
          break;
        case C_SHARP:
          blackKeys.add(pitch);
          break;
        case D_SHARP:
          blackKeys.add(pitch);
          break;
        case F_SHARP:
          blackKeys.add(pitch);
          break;
        case G_SHARP:
          blackKeys.add(pitch);
          break;
        default:
          whiteKeys.add(pitch);
      }
    }

    for (IPitch p : whiteKeys) {
      g.setColor(Color.ORANGE);
      int keyNum = pitchToKey(p);
      int mod12 = keyNum % 12;
      int toAddWhite = getWhiteKeyToAdd(mod12);

      int x = (keyNum / 12 * 7 + toAddWhite) * WHITE_KEY_WIDTH + OFFSET;
      g.fillRect(x, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
      g.setColor(Color.BLACK);
      g.drawRect(x, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);

      if (p.getNoteName() != NoteName.C
              &&
              p.getNoteName() != NoteName.F) {
        g.fillRect(x - BLACK_KEY_WIDTH / 2, 0, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
      }

      if (p.getNoteName() != NoteName.E
              &&
              p.getNoteName() != NoteName.B) {
        g.fillRect(x + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2, 0,
                BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
      }
    }

    for (IPitch p : blackKeys) {
      g.setColor(Color.ORANGE);
      int keyNum = pitchToKey(p);
      int mod12 = keyNum % 12;
      int toAddBlack = getBlackKeyToAdd(mod12);
      g.fillRect((keyNum / 12 * 7 + toAddBlack) * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + OFFSET,
              0, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
      g.setColor(Color.BLACK);
      g.drawRect((keyNum / 12 * 7 + toAddBlack) * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 + OFFSET,
              0, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
    }
  }

  /**
   * Gets the correct int needed to convert the key number to the correct black key number.
   *
   * @param i The key number
   * @return The black key number to add
   */
  private int getBlackKeyToAdd(int i) {
    switch (i) {
      case 10:
        return 6;
      case 8:
        return 5;
      case 6:
        return 4;
      case 3:
        return 2;
      case 1:
        return 1;
      default:
        return 0;
    }
  }

  /**
   * Gets the correct int needed to convert the key number to the correct white key number.
   *
   * @param i The key number
   * @return The white key number to add
   */
  private int getWhiteKeyToAdd(int i) {
    switch (i) {
      case 0:
        return 0;
      case 2:
        return 1;
      case 4:
        return 2;
      case 5:
        return 3;
      case 7:
        return 4;
      case 9:
        return 5;
      case 11:
        return 6;
      default:
        return 0;
    }
  }

  /**
   * Converts the pitch to the key number.
   *
   * @param pitch The pitch to convert
   * @return The key number of the pitch
   */
  private int pitchToKey(IPitch pitch) {
    NoteName noteName = pitch.getNoteName();
    int octave = pitch.getOctave();
    int octaveKey = (octave + 1) * 12;

    return octaveKey + noteName.ordinal();
  }
}
