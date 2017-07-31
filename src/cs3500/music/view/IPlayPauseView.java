package cs3500.music.view;

/**
 * Interface for views that muse have play/pause/stop functionality. Views that need to either
 * start, pause, or stop the playing of the Music Editor will implement this interface.
 */
public interface IPlayPauseView {
  /**
   * Begin playing at the passed in beat number and begin incrementing the current beat.
   *
   * @param currentBeat The beat to start playing at
   */
  void play(int currentBeat);

  /**
   * Pauses the song so the current beat does not increment.
   */
  void pause();

  /**
   * Stops the song so it cannot be played anymore.
   */
  void stop();
}
