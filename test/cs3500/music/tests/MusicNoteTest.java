package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import cs3500.music.model.MusicNote;
import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for MusicNote.
 */
public class MusicNoteTest {

  private MusicNote note1;
  private MusicNote note2;
  private MusicNote note3;

  /**
   * Initializes the three notes before every test.
   */
  @Before
  public void setup() {
    note1 = new MusicNote(new Pitch(NoteName.C, 1), 10, 0, 1, 1);
    note2 = new MusicNote(new Pitch(NoteName.A_SHARP, 10), 20, 10, 2, 1);
    note3 = new MusicNote(new Pitch(NoteName.B, 6), 5, 100, 3, 1);
  }

  /**
   * Verify an exception is thrown if the startBeat is negative.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeStartBeat() {
    MusicNote n = new MusicNote(new Pitch(NoteName.C_SHARP, 10), 10, -1, 1, 1);
  }

  /**
   * Test getPitch.
   */
  @Test
  public void testGetPitch() {
    assertEquals(new Pitch(NoteName.C, 1), note1.getPitch());
    assertEquals(new Pitch(NoteName.A_SHARP, 10), note2.getPitch());
    assertEquals(new Pitch(NoteName.B, 6), note3.getPitch());
  }

  /**
   * Test getDuration.
   */
  @Test
  public void testGetDuration() {
    assertEquals(10, note1.getDuration());
    assertEquals(20, note2.getDuration());
    assertEquals(5, note3.getDuration());
  }

  /**
   * Test getStartBeat.
   */
  @Test
  public void testGetStartBeat() {
    assertEquals(0, note1.getStartBeat());
    assertEquals(10, note2.getStartBeat());
    assertEquals(100, note3.getStartBeat());
  }

  /**
   * Test equals.
   */
  @Test
  public void testEquals() {
    MusicNote n = new MusicNote(new Pitch(NoteName.C, 1), 10, 0, 1, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.C, 1), 10, 0, 1, 1);
    assertEquals(note1, note1);
    assertEquals(n, note1);
    assertEquals(n2, note1);
    assertEquals(n, n2);
    assertEquals(note1, n);
    assertNotEquals(note1, note2);
    assertNotEquals(note2, note3);
  }

  /**
   * Test hashCode.
   */
  @Test
  public void testHashCode() {
    MusicNote n = new MusicNote(new Pitch(NoteName.C, 1), 10, 0, 1, 1);
    MusicNote n2 = new MusicNote(new Pitch(NoteName.C, 1), 10, 0, 1, 1);
    assertEquals(note1.hashCode(), note1.hashCode());
    assertEquals(n.hashCode(), note1.hashCode());
    assertEquals(n2.hashCode(), note1.hashCode());
    assertEquals(n.hashCode(), n2.hashCode());
    assertEquals(note1.hashCode(), n.hashCode());
    assertNotEquals(note1.hashCode(), note2.hashCode());
    assertNotEquals(note2.hashCode(), note3.hashCode());
  }

}