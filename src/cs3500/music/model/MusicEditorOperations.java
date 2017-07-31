package cs3500.music.model;

import java.util.Set;
import java.util.Map;

/**
 * A Music Editor contains a series of notes. The notion of note and a pitch is parameterized
 * across. Further, a music editor has a tempo and a number of beats per measure. Music Editors can
 * be combined (one Music Editor is put on top of another) and appended (one Music Editor is put
 * directly after another). The container that the notes in a Music Editor are contained in is up
 * to the implementation.
 */
public interface MusicEditorOperations<K, T> {

  /**
   * Sets the tempo of the piece in microseconds. Throws an IllegalArgumentException if the
   * tempo is not positive.
   *
   * @param tempo The new tempo of the piece
   * @throws IllegalArgumentException If the tempo is not positive
   */
  void setTempo(int tempo) throws IllegalArgumentException;

  /**
   * Set the beats per measure of the piece of music. Throws an IllegalArgumentException if the
   * beatsPerMeasure is not positive.
   *
   * @param beatsPerMeasure The new beats per measure of the piece of music
   * @throws IllegalArgumentException If the beatsPerMeasure is not positive
   */
  void setBeatsPerMeasure(int beatsPerMeasure) throws IllegalArgumentException;

  /**
   * Creates a new music piece. The notes in the piece are cleared and the piece of musice is reset
   * to a length of 0. The tempo of the piece is reset to 60 beats per minute and the beats per
   * measure is reset to 4 beats in a measure.
   */
  void newPiece();

  /**
   * Adds the given note to the piece of music. If the notes are past the end of the current piece,
   * the beats will be added to the piece. Duplicate notes will not be added. Two notes are defined
   * as duplicate notes if they have the same Pitch, duration, and startBeat. Throws an
   * IllegalArgumentException if the note to add is null.
   *
   * @param note The note to add to the editor
   * @throws IllegalArgumentException If the note to add is null
   */
  void addNote(K note) throws IllegalArgumentException;

  /**
   * Removes the given note from the piece of music. If the note was the last note being played, the
   * beats that are now empty are removed. Throws an IllegalArgumentException if the note to
   * remove does not exist or if the note to remove is null.
   *
   * @param note The music note to be removed
   * @throws IllegalArgumentException If the Note to remove is null or does not exist
   */
  void removeNote(K note) throws IllegalArgumentException;

  /**
   * Removes the oldNote and replaces it with newNote. If the new note is a duplicate note to a note
   * that already exists, it will not be added, but the old note will still be removed. A duplicate
   * note is defined as two notes that have the same Pitch, duration, and startBeat. If the old note
   * does not exist, the new note will not be added. Throws an IllegalArgumentException if either
   * note is null or if the note to remove does not exist.
   *
   * @param oldNote The note to remove
   * @param newNote The note to replace with
   * @throws IllegalArgumentException If the note to remove is null or either note is null
   */
  void replaceNote(K oldNote, K newNote) throws IllegalArgumentException;

  /**
   * Append the given piece onto the end of this piece. The piece that is being appended will take
   * on the tempo and beats per measure of this piece. Throws an IllegalArgumentException if the
   * piece to add at the end is null.
   *
   * @param toAppend The piece to be appended at the end of this piece
   * @throws IllegalArgumentException If the editor to be appended at the end is null
   */
  void append(MusicEditorOperations toAppend) throws IllegalArgumentException;

  /**
   * Combine the given piece on top of this piece. The piece that is being combined will take on the
   * tempo and beats per measure of this piece. If a note in the piece being combined is a duplicate
   * note, it will not be added. A duplicate note is defined as one that as the same Pitch,
   * duration, and startBeat as a note that already exists. Throws an IllegalArgumentException if
   * the piece to be combined with is null.
   *
   * @param toCombine The piece to be combined to this piece
   * @throws IllegalArgumentException If the piece to be combined is null
   */
  void combine(MusicEditorOperations toCombine) throws IllegalArgumentException;

  /**
   * Gets the tempo of the piece of music in beats per minute.
   *
   * @return The tempo of the piece of music
   */
  int getTempo();

  /**
   * Gets the beats per measure of the piece of music.
   *
   * @return The beats per measure of the piece
   */
  int getBeatsPerMeasure();

  /**
   * Gets the notes from this music editor. The notes are the values in the returned SortedMap while
   * the beats are the key in the returned SortedMap. If there are no notes at a certain beat, the
   * key will not appear in the SortedMap.
   *
   * @return The music notes from this music editor.
   */
  Map<Integer, Set<K>> getNotes();

  /**
   * Gets all the notes that are playing at a certain beat. If there are no notes at a certain beat,
   * an empty set will be returned. Throws an IllegalArgumentException if the beat is negative.
   *
   * @param beat The beat to get the notes that are playing
   * @return The set of notes that are playing
   * @throws IllegalArgumentException If the beat is negative
   */
  Set<K> getNotesAtBeat(int beat) throws IllegalArgumentException;

  /**
   * Gets the lowest pitch in the Music Editor where lowest is defined as the lowest frequency of
   * a note in the Music Editor.
   *
   * @return The lowest pitch
   */
  T getLowPitch();

  /**
   * Gets the highest pitch in the Music Editor where highest is defined as the highest frequency of
   * a note in the Music Editor.
   *
   * @return The highest pitch
   */
  T getHighPitch();
}