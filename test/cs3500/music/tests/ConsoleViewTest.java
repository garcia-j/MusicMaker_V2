package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.model.MusicEditorModelImplReadOnly;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.MusicNote;
import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.IView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the ConsoleView class to make sure the output is as expected. Numbers should be down the
 * left hand side for the beats and pitches should be across the top in increasing pitch.
 */
public class ConsoleViewTest {

  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model;
  private IView view;
  private StringBuffer out;

  /**
   * Sets up the StringBuffer, model, and view before every test.
   */
  @Before
  public void setup() {
    out = new StringBuffer();
    model = new MusicEditorModelImpl();
    view = new ConsoleView(out);
  }

  /**
   * Tests the getEditorState method when there are no notes.
   */
  @Test
  public void testGetEditorStateNoNotes() {
    StringBuffer out = new StringBuffer();
    view.display(new MusicEditorModelImplReadOnly(model));
    assertEquals("", out.toString());
  }

  /**
   * Tests the getEditorState for the pitches of notes.
   */
  @Test
  public void testGetEditorStatePitches() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 2, 2);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.D_SHARP, 3), 3, 3, 4, 5);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.E, 2), 5, 10, 9, 1);
    MusicNote n4 = new MusicNote(new Pitch(NoteName.F_SHARP, 7), 9, 15, 4, 2);
    MusicNote n5 = new MusicNote(new Pitch(NoteName.B, 10), 14, 0, 9, 1);

    model.addNote(n1);
    model.addNote(n2);
    model.addNote(n3);
    model.addNote(n4);
    model.addNote(n5);

    Pitch p = new Pitch(NoteName.E, 2);
    int j = p.getDifference(new Pitch(NoteName.B, 10));
    StringBuilder builder = new StringBuilder("  ");
    for (int i = 0; i < j + 1; i++) {
      if (p.toString().length() == 2) {
        builder.append("  " + p.toString() + " ");
      }
      else if (p.toString().length() == 3) {
        builder.append(" " + p.toString() + " ");
      }
      else {
        builder.append(" " + p.toString());
      }

      if (i != j) {
        p = p.nextPitch();
      }
    }
    builder.append("\n");

    view.display(new MusicEditorModelImplReadOnly(model));

    assertEquals(out.toString().substring(0, 523), builder.toString());
  }

  /**
   * Tests the getEditorState method for the numbering.
   */
  @Test
  public void testGetEditorStateNumbering() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 3, 4);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.D_SHARP, 3), 3, 3, 6, 5);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.E, 2), 5, 10, 3, 2);
    MusicNote n4 = new MusicNote(new Pitch(NoteName.F_SHARP, 7), 9, 15, 1, 2);
    MusicNote n5 = new MusicNote(new Pitch(NoteName.B, 10), 14, 0, 9, 8);

    model.addNote(n1);
    model.addNote(n2);
    model.addNote(n3);
    model.addNote(n4);
    model.addNote(n5);

    view.display(new MusicEditorModelImplReadOnly(model));

    for (int i = 0; i < 24; i++) {
      if (i < 10) {
        assertTrue(out.toString().contains("\n " + i + " "));
      }
      else {
        assertTrue(out.toString().contains("\n" + i + " "));
      }
    }

    assertFalse(out.toString().contains("24"));

    this.out = new StringBuffer();
    this.view = new ConsoleView(this.out);

    model.removeNote(n4);

    view.display(new MusicEditorModelImplReadOnly(model));
    for (int i = 0; i < 15; i++) {
      if (i < 10) {
        assertTrue(out.toString().contains("\n " + i + " "));
      }
      else {
        assertTrue(out.toString().contains("\n" + i + " "));
      }
    }
    assertFalse(out.toString().contains("15"));
  }

  /**
   * Tests the getEditorState for a one beat note.
   */
  @Test
  public void testGetEditorStateOneBeatNote() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 1, 0, 1, 2);
    model.addNote(n1);

    view.display(new MusicEditorModelImplReadOnly(model));

    String result = "   C5 \n0  X  \n";

    assertEquals(result, out.toString());
  }

  /**
   * Tests the getEditorState for a single note.
   */
  @Test
  public void testGetEditorStateSingleNote() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 10, 10);
    model.addNote(n1);

    String result = "    C5 \n"
            +
            " 0     \n"
            +
            " 1  X  \n"
            +
            " 2  |  \n"
            +
            " 3  |  \n"
            +
            " 4  |  \n"
            +
            " 5  |  \n"
            +
            " 6  |  \n"
            +
            " 7  |  \n"
            +
            " 8  |  \n"
            +
            " 9  |  \n"
            +
            "10  |  \n";

    view.display(new MusicEditorModelImplReadOnly(model));
    assertEquals(result, out.toString());
  }

  /**
   * Tests the getEditorState for an overlapping note.
   */
  @Test
  public void testGetEditorStateOverlappingNote() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 4, 3);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.C, 5), 10, 2, 2, 1);
    model.addNote(n1);
    model.addNote(n2);

    String result = "    C5 \n"
            +
            " 0     \n"
            +
            " 1  X  \n"
            +
            " 2  X  \n"
            +
            " 3  |  \n"
            +
            " 4  |  \n"
            +
            " 5  |  \n"
            +
            " 6  |  \n"
            +
            " 7  |  \n"
            +
            " 8  |  \n"
            +
            " 9  |  \n"
            +
            "10  |  \n"
            +
            "11  |  \n";

    view.display(new MusicEditorModelImplReadOnly(model));
    assertEquals(result, out.toString());
  }

  /**
   * Tests the getEditorState for two notes that are the same.
   */
  @Test
  public void testGetEditorStateSameNote() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 5, 3);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 16, 4);
    model.addNote(n1);
    model.addNote(n2);

    String result = "    C5 \n"
            +
            " 0     \n"
            +
            " 1  X  \n"
            +
            " 2  |  \n"
            +
            " 3  |  \n"
            +
            " 4  |  \n"
            +
            " 5  |  \n"
            +
            " 6  |  \n"
            +
            " 7  |  \n"
            +
            " 8  |  \n"
            +
            " 9  |  \n"
            +
            "10  |  \n";

    view.display(new MusicEditorModelImplReadOnly(model));

    assertEquals(result, out.toString());
  }

  /**
   * Tests the getEditorState for two notes that are a different pitch.
   */
  @Test
  public void testGetEditorStateDifferentPitch() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 3, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.A_SHARP, 4), 5, 7, 1, 3);
    model.addNote(n1);
    model.addNote(n2);

    String result = "   A#4   B4   C5 \n"
            +
            " 0               \n"
            +
            " 1            X  \n"
            +
            " 2            |  \n"
            +
            " 3            |  \n"
            +
            " 4            |  \n"
            +
            " 5            |  \n"
            +
            " 6            |  \n"
            +
            " 7  X         |  \n"
            +
            " 8  |         |  \n"
            +
            " 9  |         |  \n"
            +
            "10  |         |  \n"
            +
            "11  |            \n";

    view.display(new MusicEditorModelImplReadOnly(model));

    assertEquals(result, out.toString());
  }

  /**
   * Test getEditorState for different number of digits.
   */
  @Test
  public void testGetEditorStateDigits() {
    model.addNote(new MusicNote(new Pitch(NoteName.A, 4), 4, 1, 3, 2));
    view.display(new MusicEditorModelImplReadOnly(model));
    assertTrue(out.toString().startsWith(" "));
    out = new StringBuffer();
    view = new ConsoleView(out);

    model.addNote(new MusicNote(new Pitch(NoteName.A, 4), 4, 10, 2, 1));
    view.display(new MusicEditorModelImplReadOnly(model));
    assertTrue(out.toString().startsWith("  "));
    out = new StringBuffer();
    view = new ConsoleView(out);

    model.addNote(new MusicNote(new Pitch(NoteName.A, 4), 4, 100, 4, 7));
    view.display(new MusicEditorModelImplReadOnly(model));
    assertTrue(out.toString().startsWith("   "));
    out = new StringBuffer();
    view = new ConsoleView(out);

    model.addNote(new MusicNote(new Pitch(NoteName.A, 4), 4, 1000, 5, 50));
    view.display(new MusicEditorModelImplReadOnly(model));
    assertTrue(out.toString().startsWith("    "));
  }

  /**
   * Tests the getEditorState for two notes that are not playing at the same time.
   */
  @Test
  public void testGetEditorStateDifferentPitchNotSameTime() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 4, 1, 3, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.A_SHARP, 4), 5, 7, 1, 3);
    model.addNote(n1);
    model.addNote(n2);

    String result = "   A#4   B4   C5 \n"
            +
            " 0               \n"
            +
            " 1            X  \n"
            +
            " 2            |  \n"
            +
            " 3            |  \n"
            +
            " 4            |  \n"
            +
            " 5               \n"
            +
            " 6               \n"
            +
            " 7  X            \n"
            +
            " 8  |            \n"
            +
            " 9  |            \n"
            +
            "10  |            \n"
            +
            "11  |            \n";

    view.display(new MusicEditorModelImplReadOnly(model));

    assertEquals(result, out.toString());
  }
}
