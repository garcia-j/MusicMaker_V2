package cs3500.music.model;

import java.util.Objects;

/**
 * To represent a musical note. A musical note consists of a Pitch, a duration, and a start beat.
 */
public final class MusicNote implements IMusicNote<IPitch> {

  /**
   * The pitch of the note.
   *
   * <p>Class invariant: The pitch of the note always has a NoteName from C to B and an octave from
   * </p>
   */
  private final IPitch pitch;

  /**
   * The duration of the note.
   *
   * <p>Class invariant: The duration of the note is always positive.</p>
   */
  private final int duration;

  /**
   * The beat that this note starts at.
   *
   * <p>Class invariant: The beat of the note is always a natural number.</p>
   */
  private final int startBeat;

  /**
   * The instrument in the sound bank that this instrument represents.
   */
  private final int instrument;

  /**
   * The volume of the note when played via Midi.
   */
  private final int volume;

  /**
   * Constructor for MusicNote.
   *
   * @param pitch The pitch of the note
   * @param duration The duration of the note
   * @param startBeat The beat that this note starts at
   * @throws IllegalArgumentException If the duration is less than 1 or the startBeat is negative
   */
  public MusicNote(IPitch pitch, int duration, int startBeat, int instrument, int volume)
          throws IllegalArgumentException {
    if (duration < 0) {
      throw new IllegalArgumentException("Duration cannot be negative");
    }

    if (startBeat < 0) {
      throw new IllegalArgumentException("Start beat cannot be negative");
    }
    this.instrument = instrument;
    this.volume = volume;
    this.pitch = pitch;
    this.duration = duration;
    this.startBeat = startBeat;
  }

  /**
   * Copy Constructor for MusicNote.
   *
   * @param note The MusicNote to copy
   */
  MusicNote(IMusicNote<IPitch> note) {
    this.pitch = note.getPitch();
    this.duration = note.getDuration();
    this.startBeat = note.getStartBeat();
    this.instrument = note.getInstrument();
    this.volume = note.getVolume();
  }

  @Override
  public int getInstrument() {
    return this.instrument;
  }

  @Override
  public int getVolume() {
    return this.volume;
  }

  @Override
  public IPitch getPitch() {
    return new Pitch(pitch);
  }

  @Override
  public int getDuration() {
    return duration;
  }

  @Override
  public int getStartBeat() {
    return startBeat;
  }

  /**
   * Determines if this note is equal to the given object. They are equal if they have the same
   * pitch, duration, and start beat.
   * @param o The object to compare to
   * @return If this note is equal to that object
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (! (o instanceof MusicNote)) {
      return false;
    }

    MusicNote that = (MusicNote)o;
    return this.pitch.equals(that.pitch)
            &&
            this.duration == that.duration
            &&
            this.startBeat == that.startBeat
            &&
            this.volume == that.volume
            &&
            this.instrument == that.instrument;
  }

  /**
   * Hash function for music note.
   * @return The hashcode of this note
   */
  @Override
  public int hashCode() {
    return Objects.hash(pitch, duration, startBeat, volume, instrument);
  }
}
