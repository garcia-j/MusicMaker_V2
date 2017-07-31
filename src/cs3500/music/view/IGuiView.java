package cs3500.music.view;

import java.awt.Dimension;

/**
 * Provides functionality needed for GuiViews. GuiViews must have a way to be initialized, have a
 * set size, update the current beat that the song is being played at, and redraw the view based on
 * a new model and current beat.
 */
public interface IGuiView<T> extends IView<T> {

  /**
   * Initializes the IGuiView and makes the view visible.
   */
  void initialize();

  /**
   * Sets the size of this view.
   *
   * @return The size of the view.
   */
  Dimension getPreferredSize();

  /**
   * Updates the current beat of the IGuiView. Used when only the current beat is changed.
   *
   * @param currentBeat The current beat to update to
   */
  void updateCurrentBeat(int currentBeat);

  /**
   * Redraws the GuiView by updating both the model and the current beat. Used when the model has
   * notes that need to be redrawn.
   *
   * @param model The new Music Editor model to draw
   * @param currentBeat The new current beat
   */
  void redraw(T model, int currentBeat);
}
