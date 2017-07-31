package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import cs3500.music.model.NoteName;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the NoteName class.
 */
public class NoteNameTest {

  private NoteName n1;
  private NoteName n2;
  private NoteName n3;

  /**
   * Initializes the three note names before every test.
   */
  @Before
  public void setup() {
    n1 = NoteName.A;
    n2 = NoteName.C_SHARP;
    n3 = NoteName.B;
  }

  /**
   * Test getNoteName.
   */
  @Test
  public void testGetNoteName() {
    assertEquals("A", n1.getNoteName());
    assertEquals("C#", n2.getNoteName());
    assertEquals("B", n3.getNoteName());
  }

  /**
   * Test getNextNoteName.
   */
  @Test
  public void testGetNextNoteName() {
    assertEquals(NoteName.A_SHARP, n1.getNextNoteName());
    assertEquals(NoteName.D, n2.getNextNoteName());
    assertEquals(NoteName.C, n3.getNextNoteName());
  }

}