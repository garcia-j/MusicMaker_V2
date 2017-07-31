package cs3500.music.model;

/**
 * Represents a pitch. A pitch consists of a note name and an octave where the octave represenation
 * is up to the implementation.
 */
public interface IPitch {

  /**
   * Gets the Pitch after this Pitch.
   *
   * @return The next Pitch
   */
  IPitch nextPitch();

  /**
   * Gets the distance between this Pitch and the passed in Pitch.
   *
   * @param pitch The Pitch to get the distance between
   * @return The distance between the two pitches
   */
  int getDifference(IPitch pitch);

  /**
   * Get the NoteName of this Pitch.
   * @return The NoteName of this Pitch
   */
  NoteName getNoteName();

  /**
   * Gets the octave of this Pitch.
   * @return The octave of this Pitch
   */
  int getOctave();

  /**
   * Returns a String representing this Pitch (the NoteName in String form followed by the octave).
   *
   * @return The String representing this Pitch
   */
  String toString();

  /**
   * Compares this Pitch to the passed in Pitch. Returns a negative number if this Pitch is lower
   * than that Pitch, 0 if the Pitches are the same, and a positive number if this Pitch is higher
   * than that Pitch.
   *
   * @param that The Pitch to compare to
   * @return The comparison result
   */
  int compareTo(IPitch that);

  /**
   * Converts a Midi tone into a Pitch String Representation
   * @param tone   The midi tone to be converted.
   * @return pitch The equivalent pitch.
   */
  static String midiToPitch(int tone) {
    if (tone < 0 || tone > 127) {
      throw new IllegalArgumentException("Invalid tone");
    }
    int octave = tone / 12 - 1;
    int noteName = tone % 12;
    NoteName name = NoteName.values()[noteName];
    return new String(name.toString() + octave);
  }
}
