package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;

import static cs3500.music.model.IPitch.midiToPitch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * To test the Pitch class.
 */
public class PitchTest {

  private Pitch p1;
  private Pitch p2;
  private Pitch p3;

  /**
   * Initializes the three pitches before every test.
   */
  @Before
  public void setup() {
    p1 = new Pitch(NoteName.C, 1);
    p2 = new Pitch(NoteName.G, 5);
    p3 = new Pitch(NoteName.F_SHARP, 10);
  }

  /**
   * Test getNoteName.
   */
  @Test
  public void testGetNoteName() {
    assertEquals(NoteName.C, p1.getNoteName());
    assertEquals(NoteName.G, p2.getNoteName());
    assertEquals(NoteName.F_SHARP, p3.getNoteName());
  }

  /**
   * Test getOctave.
   */
  @Test
  public void testGetOctavd() {
    assertEquals(1, p1.getOctave());
    assertEquals(5, p2.getOctave());
    assertEquals(10, p3.getOctave());
  }

  /**
   * Test toString.
   */
  @Test
  public void testToString() {
    assertEquals("C1", p1.toString());
    assertEquals("G5", p2.toString());
    assertEquals("F#10", p3.toString());
  }

  /**
   * Test nextNoteName.
   */
  @Test
  public void testNextNoteName() {
    assertEquals(new Pitch(NoteName.C_SHARP, 1), p1.nextPitch());
    assertEquals(new Pitch(NoteName.G_SHARP, 5), p2.nextPitch());
    assertEquals(new Pitch(NoteName.G, 10), p3.nextPitch());
    assertEquals(new Pitch(NoteName.C, 3), new Pitch(NoteName.B, 2).nextPitch());
  }

  /**
   * Test getDifference.
   */
  @Test
  public void testGetDifference() {
    assertEquals(1, p1.getDifference(new Pitch(NoteName.C_SHARP, 1)));
    assertEquals(55, p1.getDifference(p2));
    assertEquals(55, p2.getDifference(p1));
    assertEquals(59, p2.getDifference(p3));
    assertEquals(114, p1.getDifference(p3));
  }

  /**
   * Test compareTo.
   */
  @Test
  public void testCompareTo() {
    assertTrue(p1.compareTo(p2) < 0);
    assertTrue(p2.compareTo(p1) > 0);
    assertTrue(p2.compareTo(p3) < 0);
    assertTrue(p3.compareTo(p2) > 0);
    assertTrue(p1.compareTo(p1) == 0);
    assertTrue(p1.compareTo(new Pitch(p1)) == 0);
  }

  /**
   * Test equals.
   */
  @Test
  public void testEquals() {
    Pitch p = new Pitch(NoteName.C, 1);
    assertTrue(p1.equals(p1));
    assertTrue(p1.equals(p));
    assertTrue(p.equals(p1));
    assertFalse(p1.equals(p2));
    assertFalse(p2.equals(p3));
  }

  /**
   * Test hashCode.
   */
  public void testHashCode() {
    Pitch p = new Pitch(NoteName.C, 1);
    assertEquals(p1.hashCode(), p1.hashCode());
    assertEquals(p1.hashCode(), p.hashCode());
    assertEquals(p.hashCode(), p1.hashCode());
    assertNotEquals(p1.hashCode(), p2.hashCode());
    assertNotEquals(p2.hashCode(), p3.hashCode());
  }

  /**
   * Tests for the midiToPitch helper function.
   */
  @Test
  public void testMidiToPitch() {
    assertEquals("A5", midiToPitch(81));
    assertEquals("B5", midiToPitch(83));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMidiLowException() {
    String n = midiToPitch(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMidiHighException() {
    String n = midiToPitch(128);
  }
}