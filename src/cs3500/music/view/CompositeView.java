package cs3500.music.view;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.controller.MetaListener;
import cs3500.music.model.IMusicNote;
import cs3500.music.model.MusicEditorOperations;

/**
 * CompositeView of the MusicEditor. A CompositeView is a combination of a GuiView and a MidiView.
 * It shows the notes being played and the keyboard keys being played in the GuiView. Further, as
 * the MidiView is being played, the red line for the GuiView moves with the beat. The play and
 * pause of the view can be toggled using the SpaceBar.
 */
public class CompositeView implements IGuiView<MusicEditorOperations>,
        IKeyListenerView, IMouseListenerView, IMetaListenerView<IMusicNote>, IPlayPauseView {
  private GuiViewImpl guiView; // The gui view component of this view
  private MidiViewImpl midiView; // The midi view component of this view

  public CompositeView() {
    this.guiView = new GuiViewImpl();
    this.midiView = new MidiViewImpl();
  }

  @Override
  public void addMouseListener(MouseListener mouseListener) {
    this.guiView.addMouseListener(mouseListener);
  }

  @Override
  public void addKeyListener(KeyListener keyListener) {
    this.guiView.addKeyListener(keyListener);
  }

  @Override
  public void initialize() {
    this.guiView.initialize();
  }

  @Override
  public Dimension getPreferredSize() {
    return this.guiView.getPreferredSize();
  }

  @Override
  public void updateCurrentBeat(int currentBeat) {
    this.guiView.updateCurrentBeat(currentBeat);
  }

  @Override
  public void redraw(MusicEditorOperations model, int currentBeat) {
    this.guiView.redraw(model, currentBeat);
  }

  @Override
  public void display(MusicEditorOperations model) {
    this.midiView.display(model);
    this.guiView.display(model);
  }

  @Override
  public void addMetaListener(MetaListener metaListener) {
    this.midiView.addMetaListener(metaListener);
  }

  @Override
  public void addNote(IMusicNote note) {
    this.midiView.addNote(note);
  }

  @Override
  public void play(int currentBeat) {
    this.midiView.play(currentBeat);
  }

  @Override
  public void playBeat(int currentBeat) {
    this.midiView.playBeat(currentBeat);
  }

  @Override
  public void pause() {
    this.midiView.pause();
  }

  @Override
  public void stop() {
    this.midiView.stop();
  }
}
