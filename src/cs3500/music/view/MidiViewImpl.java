package cs3500.music.view;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiChannel;

import cs3500.music.controller.MetaListener;
import cs3500.music.model.IMusicNote;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.NoteName;

/**
 * The Midi View for a Music Editor. When displayed, the Midi converts notes to their Midi
 * equivalent and then adds each note to a sequencer. Once all notes are added to the sequencer,
 * the notes are played to the user.
 */
public class MidiViewImpl implements IView<MusicEditorOperations<IMusicNote<IPitch>, IPitch>>,
        IMetaListenerView<IMusicNote<IPitch>>, IPlayPauseView {
  public static final int FACTOR = 7; // Factor used for resolution and tick multiplication

  /**
   * The sequencer that contains the notes of the Music Editor.
   */
  private final Sequencer sequencer;

  /**
   * The sequence that is contained in the Sequencer.
   */
  private Sequence sequence;

  /**
   * The tempo of the song.
   */
  private int tempo;

  /**
   * Constructor for the MidiView. Creates a new Sequencer.
   */
  public MidiViewImpl() {
    Sequencer seq = null;
    try {
      seq = MidiSystem.getSequencer();
      seq.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }

    this.sequencer = seq;
    try {
      this.sequence = new Sequence(Sequence.PPQ, FACTOR);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Ease of use constructor for the MidiView.
   */
  public MidiViewImpl(Sequencer sequencer) {
    this.sequencer = sequencer;
  }


  /**
   * Relevant classes and methods from the javax.sound.midi library:
   * <ul>
   *  <li>{@link MidiSystem#getSynthesizer()}</li>
   *  <li>{@link Synthesizer}
   *    <ul>
   *      <li>{@link Synthesizer#open()}</li>
   *      <li>{@link Synthesizer#getReceiver()}</li>
   *      <li>{@link Synthesizer#getChannels()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link Receiver}
   *    <ul>
   *      <li>{@link Receiver#send(MidiMessage, long)}</li>
   *      <li>{@link Receiver#close()}</li>
   *    </ul>
   *  </li>
   *  <li>{@link MidiMessage}</li>
   *  <li>{@link ShortMessage}</li>
   *  <li>{@link MidiChannel}
   *    <ul>
   *      <li>{@link MidiChannel#getProgram()}</li>
   *      <li>{@link MidiChannel#programChange(int)}</li>
   *    </ul>
   *  </li>
   * </ul>
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI">
   *   https://en.wikipedia.org/wiki/General_MIDI
   *   </a>
   */
  @Override
  public void display(MusicEditorOperations<IMusicNote<IPitch>, IPitch> model) {
    Set<IMusicNote<IPitch>> allNotes = new HashSet<IMusicNote<IPitch>>();

    try {
      Track track = sequence.createTrack();
      Track metaTrack = sequence.createTrack();

      tempo = model.getTempo();
      Map<Integer, Set<IMusicNote<IPitch>>> notes = model.getNotes();
      int max = Collections.max(notes.keySet()) + 1;

      for (int i = 1; i < max; i++) {
        MetaMessage message = new MetaMessage();
        MidiEvent event = new MidiEvent(message, i * FACTOR);
        metaTrack.add(event);
      }

      for (Integer i : notes.keySet()) {
        for (IMusicNote<IPitch> n : notes.get(i)) {
          allNotes.add(n);
        }
      }

      for (IMusicNote<IPitch> n : allNotes) {
        ShortMessage start = new ShortMessage(ShortMessage.NOTE_ON, 0, pitchToMIDI(n.getPitch()),
                n.getVolume());
        ShortMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, 0, pitchToMIDI(n.getPitch()),
                n.getVolume());
        ShortMessage instrument = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0,
                n.getInstrument(), 0);

        MidiEvent instrumentEvent = new MidiEvent(instrument, n.getStartBeat() * FACTOR);
        track.add(instrumentEvent);

        MidiEvent startEvent = new MidiEvent(start, n.getStartBeat() * FACTOR);
        track.add(startEvent);

        MidiEvent stopEvent = new MidiEvent(stop,
                (n.getStartBeat() + n.getDuration()) * FACTOR - 1);
        track.add(stopEvent);
      }

      this.sequencer.setSequence(sequence);
    } catch (Exception e) {
      return;
    }
  }

  /**
   * Converts a pitch to its corresponding MIDI value.
   *
   * @param p The pitch to convert
   * @return The MIDI value
   */
  private int pitchToMIDI(IPitch p) {
    NoteName noteName = p.getNoteName();
    int octave = p.getOctave();
    int octaveMIDI = (octave + 1) * 12;

    return octaveMIDI + noteName.ordinal();
  }

  @Override
  public void addMetaListener(MetaListener metaListener) {
    this.sequencer.addMetaEventListener(metaListener);
  }

  @Override
  public void addNote(IMusicNote<IPitch> note) {
    try {
      Track track = sequence.getTracks()[0];
      Track metaTrack = sequence.getTracks()[1];

      if (note.getStartBeat() > metaTrack.size() - 1) {
        MetaMessage message = new MetaMessage();
        MidiEvent event = new MidiEvent(message, note.getStartBeat() * FACTOR);
        metaTrack.add(event);
      }
      ShortMessage start = new ShortMessage(ShortMessage.NOTE_ON, 0, pitchToMIDI(note.getPitch()),
              note.getVolume());
      ShortMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, 0, pitchToMIDI(note.getPitch()),
              note.getVolume());
      ShortMessage instrument = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0,
              note.getInstrument(), 0);

      MidiEvent instrumentEvent = new MidiEvent(instrument, (note.getStartBeat() - 1) * FACTOR);
      track.add(instrumentEvent);

      MidiEvent startEvent = new MidiEvent(start, (note.getStartBeat() - 1) * FACTOR);
      track.add(startEvent);

      MidiEvent stopEvent = new MidiEvent(stop,
              (note.getStartBeat() + note.getDuration()) * FACTOR - 1);
      track.add(stopEvent);
      this.sequencer.setSequence(sequence);
    } catch (Exception e) {
      return;
    }
  }

  @Override
  public void play(int currentBeat) {
    sequencer.setTickPosition(currentBeat * FACTOR);
    sequencer.start();
    sequencer.setTempoInMPQ(tempo);
  }

  @Override
  public void playBeat(int currentBeat) {
    Sequencer seq = null;
    try {
      seq = MidiSystem.getSequencer();
      seq.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }


  }

  @Override
  public void pause() {
    sequencer.stop();
  }

  @Override
  public void stop() {
    this.sequencer.close();
    if (!(this.sequencer instanceof MockSequencer)) {
      System.exit(0);
    }
  }
}
