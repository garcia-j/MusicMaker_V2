package cs3500.music.model;

import java.util.Objects;

/**
 * To represent a note pitch. A note pitch contains a NoteName and an octave.
 */
public final class Pitch implements IPitch {

  /**
   * The note name of this pitch.
   *
   * <p>Class invariant: The noteName is always a NoteName from C to B.</p>
   */
  private final NoteName noteName;

  /**
   * The octave of this pitch.
   *
   * <p>Class invariant: The octave is always from 1 to 10.</p>
   */
  private final int octave; // The octave of this pitch

  /**
   * Constructor for Pitch. Takes in a NoteName and an octave.
   *
   * @param noteName The NoteName for this pitch
   * @param octave The octave for this pitch
   */
  public Pitch(NoteName noteName, int octave) {
    this.noteName = noteName;
    this.octave = octave;
  }

  /**
   * Copy constructor for Pitch. Takes in another Pitch.
   *
   * @param pitch The Pitch to copy
   */
  public Pitch(IPitch pitch) {
    this.noteName = pitch.getNoteName();
    this.octave = pitch.getOctave();
  }

  /**
   * Gets the Pitch after this Pitch.
   *
   * @return The next Pitch
   */
  public Pitch nextPitch() {
    if (noteName == NoteName.B) {
      return new Pitch(NoteName.C, octave + 1);
    }
    else {
      return new Pitch(noteName.getNextNoteName(), octave);
    }
  }

  /**
   * Gets the distance between this Pitch and the passed in Pitch.
   *
   * @param pitch The Pitch to get the distance between
   * @return The distance between the two pitches
   */
  @Override
  public int getDifference(IPitch pitch) {
    return Math.abs((pitch.getOctave() - octave) * 12 - (noteName.ordinal()
            -
            pitch.getNoteName().ordinal()));
  }

  /**
   * Get the NoteName of this Pitch.
   * @return The NoteName of this Pitch
   */
  @Override
  public NoteName getNoteName() {
    return noteName;
  }

  /**
   * Gets the octave of this Pitch.
   * @return The octave of this Pitch
   */
  @Override
  public int getOctave() {
    return octave;
  }

  /**
   * Returns a String representing this Pitch (the NoteName in String form followed by the octave).
   *
   * @return The String representing this Pitch
   */
  @Override
  public String toString() {
    return this.noteName.getNoteName() + this.octave;
  }

  /**
   * Compares this Pitch to the passed in Pitch. Returns a negative number if this Pitch is lower
   * than that Pitch, 0 if the Pitches are the same, and a positive number if this Pitch is higher
   * than that Pitch.
   *
   * @param that The Pitch to compare to
   * @return The comparison result
   */
  @Override
  public int compareTo(IPitch that) {
    if (this.octave != that.getOctave()) {
      return this.octave - that.getOctave();
    }

    return this.noteName.compareTo(that.getNoteName());
  }

  /**
   * Determines if this Pitch is equal to the passed in Object. They are equal if they have the same
   * NoteName and octave.
   *
   * @param o The Object to compare to
   * @return If this Pitch is equal to the passed in Object
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (! (o instanceof Pitch)) {
      return false;
    }

    Pitch that = (Pitch)o;

    return this.noteName.compareTo(that.noteName) == 0 && this.octave == that.octave;
  }

  /**
   * Hash function for Pitch.
   *
   * @return The hashcode for this Pitch
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.noteName, this.octave);
  }
}
