package cs3500.music.view;

/**
 * All views implement this interface. This interface provides the functionality to display the
 * view. Displays the notes in a way that can be interpreted by the user. Parameterized across the
 * Music Editor model.
 */
public interface IView<T> {

  /**
   * Displays this view. The implementation of this interface takes the Music Editor model and
   * creates a view such that the user can interface with it.
   *
   * @param model The Music Editor model
   */
  void display(T model);
}
