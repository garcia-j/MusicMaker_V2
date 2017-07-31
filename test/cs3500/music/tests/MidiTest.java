package cs3500.music.tests;

import cs3500.music.controller.MusicEditorController;
import cs3500.music.model.IPitch;
import cs3500.music.model.MusicEditorModelImpl;
import cs3500.music.model.MusicEditorModelImplReadOnly;
import cs3500.music.model.MusicEditorOperations;
import cs3500.music.model.MusicNote;
import cs3500.music.model.NoteName;
import cs3500.music.model.Pitch;
import cs3500.music.util.MusicReader;
import cs3500.music.view.IView;
import cs3500.music.view.MidiViewImpl;
import cs3500.music.view.MockSequencer;

import org.junit.Before;
import org.junit.Test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Midi using a mock sequencer. The mock appends commands to a EVENT_LOG as it adds notes.
 * This allows us to check the EVENT_LOG to make sure the midi did what was expected.
 */
public class MidiTest {
  IView midiView;

  /**
   * Sets up the view as a new MidiViewImpl with a mock sequencer.
   */
  @Before
  public void beforeEachTest() {
    midiView = new MidiViewImpl(new MockSequencer());
  }

  /**
   * Tests the Midi Mock sequencer with a single note.
   */
  @Test
  public void testSingleNoteAddition() {
    MusicEditorOperations model = new MusicEditorModelImpl();
    model.addNote(new MusicNote(new Pitch(NoteName.A, 5), 2, 0,
            1, 70));
    midiView.display(new MusicEditorModelImplReadOnly(model));
    String log = MockSequencer.EVENT_LOG.toString();

    assertTrue(log.contains("Beat: 0 Command: NOTE_ON Pitch: A5 Volume: 70"));
    assertTrue(log.contains("Beat: 1 Command: NOTE_OFF Pitch: A5 Volume: 70"));
    assertTrue(log.contains("Beat: 0 Command: PROGRAM_CHANGE Instrument: 1 Data2: 0"));
  }

  /**
   * Tests the Midi Mock sequencer with two notes playing at the same time.
   */
  @Test
  public void testSameTimeNotes() {
    MusicEditorOperations model = new MusicEditorModelImpl();
    model.addNote(new MusicNote(new Pitch(NoteName.A, 5), 10, 0,
            1, 70));
    model.addNote(new MusicNote(new Pitch(NoteName.C, 7), 4, 3,
            5, 14));
    midiView.display(new MusicEditorModelImplReadOnly(model));
    String log = MockSequencer.EVENT_LOG.toString();

    assertTrue(log.contains("Beat: 0 Command: NOTE_ON Pitch: A5 Volume: 70"));
    assertTrue(log.contains("Beat: 9 Command: NOTE_OFF Pitch: A5 Volume: 70"));
    assertTrue(log.contains("Beat: 0 Command: PROGRAM_CHANGE Instrument: 1 Data2: 0"));
    assertTrue(log.contains("Beat: 3 Command: NOTE_ON Pitch: C7 Volume: 14"));
    assertTrue(log.contains("Beat: 6 Command: NOTE_OFF Pitch: C7 Volume: 14"));
    assertTrue(log.contains("Beat: 3 Command: PROGRAM_CHANGE Instrument: 5 Data2: 0"));
  }

  /**
   * Tests the Midi Mock sequencer for two notes played at different times.
   */
  @Test
  public void testDifferentTimeNotes() {
    MusicEditorOperations model = new MusicEditorModelImpl();
    model.addNote(new MusicNote(new Pitch(NoteName.A, 5), 10, 0,
            1, 70));
    model.addNote(new MusicNote(new Pitch(NoteName.C, 7), 4, 12,
            5, 14));
    midiView.display(new MusicEditorModelImplReadOnly(model));
    String log = MockSequencer.EVENT_LOG.toString();

    assertTrue(log.contains("Beat: 0 Command: NOTE_ON Pitch: A5 Volume: 70"));
    assertTrue(log.contains("Beat: 9 Command: NOTE_OFF Pitch: A5 Volume: 70"));
    assertTrue(log.contains("Beat: 0 Command: PROGRAM_CHANGE Instrument: 1 Data2: 0"));
    assertTrue(log.contains("Beat: 12 Command: NOTE_ON Pitch: C7 Volume: 14"));
    assertTrue(log.contains("Beat: 15 Command: NOTE_OFF Pitch: C7 Volume: 14"));
    assertTrue(log.contains("Beat: 12 Command: PROGRAM_CHANGE Instrument: 5 Data2: 0"));
  }

  /**
   * Tests the Midi Mock sequencer for no notes.
   */
  @Test
  public void testNoNotes() {
    MusicEditorOperations model = new MusicEditorModelImpl();
    midiView.display(new MusicEditorModelImplReadOnly(model));

    String log = MockSequencer.EVENT_LOG.toString();
    assertEquals("Beat: 0 Command: END End Data1: 47 End Data2: 0\n", log);
  }

  /**
   * Tests the Midi Mock sequencer for the mary had a little lamb song.
   */
  @Test
  public void testMidiViewFullSong() throws IOException {
    String line = null;
    String fileName = "mary-little-lamb.txt";

    MusicReader reader = new MusicReader();
    MusicEditorModelImpl.Builder builder = new MusicEditorModelImpl.Builder();
    MusicEditorOperations model = reader.parseFile(new FileReader(fileName), builder);
    MusicEditorController controller;
    controller = new MusicEditorController(midiView, model);
    controller.startApplication();

    String myLog = MockSequencer.EVENT_LOG.toString();


    FileReader fin = new FileReader("mary-little-lamb.txt");
    BufferedReader bin = new BufferedReader(fin);
    line = bin.readLine(); //We can disregard the first line (tempo)

    while ((line = bin.readLine()) != null) {
      Scanner sc = new Scanner(line);
      String note = sc.next();
      int startBeat = sc.nextInt();
      int endBeat = sc.nextInt() - 1;
      int instrument = sc.nextInt();
      int pitch = sc.nextInt();
      int volume = sc.nextInt();

      String noteStartEvent = "Beat: " + startBeat + " Command: NOTE_ON Pitch: "
              +
              IPitch.midiToPitch(pitch) + " Volume: " + volume;
      String noteEndEvent = "Beat: " + endBeat + " Command: NOTE_OFF Pitch: "
              +
              IPitch.midiToPitch(pitch) + " Volume: " + volume;

      assertTrue(myLog.contains(noteStartEvent));
      assertTrue(myLog.contains(noteEndEvent));
    }
  }
}