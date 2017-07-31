package cs3500.music.model;

/**
 * Represents a music note parameterized across the implementation of the pitch. A music note should
 * have an instrument, volume, pitch, duration, and start beat.
 */
public interface IMusicNote<K> {


  /**
   * Gets the instrument of the note.
   *
   * @return The instrument
   */
  int getInstrument();

  /**
   * Gets the volume of the note.
   *
   * @return The volume
   */
  int getVolume();

  /**
   * Gets the Pitch of this note.
   * @return The pitch of this note
   */
  K getPitch();

  /**
   * Gets the duration of this note.
   * @return The duration of this note
   */
  int getDuration();

  /**
   * Gets the start beat of this note.
   * @return The start beat of this note
   */
  int getStartBeat();
}
