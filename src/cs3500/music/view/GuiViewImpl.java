package cs3500.music.view;

import java.awt.GridLayout;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Collections;


import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.ScrollPaneConstants;

import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorOperations;

/**
 * Implementation of a GUI View for Music Editor. The view is split into two sections an upper score
 * section and a lower keyboard section. The score section displays the notes in the Music Editor
 * on the left side in a range from the lowest to highest pitch, with the highest pitch at the top
 * of the left side. It displays the beats in the Music Editor along the top of the score section
 * with each beat number corresponding to four beats in the Music Editor. A start beat of a note is
 * represented by a black square while a continuation beat of a note is represented by a green
 * square. The current beat of the Music Editor is represented by a red line and moves based on user
 * input. The keyboard section displays a ten octave keyboard. A note that is currently being
 * played is represented by an orange key. Therefore, the orange keys is in sync with the red line
 * in the score section.
 */
public class GuiViewImpl extends JFrame
        implements IGuiView<MusicEditorOperations<IMusicNote<IPitch>, IPitch>>, IKeyListenerView,
        IMouseListenerView {
  public static final int WIDTH = 1200; // The width of the view
  public static final int HEIGHT = 600; // The height of the view

  /**
   * Contains the score panel so the user can scroll to view all pitches if the number of pitches
   * is too large to be displayed.
   */
  private JScrollPane scrollPane;
  private final ScorePanel scorePanel; // The score section
  private final KeyboardPanel keyboardPanel; // The keyboard section

  /**
   * Constructor for GuiViewImpl. Creates a new GUI view with a score and a keyboard section.
   */
  public GuiViewImpl() {
    this.scorePanel = new ScorePanel();
    this.keyboardPanel = new KeyboardPanel();
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setLayout(new GridLayout(2, 1));
    this.scrollPane = new JScrollPane(scorePanel);
    this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    this.getContentPane().add(scrollPane);
    this.getContentPane().add(keyboardPanel);
    this.setResizable(false);
    this.pack();
  }

  @Override
  public void display(MusicEditorOperations<IMusicNote<IPitch>, IPitch> model) {
    this.scorePanel.setPreferredSize(new Dimension(Collections.max(model.getNotes().keySet()) * 15,
            (model.getLowPitch().getDifference(model.getHighPitch()) + 4) * 15));
    this.scrollPane.setViewportView(scorePanel);
    this.scorePanel.redraw(model, 0);
    this.keyboardPanel.redraw(model, 0);
    this.requestFocus();
    initialize();
  }

  @Override
  public void addKeyListener(KeyListener keyListener) {
    super.addKeyListener(keyListener);
  }

  @Override
  public void initialize() {
    this.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(WIDTH, HEIGHT);
  }

  @Override
  public void updateCurrentBeat(int currentBeat) {
    this.scorePanel.updateCurrentBeat(currentBeat);
    this.keyboardPanel.updateCurrentBeat(currentBeat);
    repaint();
  }

  @Override
  public void redraw(MusicEditorOperations<IMusicNote<IPitch>, IPitch> model, int currentBeat) {
    this.scorePanel.redraw(model, currentBeat);
    this.keyboardPanel.redraw(model, currentBeat);
    this.scorePanel.setPreferredSize(new Dimension(Collections.max(model.getNotes().keySet()) * 15,
            (model.getLowPitch().getDifference(model.getHighPitch()) + 4) * 15));
    this.scrollPane.setViewportView(scorePanel);
  }

  @Override
  public void addMouseListener(MouseListener mouseListener) {
    super.addMouseListener(mouseListener);
  }
}