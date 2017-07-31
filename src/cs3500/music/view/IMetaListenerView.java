package cs3500.music.view;

import cs3500.music.controller.MetaListener;

/**
 * Interface for views that use MetaListeners. Views that need to respond to a MetaMessage will
 * implement this interface. The views that use MetaListeners also must have a way to add music
 * notes. This interface is parameterized across the implementation of a music note.
 */
public interface IMetaListenerView<T> {

  /**
   * Adds the given MetaListener to the view.
   *
   * @param metaListener The MetaListener to be added
   */
  void addMetaListener(MetaListener metaListener);

  /**
   * A note was added to the model so a new note must be added to the view as well.
   *
   * @param note The note to be added
   */
  void addNote(T note);
}
