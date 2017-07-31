package cs3500.music.controller;

/**
 * Controller for a Music Editor. In charge of controlling the view and the model of Music Editors.
 * Can take in input from the user (mouse actions, keyboard actions, user String input, etc.) and
 * change the model, view, both or neither.
 */
public interface IController {

  /**
   * Run the controller. This method will start the controller and set up any listeners required by
   * the controller. It will take in data from ther user and pass it off to the model or the view.
   */
  void startApplication();

  /**
   * Returns the current beat that is being played by the music editor.
   *
   * @return The current beat
   */
  int getCurrentBeat();
}
