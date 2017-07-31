package cs3500.music.view;

import cs3500.music.provider.Note;
import cs3500.music.provider.Pitch;

/**
 * Factory class for views. Creates the specific view needed based on a String inputted by the user.
 */
public class ViewFactory {

  /**
   * Creates a specific view type based on the String argument. "console" for ConsoleView, "visual"
   * for GuiViewImpl, "midi" for MidiViewImpl and "composite" for CompositeView.
   *
   * @param viewType The type of view to create
   * @return The correct view type
   * @throws IllegalArgumentException If an incorrect view type was entered
   */
  public cs3500.music.provider.IView createView(String viewType) throws IllegalArgumentException {
    switch (viewType) {
      case "console":
        return new cs3500.music.provider.ConsoleView();
      case "visual":
        return new cs3500.music.provider.GuiViewFrame();
      case "midi":
        return new cs3500.music.provider.MidiViewImpl(200000);
      case "composite":
        Note note = new Note(Pitch.A, 4, 1, 100, 1, true);
        return new cs3500.music.provider.CompositeView(note, note, 0);
      default:
        throw new IllegalArgumentException("Illegal view type");
    }
  }
}
