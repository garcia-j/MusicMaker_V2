package cs3500.music.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cs3500.music.provider.IBar;
import cs3500.music.provider.Note;

/**
 * Created by DavidThornton on 4/15/17.
 */
public class Bar implements IBar {

  Map<Integer, List<Note>> notes;
  int beats;

  public Bar() {
    this.notes = new HashMap<Integer, List<Note>>();
    this.beats = 4;
  }

  @Override
  public void addNote(Note newNote, int beatIndex) {
    if (!notes.containsKey(beatIndex)) {
      notes.put(beatIndex, new ArrayList<Note>());
    }

    notes.get(beatIndex).add(newNote);
  }

  @Override
  public void removeNote(int beatIndex, int pos) {

  }

  @Override
  public Note removeNote(int beatIndex, cs3500.music.provider.Pitch p, int oct) {
    List<Note> beat = notes.get(beatIndex);

    for (int i = 0; i < beat.size(); i++) {
      Note n = beat.get(i);
      if (n.getPitch() == p && n.getOctave() == oct) {
        notes.get(beatIndex).remove(i);
        return n;
      }
    }

    throw new IllegalArgumentException("Note does not exist");
  }

  @Override
  public void editNote(int beatIndex, int pos, Note newNote) {

  }

  @Override
  public Note noteAt(int beatIndex, int pos) {
    return null;
  }

  @Override
  public Note getNote(int beatIndex, cs3500.music.provider.Pitch p, int octave) {
    List<Note> notesAtBeat = notes.get(beatIndex);

    for (Note n : notesAtBeat) {
      if (n.getOctave() == octave && n.getPitch() == p) {
        return n;
      }
    }

    return null;
  }

  @Override
  public boolean hasNote(int beatIndex, cs3500.music.provider.Pitch p, int octave) {
    List<Note> notesAtBeat = notes.get(beatIndex);

    for (Note n : notesAtBeat) {
      if (n.getPitch() == p && n.getOctave() == octave) {
        return true;
      }
    }

    return false;
  }

  @Override
  public Note topNote() {
    if (notes.size() == 0) {
      return null;
    }

    TreeMap<Integer, List<Note>> sortedNotes = new TreeMap<Integer, List<Note>>(notes);
    Note result = notes.get(sortedNotes.firstKey()).get(0);

    for (Integer i : notes.keySet()) {
      List<Note> notesAtBeat = notes.get(i);
      for (Note n : notesAtBeat) {
        if (n.getOctave() > result.getOctave()
                ||
                (n.getOctave() == result.getOctave()
                &&
                n.getPitch().ordinal() > result.getPitch().ordinal())) {
          result = n;
        }
      }
    }

    return result;
  }

  @Override
  public Note bottomNote() {
    if (notes.size() == 0) {
      return null;
    }

    TreeMap<Integer, List<Note>> sortedNotes = new TreeMap<Integer, List<Note>>(notes);
    Note result = notes.get(sortedNotes.firstKey()).get(0);

    for (Integer i : notes.keySet()) {
      List<Note> notesAtBeat = notes.get(i);
      for (Note n : notesAtBeat) {
        if (n.getOctave() < result.getOctave()
                ||
                (n.getOctave() == result.getOctave()
                        &&
                        n.getPitch().ordinal() < result.getPitch().ordinal())) {
          result = n;
        }
      }
    }

    return result;
  }

  @Override
  public int getBeats() {
    return beats;
  }

  @Override
  public List<Note> getBeat(int beatIndex) {
    if (!notes.containsKey(beatIndex)) {
      return new ArrayList<Note>();
    }

    return notes.get(beatIndex);
  }
}
