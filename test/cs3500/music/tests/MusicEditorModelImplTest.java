package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.model.MusicEditorModelImplReadOnly;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.MusicNote;
import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Tests for the MusicEditorModelImpl.
 */
public class MusicEditorModelImplTest {

  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model1;
  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model2;
  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model3;

  /**
   * Set the models back to their original states.
   */
  @Before
  public void setup() {
    model1 = new MusicEditorModelImpl();
    model2 = new MusicEditorModelImpl();
    model3 = new MusicEditorModelImplReadOnly();
  }

  /**
   * Tests the getTempo method.
   */
  @Test
  public void testGetTempo() {
    assertEquals(10000, model1.getTempo());
  }

  /**
   * Tests the getBeatsPerMinute method.
   */
  @Test
  public void testGetBeatsPerMinute() {
    assertEquals(4, model1.getBeatsPerMeasure());
  }

  /**
   * Test exception thrown from tempo that is not positive.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonPositiveTempo() {
    model1.setTempo(0);
  }

  /**
   * Test setTempo.
   */
  @Test
  public void testSetTempo() {
    model1.setTempo(1);
    assertEquals(1, model1.getTempo());

    model1.setTempo(50);
    assertEquals(50, model1.getTempo());
  }

  /**
   * Test exception thrown from beats per measure that is not positive.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonPositiveBeatsPerMeasure() {
    model1.setBeatsPerMeasure(0);
  }

  /**
   * Test setBeatsPerMeasure.
   */
  @Test
  public void testSetBeatsPerMeasure() {
    model1.setBeatsPerMeasure(1);
    assertEquals(1, model1.getBeatsPerMeasure());

    model1.setBeatsPerMeasure(3);
    assertEquals(3, model1.getBeatsPerMeasure());
  }

  /**
   * Tests that a new piece of music is created when newPiece is called.
   */
  @Test
  public void testNewPiece() {
    model1.addNote(new MusicNote(new Pitch(NoteName.A_SHARP, 10), 10, 10, 1, 1));
    model1.addNote(new MusicNote(new Pitch(NoteName.B, 6), 23, 1, 2, 3));
    model1.addNote(new MusicNote(new Pitch(NoteName.C, 1), 5, 0, 4, 1));
    model1.addNote(new MusicNote(new Pitch(NoteName.F_SHARP, 4), 20, 12, 5, 10));

    model1.setBeatsPerMeasure(3);
    model1.setTempo(100);

    assertEquals(32 ,model1.getNotes().size());

    model1.newPiece();
    assertEquals(0, model1.getNotes().size());
    assertEquals(60, model1.getTempo());
    assertEquals(4, model1.getBeatsPerMeasure());
  }

  /**
   * Test getNotes with an empty music piece.
   */
  @Test
  public void testGetNotesEmpty() {
    assertEquals(new TreeMap<Integer, Set<MusicNote>>(), model1.getNotes());
  }

  /**
   * Tests getNotes.
   */
  @Test
  public void testGetNotes() {
    assertEquals(0, model1.getNotes().size());

    MusicNote n = new MusicNote(new Pitch(NoteName.A_SHARP, 10), 10, 10, 2, 2);
    model1.addNote(n);

    assertEquals(10, model1.getNotes().size());

    for (int i = 10; i < 20; i++) {
      assertTrue(model1.getNotes().get(i).contains(n));
    }
  }

  /**
   * Test that an exception is thrown when a null note is added.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullNote() {
    model1.addNote(null);
  }

  /**
   * Test that a note can be added successfully.
   */
  @Test
  public void testAddNote() {
    MusicNote n = new MusicNote(new Pitch(NoteName.A_SHARP, 10), 10, 10, 1, 5);
    model1.addNote(n);

    assertEquals(10, model1.getNotes().size());

    for (int i = 10; i < 20; i++) {
      assertTrue(model1.getNotes().get(i).contains(n));
    }

    n = new MusicNote(new Pitch(NoteName.C, 3), 15, 0, 4, 3);
    model1.addNote(n);
    assertEquals(20, model1.getNotes().size());
    for (int i = 0; i < 15; i++) {
      assertTrue(model1.getNotes().get(i).contains(n));
    }

    MusicNote n2 = new MusicNote(new Pitch(NoteName.C, 3), 15, 1, 3, 2);
    model1.addNote(n2);
    assertEquals(20, model1.getNotes().size());
    for (int i = 1; i < 15; i++) {
      assertTrue(model1.getNotes().get(i).contains(n));
      assertTrue(model1.getNotes().get(i).contains(n2));
    }
  }

