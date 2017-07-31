package cs3500.music.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import cs3500.music.util.CompositionBuilder;

/**
 * A Music Editor that is parameterized across the Music Note and Pitch classes. Notes in this
 * Music Editor are stored in a SortedMap where the beat number is mapped to the set of notes that
 * are played at that beat. This Music Editor allows capabilities for all functions in the
 * MusicEditorOperations interface (unlike the MusicEditorModelImplReadOnly).
 */
public class MusicEditorModelImpl implements MusicEditorOperations<IMusicNote<IPitch>, IPitch> {

  /**
   * The notes in this editor. The Integer represents each beat in the editor and each beat is
   * mapped to a set of notes that is being played at that beat. A SortedMap is used so that it is
   * easy to get the highest beat in the editor. Further, a map was used instead of a list because
   * it is faster to add, remove, and retrieve from a map than a list. A set was used because a set
   * does not allow duplicates and my model implementation does not allow duplicates.
   *
   * <p>Class invariant: If the size of the SortedMap is 0 there are no beats in the piece of music
   * and if the size of the SortedMap is not 0, the last key in the SortedMap is the highest beat in
   * the piece of music.</p>
   */
  private SortedMap<Integer, Set<IMusicNote<IPitch>>> notes;

  /**
   * The lowest pitch in the piece of music.
   *
   * <p>Class invariant: The lowest pitch in the editor is either null or a valid Pitch (valid Pitch
   * means it has an NoteName from C to B and an octave from 1 to 10).</p>
   */
  private IPitch lowestPitch;

  /**
   * The highest pitch in the piece of music.
   *
   * <p>Class invariant: The highest pitch in the editor is either null or a valid Pitch (valid
   * Pitch means it has an NoteName from C to B and an octave from 1 to 10).</p>
   */
  private IPitch highestPitch;

  /**
   * The tempo of the piece of music in microseconds.
   *
   * <p>Class invariant: The tempo of the piece of music is always positive.</p>
   */
  private int tempo;

  /**
   * The beats per measure of the piece of music.
   *
   * <p>Class invariant: The beats per minute of the piece of music is always positive.</p>
   */
  private int beatsPerMeasure;

  /**
   * Constructor for MusicEditorModelImpl. Creates a new TreeMap and sets the lowest and highest
   * pitches to null. Sets the tempo of the piece of music to 60 and sets the beats per measure of
   * the piece of music to 4.
   */
  public MusicEditorModelImpl() {
    notes = new TreeMap<Integer, Set<IMusicNote<IPitch>>>();
    lowestPitch = null;
    highestPitch = null;
    tempo = 10000;
    beatsPerMeasure = 4;
  }

  @Override
  public int getTempo() {
    return tempo;
  }

  @Override
  public void setTempo(int tempo) throws IllegalArgumentException {
    if (tempo < 1) {
      throw new IllegalArgumentException("Tempo must be positive");
    }
    this.tempo = tempo;
  }

  @Override
  public int getBeatsPerMeasure() {
    return beatsPerMeasure;
  }

  @Override
  public void setBeatsPerMeasure(int beatsPerMeasure) throws IllegalArgumentException {
    if (beatsPerMeasure < 1) {
      throw new IllegalArgumentException("Beats per measure must be positive");
    }
    this.beatsPerMeasure = beatsPerMeasure;
  }

  @Override
  public void newPiece() {
    notes = new TreeMap<Integer, Set<IMusicNote<IPitch>>>();
    lowestPitch = null;
    highestPitch = null;
    tempo = 60;
    beatsPerMeasure = 4;
  }

  @Override
  public Map<Integer, Set<IMusicNote<IPitch>>> getNotes() {
    return new TreeMap<Integer, Set<IMusicNote<IPitch>>>(notes);
  }

  @Override
  public void addNote(IMusicNote<IPitch> note) throws IllegalArgumentException {
    if (note == null) {
      throw new IllegalArgumentException("Note to add cannot be null");
    }

    for (int i = note.getStartBeat(); i < note.getStartBeat() + note.getDuration(); i++) {
      addBeatIfNeeded(i);
      notes.get(i).add(new MusicNote(note));
    }

    if (lowestPitch == null || note.getPitch().compareTo(lowestPitch) < 0) {
      lowestPitch = new Pitch(note.getPitch());
    }

    if (highestPitch == null || note.getPitch().compareTo(highestPitch) > 0) {
      highestPitch = new Pitch(note.getPitch());
    }
  }

  @Override
  public void removeNote(IMusicNote<IPitch> note) throws IllegalArgumentException {
    if (note == null) {
      throw new IllegalArgumentException("Note to remove cannot be null");
    }

    if (!notes.containsKey(note.getStartBeat())) {
      throw new IllegalArgumentException("Note does not exist");
    }

    if (!notes.get(note.getStartBeat()).contains(note)) {
      throw new IllegalArgumentException("Note does not exist");
    }

    for (int i = note.getStartBeat(); i < note.getStartBeat() + note.getDuration(); i++) {
      notes.get(i).remove(note);
      subBeatIfNeeded(i);
    }

    if (note.getPitch().compareTo(lowestPitch) == 0
            ||
            note.getPitch().compareTo(highestPitch) == 0) {
      updatePitchExtremes();
    }
  }

