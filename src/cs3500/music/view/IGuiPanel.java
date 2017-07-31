package cs3500.music.view;

/**
 * Interface for a GUI Panels. Allows a GUI Panel to update its model and current beat.
 * Parameterized across the Music Editor model.
 */
public interface IGuiPanel<T> {

  /**
   * Updates the model and the current beat in the Panel. This method is used when only the current
   * beat needs to be updated and the model does not need to be redrawn.
   *
   * @param currentBeat The currentBeat to update to
   */
  void updateCurrentBeat(int currentBeat);

  /**
   * Redraws the GuiPanel. This method is used instead of the updateCurrentBeat method when the
   * model has new notes that need to be redrawn.
   *
   * @param model The Music Editor model to redraw
   * @param currentBeat The current beat to redraw
   */
  void redraw(T model, int currentBeat);
}
