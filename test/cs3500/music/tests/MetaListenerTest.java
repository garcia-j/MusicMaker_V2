package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import cs3500.music.controller.MetaListener;
import cs3500.music.controller.MockRunnable;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.model.MusicEditorModelImplReadOnly;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.MusicNote;
import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;
import cs3500.music.view.CompositeView;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the MetaListener class. Uses a MetaListener initializes with a mock runnable that
 * keeps track of the total count of calls to run() which should be equal to the total number of
 * beats.
 */
public class MetaListenerTest {
  private MetaListener metaListener;
  private CompositeView metaView;
  private MusicEditorOperations model;

  /**
   * Initialization for the metaView and the metaListener. Sets the metaListener's runnable to the
   * mock.
   */
  @Before
  public void doBefore() {
    model = new MusicEditorModelImpl();
    model.addNote(new MusicNote(new Pitch(NoteName.A_SHARP, 1), 10, 10, 1, 1));

    metaListener = new MetaListener();
    metaListener.setRunnable(new MockRunnable("meta"));

    metaView = new CompositeView();
    metaView.addMetaListener(metaListener);
    metaView.display(new MusicEditorModelImplReadOnly(model));
  }

  /**
   * Plays a 20-beat long piece of music and checks that the MetaLisener called its runnable exactly
   * 20 times.
   * @throws InterruptedException If the thread cannot sleep
   */
  @Test
  public void testMetaListener() throws InterruptedException {
    metaView.play(0);
    Thread.sleep(500);
    assertTrue(MockRunnable.EVENT_LOG.toString().contains("MoveWhilePlaying count: 20"));
  }

  /**
   * Checks that the MetaMessage is only added for each beat not per note (same time notes).
   * @throws InterruptedException If the thread cannot sleep
   */
  @Test
  public void testMetaListenerTwoBeatsSameTime() throws InterruptedException {
    model.addNote(new MusicNote(new Pitch(NoteName.A_SHARP, 3), 10, 10, 1, 1));
    metaView = new CompositeView();
    metaView.addMetaListener(metaListener);
    metaView.display(new MusicEditorModelImplReadOnly(model));

    metaView.play(0);
    Thread.sleep(500);
    assertTrue(MockRunnable.EVENT_LOG.toString().contains("MoveWhilePlaying count: 20"));
  }

  /**
   * Checks that the MetaMessage is only added for each beat not per note (consecutive notes).
   * @throws InterruptedException If the thread cannot sleep
   */
  @Test
  public void testMetaListenerTwoBeatsDifferentTime() throws InterruptedException {
    model.addNote(new MusicNote(new Pitch(NoteName.A_SHARP, 3), 10, 21, 1, 1));
    metaView = new CompositeView();
    metaView.addMetaListener(metaListener);
    metaView.display(new MusicEditorModelImplReadOnly(model));

    metaView.play(0);
    Thread.sleep(500);
    assertTrue(MockRunnable.EVENT_LOG.toString().contains("MoveWhilePlaying count: 31"));
  }
}