  @Override
  public void replaceNote(IMusicNote<IPitch> oldNote, IMusicNote<IPitch> newNote)
          throws IllegalArgumentException {
    if (oldNote == null) {
      throw new IllegalArgumentException("The note to be replaced cannot be null");
    }

    if (newNote == null) {
      throw new IllegalArgumentException("The note to be added cannot be null");
    }

    removeNote(oldNote);
    addNote(newNote);
  }

  @Override
  public void append(MusicEditorOperations toAppend) throws IllegalArgumentException {
    if (toAppend == null) {
      throw new IllegalArgumentException("Editor to append cannot be null");
    }

    int startBeatOffset = 0;
    if (notes.size() != 0) {
      startBeatOffset = this.notes.lastKey() + 1;
    }

    Map<Integer, Set<MusicNote>> notesToAdd = toAppend.getNotes();

    for (Integer i : notesToAdd.keySet()) {
      for (MusicNote note : notesToAdd.get(i)) {
        this.addNote(new MusicNote(note.getPitch(), note.getDuration(),
                note.getStartBeat() + startBeatOffset, note.getInstrument(), note.getVolume()));
      }
    }
  }

  @Override
  public void combine(MusicEditorOperations toCombine) throws IllegalArgumentException {
    if (toCombine == null) {
      throw new IllegalArgumentException("Editor to combine cannot be null");
    }

    Map<Integer, Set<MusicNote>> notesToAdd = toCombine.getNotes();

    for (Integer i : notesToAdd.keySet()) {
      for (MusicNote note : notesToAdd.get(i)) {
        this.addNote(new MusicNote(note));
      }
    }
  }

  @Override
  public Set<IMusicNote<IPitch>> getNotesAtBeat(int beat) throws IllegalArgumentException {
    if (beat < 0) {
      throw new IllegalArgumentException("Beat cannot be negative");
    }

    if (!notes.containsKey(beat)) {
      return new HashSet<IMusicNote<IPitch>>();
    }
    else {
      return new HashSet(notes.get(beat));
    }
  }

  @Override
  public Pitch getLowPitch() {
    if (lowestPitch == null) {
      return null;
    }
    return new Pitch(lowestPitch);
  }

  @Override
  public Pitch getHighPitch() {

    if (highestPitch == null) {
      return null;
    }
    return new Pitch(highestPitch);
  }

  /**
   * Add a new key-value to the map if it does not exist.
   * @param i The beat number to check
   */
  private void addBeatIfNeeded(int i) {
    if (!notes.containsKey(i)) {
      notes.put(i, new HashSet<IMusicNote<IPitch>>());
    }
  }

  /**
   * Remove an existing key-value from the map if it no longer has any notes in it.
   * @param i The beat number to check
   */
  private void subBeatIfNeeded(int i) {
    if (notes.get(i).size() == 0) {
      notes.remove(i);
    }
  }

  /**
   * Updates the highest and lowest pitches.
   */
  private void updatePitchExtremes() {
    if (notes.size() == 0) {
      lowestPitch = null;
      highestPitch = null;
      return;
    }

    IPitch low = null;
    IPitch high = null;

    for (Integer i : notes.keySet()) {
      for (IMusicNote<IPitch> note: notes.get(i)) {
        if (low == null || note.getPitch().compareTo(low) < 0) {
          low = note.getPitch();
        }

        if (high == null || note.getPitch().compareTo(high) > 0) {
          high = note.getPitch();
        }
      }
    }

    lowestPitch = new Pitch(low);
    highestPitch = new Pitch(high);
  }

  /**
   * Builder used to create a model instance using text file input.
   */
  public static final class Builder implements CompositionBuilder<MusicEditorOperations> {

    private Map<Integer, MusicNote> notes = new HashMap<Integer, MusicNote>(); // Notes in builder
    private int tempo; // tempo of builder
    private static int i = 0; // note number

    @Override
    public MusicEditorOperations build() {
      ProviderToCurrentModel model = new ProviderToCurrentModel();
      for (Integer i : notes.keySet()) {
        model.addNote(notes.get(i));
      }

      model.setTempo(this.tempo);
      return model;
    }

    @Override
    public CompositionBuilder<MusicEditorOperations> setTempo(int tempo) {
      this.tempo = tempo;
      return this;
    }

    @Override
    public CompositionBuilder<MusicEditorOperations> addNote(int start, int end, int instrument,
                                                             int pitch, int volume) {
      notes.put(i, new MusicNote(intToPitch(pitch), end - start, start, instrument, volume));
      i++;
      return this;
    }

    /**
     * Converts the given int Midi value to the correct pitch.
     *
     * @param i The Midi value to convert
     * @return The corresponding Pitch
     */
    private Pitch intToPitch(int i) {
      int octave = (i / 12) - 1;
      NoteName name = NoteName.values()[i % 12];

      return new Pitch(name, octave);
    }
  }
}
