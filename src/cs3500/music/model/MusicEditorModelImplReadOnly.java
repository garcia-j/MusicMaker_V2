package cs3500.music.model;

import java.util.Map;
import java.util.Set;

/**
 * A Music Editor that is Read Only. This Music Editor cannot call methods that change data in a
 * Music Editor. Instead it can only get data from the Music Editor.
 */
public class MusicEditorModelImplReadOnly implements MusicEditorOperations<IMusicNote<IPitch>, IPitch> {
  private MusicEditorOperations<IMusicNote<IPitch>, IPitch> model; // The model to get values from

  /**
   * Default constructor to create a blank model.
   */
  public MusicEditorModelImplReadOnly() {
    this.model = new MusicEditorModelImpl();
  }

  /**
   * Convenience constructor to set this model to the passed in model.
   */
  public MusicEditorModelImplReadOnly(MusicEditorOperations<IMusicNote<IPitch>, IPitch> model) {
    this.model = model;
  }

  @Override
  public void setTempo(int tempo) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public void setBeatsPerMeasure(int beatsPerMeasure) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public void newPiece() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public void addNote(IMusicNote<IPitch> note) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public void removeNote(IMusicNote<IPitch> note) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public void replaceNote(IMusicNote<IPitch> oldNote, IMusicNote<IPitch> newNote)
          throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public void append(MusicEditorOperations toAppend) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public void combine(MusicEditorOperations toCombine) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Model cannot modify data");
  }

  @Override
  public int getTempo() {
    return this.model.getTempo();
  }

  @Override
  public int getBeatsPerMeasure() {
    return this.model.getBeatsPerMeasure();
  }

  @Override
  public Map<Integer, Set<IMusicNote<IPitch>>> getNotes() {
    return this.model.getNotes();
  }

  @Override
  public Set<IMusicNote<IPitch>> getNotesAtBeat(int beat) throws IllegalArgumentException {
    return this.model.getNotesAtBeat(beat);
  }

  @Override
  public IPitch getLowPitch() {
    return this.model.getLowPitch();
  }

  @Override
  public IPitch getHighPitch() {
    return this.model.getHighPitch();
  }
}
