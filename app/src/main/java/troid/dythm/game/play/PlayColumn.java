package troid.dythm.game.play;

import java.util.ArrayDeque;
import java.util.Queue;

public class PlayColumn {
    private Queue<PlayNote> activeNotes = new ArrayDeque<>();

    public Queue<PlayNote> getActiveNotes() {
        return activeNotes;
    }
}
