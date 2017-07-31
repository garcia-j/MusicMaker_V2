package cs3500.music.controller;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;

/**
 * A Listener for MetaMessages. Once a MetaMessage is encountered in the Sequencer, the Runnable for
 * this MetaListener is run. This class is used to sync the GUI View to the Midi View.
 */
public class MetaListener implements MetaEventListener {
  // The Runnable to be run when a MetaMessage is encountered
  Runnable runnable;

  /**
   * Set the MetaListener's Runnable to the passed in Runnable.
   *
   * @param runnable The Runnable to set this MetaListener Runnable to
   */
  public void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void meta(MetaMessage meta) {
    runnable.run();
  }
}