  /**
   * Verify that a duplicate note doesn't add a new note.
   */
  @Test
  public void testAddDuplicateNote() {
    MusicNote n = new MusicNote(new Pitch(NoteName.C, 3), 15, 0, 4, 4);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.C, 3), 15, 0, 4, 4);

    model1.addNote(n);
    model1.addNote(n2);
    for (Integer i : model1.getNotes().keySet()) {
      assertTrue(model1.getNotes().get(i).size() == 1);
    }
  }

  /**
   * Test that an exception is thrown when a null note is removed.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNullNote() {
    model1.removeNote(null);
  }

  /**
   * Test that an exception is thrown if a note is removed from an empty editor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveEmptyEditor() {
    model1.removeNote(new MusicNote(new Pitch(NoteName.C, 5), 5, 5, 5, 6));
  }

  /**
   * Test than an exception is thrown if a note is removed that does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonexistentNote() {
    model1.addNote(new MusicNote(new Pitch(NoteName.C, 5), 5, 4, 6, 5));
    model1.removeNote(new MusicNote(new Pitch(NoteName.C, 5), 5, 5, 3, 2));
  }

  /**
   * Test removing a note.
   */
  @Test
  public void testRemoveNote() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.C, 5), 10, 1, 4, 3);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.D_SHARP, 3), 3, 3, 2, 1);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.E, 2), 5, 10, 0, 9);
    MusicNote n4 = new MusicNote(new Pitch(NoteName.F_SHARP, 7), 9, 15, 0, 8);
    MusicNote n5 = new MusicNote(new Pitch(NoteName.B, 8), 14, 0, 2, 4);

    model1.addNote(n1);
    model1.addNote(n2);
    model1.addNote(n3);
    model1.addNote(n4);
    model1.addNote(n5);
    assertEquals(24, model1.getNotes().size());

    model1.removeNote(n4);
    assertEquals(15, model1.getNotes().size());
    for (int i = 15; i < 24; i++) {
      if (model1.getNotes().containsKey(i)) {
        assertFalse(model1.getNotes().get(i).contains(n4));
      }
    }

    model1.removeNote(n5);
    assertEquals(14, model1.getNotes().size());
    for (int i = 0; i < 14; i++) {
      if (model1.getNotes().containsKey(i)) {
        assertFalse(model1.getNotes().get(i).contains(n5));
      }
    }

    model1.removeNote(n3);
    assertEquals(10, model1.getNotes().size());
    for (int i = 10; i < 15; i++) {
      if (model1.getNotes().containsKey(i)) {
        assertFalse(model1.getNotes().get(i).contains(n3));
      }
    }

    model1.removeNote(n1);
    assertEquals(3, model1.getNotes().size());
    for (int i = 1; i < 11; i++) {
      if (model1.getNotes().containsKey(i)) {
        assertFalse(model1.getNotes().get(i).contains(n1));
      }
    }

    model1.removeNote(n2);
    assertEquals(0, model1.getNotes().size());
  }

  /**
   * Test that an exception is thrown when a null note is being replaced.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testReplaceNullNote() {
    model1.replaceNote(null, new MusicNote(new Pitch(NoteName.A, 1), 10, 10, 5, 3));
  }

  /**
   * Test that an exception is thrown when a null note is doing the replacing.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullNoteDoingReplacing() {
    model1.replaceNote(new MusicNote(new Pitch(NoteName.A, 1), 10, 10, 2, 3), null);
  }

  /**
   * Test that replacing an old note that does not exist throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testReplaceNoteNotExist() {
    model1.replaceNote(new MusicNote(new Pitch(NoteName.A, 1), 10, 10, 4, 3),
            new MusicNote(new Pitch(NoteName.C, 2), 10, 10, 5, 4));
  }

  /**
   * Test that replacing an old note that does note exist does not add a new note.
   */
  @Test
  public void testReplaceNoteDoesNotAdd() {
    try {
      model1.replaceNote(new MusicNote(new Pitch(NoteName.A, 1), 10, 10, 3, 2),
              new MusicNote(new Pitch(NoteName.C, 2), 10, 10, 3, 1));
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      assertEquals(0, model1.getNotes().size());
    }
  }

  /**
   * Test that replacing an old note works.
   */
  @Test
  public void testReplaceNote() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.A, 1), 10, 10, 2, 2);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.C, 2), 11, 5, 0, 4);

    model1.addNote(n1);
    model1.replaceNote(n1, n2);

    for (int i = 5; i < 16; i++) {
      assertTrue(model1.getNotes().containsKey(i));
    }

    for (int i = 16; i < 20; i++) {
      assertFalse(model1.getNotes().containsKey(i));
    }

    for (Integer i : model1.getNotes().keySet()) {
      assertTrue(model1.getNotes().get(i).contains(n2));
    }
  }

  /**
   * Test that an exception is thrown when a null editor is appended.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAppendNullEditor() {
    model1.append(null);
  }

  /**
   * Test append with two empty editors.
   */
  @Test
  public void testAppendTwoEmpty() {
    model1.append(model2);

    assertEquals(0, model1.getNotes().size());
  }

  /**
   * Test append with the first editor empty.
   */
  @Test
  public void testAppendFirstEmpty() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.A_SHARP, 5), 12, 5, 4, 3);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.B, 3), 10, 2, 4, 4);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.F, 6), 20, 20, 4, 10);

    model2.addNote(n1);
    model2.addNote(n2);
    model2.addNote(n3);
    model1.append(model2);

    assertEquals(model1.getNotes(), model2.getNotes());
  }

  /**
   * Test append with the second editor empty.
   */
  @Test
  public void testAppendSecondEmpty() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.A_SHARP, 5), 12, 5, 3, 2);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.B, 3), 10, 2, 3, 6);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.F, 6), 20, 20, 3, 7);

    model2.addNote(n1);
    model2.addNote(n2);
    model2.addNote(n3);

    MusicEditorOperations model3 = new MusicEditorModelImpl();
    model3.addNote(n1);
    model3.addNote(n2);
    model3.addNote(n3);

    model2.append(model1);

    assertEquals(model3.getNotes(), model2.getNotes());
  }

  /**
   * Test append with two editors that have notes.
   */
  @Test
  public void testAppendWithNotes() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.A_SHARP, 5), 12, 5, 2, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.B, 3), 10, 2, 3, 1);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.F, 6), 20, 20, 2, 10);
    model1.addNote(n1);
    model1.addNote(n2);
    model1.addNote(n3);

    MusicNote n4 = new MusicNote(new Pitch(NoteName.C, 1), 13, 0, 3, 5);
    MusicNote n5 = new MusicNote(new Pitch(NoteName.E, 4), 19, 3, 5, 4);
    MusicNote n6 = new MusicNote(new Pitch(NoteName.C_SHARP, 6), 25, 15, 3, 5);
    model2.addNote(n4);
    model2.addNote(n5);
    model2.addNote(n6);

    MusicNote n4After = new MusicNote(new Pitch(NoteName.C, 1), 13, 40, 3, 5);
    MusicNote n5After = new MusicNote(new Pitch(NoteName.E, 4), 19, 43, 5, 4);
    MusicNote n6After = new MusicNote(new Pitch(NoteName.C_SHARP, 6), 25, 55, 3, 5);
    model1.append(model2);

    for (Integer i : model1.getNotes().keySet()) {
      if (i > 4 && i < 17) {
        assertTrue(model1.getNotes().get(i).contains(n1));
      }
      if (i > 1 && i < 12) {
        assertTrue(model1.getNotes().get(i).contains(n2));
      }
      if (i > 19 && i < 40) {
        assertTrue(model1.getNotes().get(i).contains(n3));
      }
      if (i > 39 && i < 53) {
        assertTrue(model1.getNotes().get(i).contains(n4After));
      }
      if (i > 42 && i < 62) {
        assertTrue(model1.getNotes().get(i).contains(n5After));
      }
      if (i > 54 && i < 80) {
        assertTrue(model1.getNotes().get(i).contains(n6After));
      }
    }
  }

  /**
   * Test that an exception is thrown when a null editor is combined.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCombineNullEditor() {
    model1.combine(null);
  }

  /**
   * Test combine with two empty editors.
   */
  @Test
  public void testCombineTwoEmpty() {
    model1.combine(model2);
    assertEquals(0, model1.getNotes().size());
  }

  /**
   * Test combine with the first editor empty.
   */
  @Test
  public void testCombineFirstEmpty() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.A_SHARP, 5), 12, 5, 1, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.B, 3), 10, 2, 1, 4);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.F, 6), 20, 20, 1, 9);

    model1.addNote(n1);
    model1.addNote(n2);
    model1.addNote(n3);
    model2.combine(model1);

    assertEquals(model1.getNotes(), model2.getNotes());
  }

  /**
   * Test combine with the second editor empty.
   */
  @Test
  public void testCombineSecondEmpty() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.A_SHARP, 5), 12, 5, 1, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.B, 3), 10, 2, 1, 3);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.F, 6), 20, 20, 1, 6);
    model1.addNote(n1);
    model1.addNote(n2);
    model1.addNote(n3);

    MusicEditorOperations model3 = new MusicEditorModelImpl();
    model3.addNote(n1);
    model3.addNote(n2);
    model3.addNote(n3);
    model1.combine(model2);

    assertEquals(model1.getNotes(), model3.getNotes());
  }

  /**
   * Test combine with notes.
   */
  @Test
  public void testCombineNotes() {
    MusicNote n1 = new MusicNote(new Pitch(NoteName.A_SHARP, 5), 12, 5, 1, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.B, 3), 10, 2, 1, 9);
    MusicNote n3 = new MusicNote(new Pitch(NoteName.F, 6), 20, 20, 1, 4);
    model1.addNote(n1);
    model1.addNote(n2);
    model1.addNote(n3);

    MusicNote n4 = new MusicNote(new Pitch(NoteName.C, 1), 13, 0, 5, 4);
    MusicNote n5 = new MusicNote(new Pitch(NoteName.E, 4), 19, 3, 5, 3);
    MusicNote n6 = new MusicNote(new Pitch(NoteName.C_SHARP, 6), 25, 15, 5, 9);
    model2.addNote(n4);
    model2.addNote(n5);
    model2.addNote(n6);
    model1.combine(model2);

    for (Integer i : model1.getNotes().keySet()) {
      if (i > 4 && i < 17) {
        assertTrue(model1.getNotes().get(i).contains(n1));
      }
      if (i > 1 && i < 12) {
        assertTrue(model1.getNotes().get(i).contains(n2));
      }
      if (i > 19 && i < 40) {
        assertTrue(model1.getNotes().get(i).contains(n3));
      }
      if (i > -1 && i < 13) {
        assertTrue(model1.getNotes().get(i).contains(n4));
      }
      if (i > 2 && i < 22) {
        assertTrue(model1.getNotes().get(i).contains(n5));
      }
      if (i > 14 && i < 40) {
        assertTrue(model1.getNotes().get(i).contains(n6));
      }
    }
  }

  /**
   * Test that an exception is thrown when a negative beat is given to get.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetNotesAtBeatNegative() {
    model1.getNotesAtBeat(-1);
  }

  /**
   * Test that an empty set is returned if there are no notes at that beat.
   */
  @Test
  public void testGetNotesAtBeatEmpty() {
    model1.addNote(new MusicNote(new Pitch(NoteName.C_SHARP, 3), 10, 10, 1, 4));
    assertEquals(0, model1.getNotesAtBeat(2).size());
  }

  /**
   * Test getNotesAtBeat.
   */
  @Test
  public void testGetNotesAtBeat() {
    model1.addNote(new MusicNote(new Pitch(NoteName.B, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.A_SHARP, 6), 2, 3, 1, 5));

    assertEquals(2, model1.getNotesAtBeat(4).size());
  }

  /**
   * Test getting the lowest pitch.
   */
  @Test
  public void testGetLowPitch() {
    model1.addNote(new MusicNote(new Pitch(NoteName.F, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.C, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.D, 4), 20, 4, 1, 4));

    assertEquals(new Pitch(NoteName.C, 4), model1.getLowPitch());
  }

  /**
   * Test getting the highest pitch.
   */
  @Test
  public void testGetHighPitch() {
    model1.addNote(new MusicNote(new Pitch(NoteName.F, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.C, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.D, 4), 20, 4, 1, 4));

    assertEquals(new Pitch(NoteName.F, 4), model1.getHighPitch());
  }

  /**
   * Test getting null low pitch.
   */
  @Test
  public void testGetNullLowPitch() {
    assertNull(model1.getLowPitch());
  }

  /**
   * Test getting null high pitch.
   */
  @Test
  public void testGetNullHighPitch() {
    assertNull(model1.getHighPitch());
  }

  /*
   *************************************************************************************************
   * Tests for Read Only Model
   *************************************************************************************************
   */

  /**
   * Error is thrown for setTempo.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelSetTempo() {
    model3.setTempo(5);
  }

  /**
   * Error is thrown for setBeatsPerMeasure.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelSetBeatsPerMeasure() {
    model3.setBeatsPerMeasure(10);
  }

  /**
   * Error is thrown for newPiece.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelNewPiece() {
    model3.newPiece();
  }

  /**
   * Error is thrown for addNote.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelAddNote() {
    model3.addNote(new MusicNote(new Pitch(NoteName.E, 3), 1, 3, 4, 5));
  }

  /**
   * Error is thrown for removeNote.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelRemoveNote() {
    model3.removeNote(new MusicNote(new Pitch(NoteName.D, 5), 7, 4, 1, 1));
  }

  /**
   * Error is thrown for replaceNote.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelReplaceNote() {
    model3.replaceNote(new MusicNote(new Pitch(NoteName.D, 5), 7, 4, 1, 1),
            new MusicNote(new Pitch(NoteName.F_SHARP, 5), 7, 4, 1, 1));
  }

  /**
   * Error is thrown for append.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelAppend() {
    model3.append(model1);
  }

  /**
   * Error is thrown for combine.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testReadOnlyModelCombine() {
    model3.combine(model1);
  }

  /**
   * Test getTempo.
   */
  @Test
  public void testReadOnlyGetTempo() {
    model1.setTempo(100);
    model3 = new MusicEditorModelImplReadOnly(model1);
    assertEquals(100, model3.getTempo());
  }

  /**
   * Test getBeatsPerMeasure.
   */
  @Test
  public void testReadOnlyGetBeatsPerMeasure() {
    model1.setBeatsPerMeasure(3);
    model3 = new MusicEditorModelImplReadOnly(model1);
    assertEquals(3, model3.getBeatsPerMeasure());
  }

  /**
   * Test getNotes.
   */
  @Test
  public void testReadOnlyGetNotes() {
    model1.addNote(new MusicNote(new Pitch(NoteName.D, 5), 7, 4, 1, 1));
    model3 = new MusicEditorModelImplReadOnly(model1);
    Map<Integer, Set<MusicNote>> map = new TreeMap<Integer, Set<MusicNote>>();
    for (int i = 4; i < 11; i++) {
      map.put(i, new HashSet<MusicNote>());
      map.get(i).add(new MusicNote(new Pitch(NoteName.D, 5), 7, 4, 1, 1));
    }

    assertEquals(map, model3.getNotes());
  }

  /**
   * Test getNotesAtBeat.
   */
  @Test
  public void testReadOnlyGetNotesAtBeat() {
    model1.addNote(new MusicNote(new Pitch(NoteName.D, 5), 7, 4, 1, 1));
    model3 = new MusicEditorModelImplReadOnly(model1);
    Set<MusicNote> set = new HashSet<MusicNote>();
    set.add(new MusicNote(new Pitch(NoteName.D, 5), 7, 4, 1, 1));

    assertEquals(set, model3.getNotesAtBeat(5));
  }

  /**
   * Test getting the lowest pitch.
   */
  @Test
  public void testReadOnlyGetLowPitch() {
    model1.addNote(new MusicNote(new Pitch(NoteName.F, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.C, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.D, 4), 20, 4, 1, 4));
    model3 = new MusicEditorModelImplReadOnly(model1);
    assertEquals(new Pitch(NoteName.C, 4), model3.getLowPitch());
  }

  /**
   * Test getting the highest pitch.
   */
  @Test
  public void testReadOnlyGetHighPitch() {
    model1.addNote(new MusicNote(new Pitch(NoteName.F, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.C, 4), 20, 4, 1, 4));
    model1.addNote(new MusicNote(new Pitch(NoteName.D, 4), 20, 4, 1, 4));
    model3 = new MusicEditorModelImplReadOnly(model1);
    assertEquals(new Pitch(NoteName.F, 4), model3.getHighPitch());
  }

  /**
   * Test getting null low pitch.
   */
  @Test
  public void testReadOnlyGetNullLowPitch() {
    model3 = new MusicEditorModelImplReadOnly(model1);
    assertNull(model3.getLowPitch());
  }

  /**
   * Test getting null high pitch.
   */
  @Test
  public void testReadOnlyGetNullHighPitch() {
    model3 = new MusicEditorModelImplReadOnly(model1);
    assertNull(model3.getHighPitch());
  }
}