package cs3500.music.view;

import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.Transmitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cs3500.music.model.IPitch;

/**
 * Represents a MockSequencer. When a sequence is set, the Mock Sequencer will take all the notes
 * in the track and EVENT_LOG them to the EVENT_LOG so they can be viewed later for testing
 * purposes.
 */
public class MockSequencer implements Sequencer {

  public static StringBuilder EVENT_LOG;

  /**
   * Default constructor for MockSequencer.
   */
  public MockSequencer() {
    EVENT_LOG = new StringBuilder();
  }

  /**
   * When the sequence gets set, the notes in the sequence and events are appended to the log.
   *
   * @param s The sequence being set
   */
  @Override
  public void setSequence(Sequence s) {
    Track myTrack = s.getTracks()[0];

    for (int i = 0; i < myTrack.size(); i++) {
      MidiEvent currEvent = myTrack.get(i);
      byte[] data = currEvent.getMessage().getMessage();
      try {
        ShortMessage msg = new ShortMessage(data){};
        int cmd = msg.getCommand();
        String cmdMsg;

        switch (cmd) {
          case 192: cmdMsg = "PROGRAM_CHANGE";
            break;
          case 144: cmdMsg = "NOTE_ON";
            break;
          case 128: cmdMsg = "NOTE_OFF";
            break;
          case 240: cmdMsg = "END";
            break;
          default: cmdMsg = "";
        }

        EVENT_LOG.append("Beat: " + currEvent.getTick() / MidiViewImpl.FACTOR + " ");
        EVENT_LOG.append("Command: " + cmdMsg + " ");

        if (cmdMsg.equals("NOTE_ON") || cmdMsg.equals("NOTE_OFF")) {
          EVENT_LOG.append("Pitch: " + IPitch.midiToPitch(msg.getData1()) + " ");
          EVENT_LOG.append("Volume: " + msg.getData2());
        }
        else if (cmdMsg.equals("PROGRAM_CHANGE")) {
          EVENT_LOG.append("Instrument: " + msg.getData1() + " ");
          EVENT_LOG.append("Data2: " + msg.getData2());
        }
        else {
          EVENT_LOG.append("End Data1: " + msg.getData1() + " ");
          EVENT_LOG.append("End Data2: " + msg.getData2());
        }
        EVENT_LOG.append("\n");
      } catch (Exception e) {
        return;
      }

    }
  }

  @Override
  public void setSequence(InputStream inputStream) throws IOException, InvalidMidiDataException,
          UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public Sequence getSequence() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");

  }

  @Override
  public void start() {
    return;
  }

  @Override
  public void stop() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public boolean isRunning() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void startRecording() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void stopRecording() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public boolean isRecording() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void recordEnable(Track track, int i) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void recordDisable(Track track) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public float getTempoInBPM() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setTempoInBPM(float v) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public float getTempoInMPQ() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setTempoInMPQ(float v) {
    return;
  }

  @Override
  public void setTempoFactor(float v) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public float getTempoFactor() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public long getTickLength() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public long getTickPosition() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setTickPosition(long l) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public long getMicrosecondLength() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public Info getDeviceInfo() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void open() throws MidiUnavailableException, UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void close() {
    return;
  }

  @Override
  public boolean isOpen() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public long getMicrosecondPosition() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public int getMaxReceivers() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public int getMaxTransmitters() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public Receiver getReceiver() throws MidiUnavailableException, UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public List<Receiver> getReceivers() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public Transmitter getTransmitter() throws MidiUnavailableException,
          UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public List<Transmitter> getTransmitters() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setMicrosecondPosition(long l) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setMasterSyncMode(SyncMode syncMode) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public SyncMode getMasterSyncMode() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public SyncMode[] getMasterSyncModes() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setSlaveSyncMode(SyncMode syncMode) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public SyncMode getSlaveSyncMode() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public SyncMode[] getSlaveSyncModes() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setTrackMute(int i, boolean b) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public boolean getTrackMute(int i) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setTrackSolo(int i, boolean b) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public boolean getTrackSolo(int i) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public boolean addMetaEventListener(MetaEventListener metaEventListener)
          throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void removeMetaEventListener(MetaEventListener metaEventListener)
          throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public int[] addControllerEventListener(ControllerEventListener controllerEventListener,
                                          int[] ints) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public int[] removeControllerEventListener(ControllerEventListener controllerEventListener,
                                             int[] ints) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setLoopStartPoint(long l) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public long getLoopStartPoint() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setLoopEndPoint(long l) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public long getLoopEndPoint() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public void setLoopCount(int i) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }

  @Override
  public int getLoopCount() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Operation is not supported in this mock");
  }
}
