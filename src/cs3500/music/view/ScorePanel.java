
package cs3500.music.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorOperations;

/**
 * Represents a score of a Music Editor. The score section displays the notes in the Music Editor
 * on the left side in a range from the lowest to highest pitch, with the highest pitch at the top
 * of the left side. It displays the beats in the Music Editor along the top of the score section
 * with each beat number corresponding to four beats in the Music Editor. A start beat of a note is
 * represented by a black square while a continuation beat of a note is represented by a green
 * square. The current beat of the Music Editor is represented by a red line and moves based on user
 * input.
 */
public class ScorePanel extends JPanel implements
        IGuiPanel<MusicEditorOperations<IMusicNote<IPitch>, IPitch>> {
  private static final int NOTE_DIMENSION = 15; // The height and width of a note
  private static final int X_OFFSET = 50; // The offset in the y direction
  private static final int Y_OFFSET = 10; // The offset in the x direction

  // The model containing the notes of the editor
  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model;

  // The current beat being played
  private int currentBeat;

  /**
   * Constructor for ScorePanel. Sets the model to a new ReadOnly model and the current beat being
   * played to 0.
   */
  public ScorePanel() {
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

    Graphics2D graphics2D = (Graphics2D)g;
    TreeMap<Integer, Set<IMusicNote<IPitch>>> notes =
            new TreeMap<Integer, Set<IMusicNote<IPitch>>>(model.getNotes());
    if (model.getLowPitch() == null || model.getHighPitch() == null) {
      return;
    }
    IPitch lowPitch = model.getLowPitch();
    IPitch highPitch = model.getHighPitch();

    drawNumbers(g, notes);
    drawNotes(g, lowPitch, highPitch, notes);
    drawPitchesAndBars(g, lowPitch, highPitch, notes);
    drawLine(g, lowPitch, highPitch);

  }

  /**
   * Draws the numbers along the top of the score.
   * @param g Graphics
   * @param notes The notes in the model
   */
  private void drawNumbers(Graphics g, TreeMap<Integer, Set<IMusicNote<IPitch>>> notes) {
    Graphics2D graphics2D = (Graphics2D)g;
    int maxBeat = notes.lastKey();
    while (maxBeat % 4 != 0) {
      maxBeat++;
    }

    graphics2D.setFont(new Font(Font.SANS_SERIF, Font.BOLD, NOTE_DIMENSION));
    graphics2D.setStroke(new BasicStroke(2));
    int lowRange = currentBeat / 72 * 72;
    int highRange = (currentBeat / 72 + 1) * 72 + 1;
    for (int i = lowRange; i < Math.min(highRange, maxBeat + 1); i += 4) {
      if (i != highRange - 1) {
        graphics2D.drawString(String.valueOf(i), NOTE_DIMENSION * (i % 72) + X_OFFSET,
                10 + Y_OFFSET);
      }
      else {
        graphics2D.drawString(String.valueOf(i), NOTE_DIMENSION * 72 + X_OFFSET, 10 + Y_OFFSET);
      }
    }
  }

  /**
   * Draws the notes for the score view.
   * @param g Graphics
   * @param lowPitch The lowest pitch in the song
   * @param highPitch The highest pitch in the song
   * @param notes The notes in the song
   */
  private void drawNotes(Graphics g, IPitch lowPitch, IPitch highPitch,
                         TreeMap<Integer, Set<IMusicNote<IPitch>>> notes) {
    int pitchDifference = lowPitch.getDifference(highPitch);
    IPitch currentPitch = lowPitch;
    for (Integer i : notes.keySet()) {

      if (i < currentBeat / 72 * 72 || i > currentBeat / 72 * 72 + 75) {
        continue;
      }

      Set<IMusicNote<IPitch>> notesToDraw = notes.get(i);
      for (int j = pitchDifference; j >= 0; j--) {
        for (IMusicNote<IPitch> n : notesToDraw) {
          if (n.getPitch().compareTo(currentPitch) == 0) {
            if (n.getStartBeat() == i) {
              g.setColor(Color.BLACK);
            }
            else {
              g.setColor(Color.GREEN);
            }

            if (i < currentBeat / 72 * 72 + 72) {
              g.fillRect(NOTE_DIMENSION * (i % 72) + X_OFFSET,
                      j * NOTE_DIMENSION + NOTE_DIMENSION + Y_OFFSET,
                      NOTE_DIMENSION, NOTE_DIMENSION);
            }
            else {
              g.fillRect(NOTE_DIMENSION * (i % 72 + 72) + X_OFFSET,
                      j * NOTE_DIMENSION + NOTE_DIMENSION + Y_OFFSET,
                      NOTE_DIMENSION, NOTE_DIMENSION);
            }
            break;
          }
        }

        if (j != 0) {
          currentPitch = currentPitch.nextPitch();
        }
      }
      currentPitch = lowPitch;
    }
  }

  /**
   * Draws the pitches and bars for the score.
   * @param g Graphics
   * @param lowPitch The lowest pitch of the composition
   * @param highPitch The highest pitch of the composition
   * @param notes The notes in the composition
   */
  private void drawPitchesAndBars(Graphics g, IPitch lowPitch, IPitch highPitch,
                           TreeMap<Integer, Set<IMusicNote<IPitch>>> notes) {
    IPitch currentPitch = lowPitch;
    g.setColor(Color.BLACK);
    int pitchDifference = lowPitch.getDifference(highPitch);
    int lastScreen;
    if (currentBeat >= notes.lastKey() / 72 * 72 && currentBeat <= notes.lastKey() + 1) {
      lastScreen = (notes.lastKey() - notes.lastKey() / 72 * 72) / 4 + 1;
    }
    else {
      lastScreen = 21;
    }
    for (int i = pitchDifference; i >= 0; i--) {
      g.drawString(currentPitch.toString(), 10, i * NOTE_DIMENSION + NOTE_DIMENSION * 2
              +
              Y_OFFSET);

      for (int j = 0; j < Math.min(20, lastScreen); j++) {
        g.drawRect(NOTE_DIMENSION  * j * 4 + X_OFFSET, i * NOTE_DIMENSION + NOTE_DIMENSION
                +
                Y_OFFSET, NOTE_DIMENSION * 4, NOTE_DIMENSION);
      }

      if (i != 0) {
        currentPitch = currentPitch.nextPitch();
      }
    }
  }

  /**
   * Draws the red bar on the GUI view.
   * @param g Graphics
   * @param lowPitch The lowest pitch in the model
   * @param highPitch The highest pitch in the model
   */
  private void drawLine(Graphics g, IPitch lowPitch, IPitch highPitch) {
    int pitchDifference = lowPitch.getDifference(highPitch);
    g.setColor(Color.RED);
    g.drawLine((currentBeat % 72) * NOTE_DIMENSION + X_OFFSET, NOTE_DIMENSION + Y_OFFSET,
            (currentBeat % 72) * NOTE_DIMENSION + X_OFFSET,
            (pitchDifference + 2) * NOTE_DIMENSION + Y_OFFSET);
  }
}