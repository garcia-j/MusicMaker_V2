package cs3500.music.view;

import java.io.IOException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorOperations;

/**
 * The Console View for a Music Editor. Beat numbers are displayed down the left side of the console
 * and the range of pitches from the lowest pitch of a note played to the highest pitch of a note
 * played is displayed across the top of the console. A start beat of a note is represented by an
 * "X" while the continuation of a beat is represented by a "|". If a note is not played at a
 * certain beat, the spot is blank.
 */
public class ConsoleView implements IView<MusicEditorOperations<IMusicNote<IPitch>, IPitch>> {

  /**
   * The appendable to append output to the console.
   */
  private final Appendable appendable;

  /**
   * Constructor for ConsoleView. Sets the Appendable for this ConsoleView.
   */
  public ConsoleView() {
    appendable = System.out;
  }

  /**
   * Convenience constructor to test ConsoleView.
   *
   * @param appendable The appendable to set this appendable to
   */
  public ConsoleView(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public void display(MusicEditorOperations<IMusicNote<IPitch>, IPitch> model) {
    TreeMap<Integer, Set<IMusicNote<IPitch>>> notes =
            new TreeMap<Integer, Set<IMusicNote<IPitch>>>(model.getNotes());
    IPitch lowPitch = model.getLowPitch();
    IPitch highPitch = model.getHighPitch();
    if (lowPitch == null || highPitch == null || notes.size() == 0) {
      try {
        appendable.append("");
      } catch (IOException e) {
        return;
      }
      return;
    }

    int numBeatDigits = String.valueOf(notes.lastKey()).length();
    StringBuilder builder = new StringBuilder(printPitches(notes, lowPitch, highPitch));
    int highLowPitchDifference = lowPitch.getDifference(highPitch);

    for (int i = 0; i < notes.lastKey() + 1; i++) {
      builder.append(String.format("%" + numBeatDigits + "s", i));

      if (!notes.containsKey(i)) {
        for (int j = 0; j < highLowPitchDifference + 1; j++) {
          builder.append("     ");
        }
      }
      else {
        IPitch toPrint = lowPitch;
        for (int j = 0; j < highLowPitchDifference + 1; j++) {

          Set<IMusicNote<IPitch>> notesToPrint = notes.get(i);
          boolean printX = false;
          boolean printLine = false;

          for (IMusicNote<IPitch> note : notesToPrint) {
            if (note.getPitch().compareTo(toPrint) == 0 && note.getStartBeat() == i) {
              printX = true;
              break;
            }
            else if (note.getPitch().compareTo(toPrint) == 0) {
              printLine = true;
            }
          }

          if (printX) {
            builder.append("  X  ");
          }
          else if (printLine) {
            builder.append("  |  ");
          }
          else {
            builder.append("     ");
          }

          if (j != highLowPitchDifference) {
            toPrint = toPrint.nextPitch();
          }
        }
      }
      builder.append("\n");
    }
    try {
      appendable.append(builder.toString());
    } catch (IOException e) {
      return;
    }
  }

  /**
   * Prints the top line of the editor state, which is the pitches in order.
   * @return The pitches
   */
  private String printPitches(SortedMap<Integer, Set<IMusicNote<IPitch>>> notes, IPitch low,
                              IPitch high) {
    StringBuilder builder = new StringBuilder();
    int numBeatDigits = String.valueOf(notes.lastKey()).length();

    for (int i = 0; i < numBeatDigits; i++) {
      builder.append(' ');
    }

    IPitch p = low;
    for (int i = 0; i < low.getDifference(high) + 1; i++) {
      StringBuilder temp = new StringBuilder();
      if (p.toString().length() == 2) {
        String s = " " + p.toString();
        temp.append(s);
      }
      else {
        temp.append(p.toString());
      }

      builder.append(String.format("%5s", String.format("%-4s", temp.toString())));

      if (i != low.getDifference(high)) {
        p = p.nextPitch();
      }
    }

    builder.append("\n");

    return builder.toString();
  }
}
