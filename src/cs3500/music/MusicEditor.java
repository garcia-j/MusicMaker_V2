package cs3500.music;

import cs3500.music.provider.IScore;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.GuiViewImpl;
import cs3500.music.view.MidiViewImpl;
import cs3500.music.view.ViewFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Class that contains the main function for Music Editor which starts the music editor with a
 * certain midi file and view. The first argument of the main method is the fileName and the second
 * argument of the main method is the type of view to create.
 */
public class MusicEditor {

  /**
   * Runs the Music Editor. Creates an instance of a model. Creates an instance of a view based on
   * the passed in argument. Sets a new model to the model and view and runs the Music Editor with
   * the view and model.
   *
   * @param args The first argument is the file name. The second argument is the view type
   * @throws IOException If the file could not be parsed correctly
   * @throws IllegalArgumentException If the file does not exist
   */
  public static void main(String[] args) throws IOException, IllegalArgumentException {
    GuiViewImpl guiView = new GuiViewImpl();
    MidiViewImpl midiView = new MidiViewImpl();
    ConsoleView consoleView = new ConsoleView();

    String fileName = args[0];
    String viewType = args[1];
    File file = new File(fileName);
    if (!file.exists()) {
      throw new IllegalArgumentException("File does not exist");
    }

    MusicReader reader = new MusicReader();
    ProviderToCurrentModel.Builder builder = new ProviderToCurrentModel.Builder();
    IScore model = reader.parseFile(new FileReader(fileName), builder);
    ProviderToCurrentController controller;
    ViewFactory factory = new ViewFactory();
    controller = new ProviderToCurrentController(factory.createView(viewType), model);

    controller.startApplication();
  }
}
