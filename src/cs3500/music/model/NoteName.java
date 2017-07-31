package cs3500.music.model;

/**
 * To represent a note name. A note name is from C to B.
 */
public enum NoteName {
  C("C"), C_SHARP("C#"), D("D"), D_SHARP("D#"), E("E"), F("F"), F_SHARP("F#"), G("G"),
  G_SHARP("G#"), A("A"), A_SHARP("A#"), B("B");

  private final String noteName;

  /**
   * Constructor for Tone.
   *
   * @param noteName The String symbol of the pitch
   */
  NoteName(String noteName) {
    this.noteName = noteName;
  }

  /**
   * Gets the string representing this note name.
   * @return The string that this note name represents
   */
  public String getNoteName() {
    return noteName;
  }

  /**
   * Gets the next note name in the list (if the note is b, circles around to c).
   *
   * @return The next note name
   */
  public NoteName getNextNoteName() {
    return values()[(this.ordinal() + 1) % 12];
  }
}
