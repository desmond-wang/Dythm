package troid.dythm.game.play;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

import troid.dythm.game.map.Beatmap;
import troid.dythm.game.map.BeatmapNote;

public class Play {
    private PlayState state;
    private int nextNoteIndex = 0;
    private long lastUpdate;
    private long holdNoteStartTime;

    private List<PlayObserver> observers = new ArrayList<>();

    public Play(Beatmap beatmap) {
        state = new PlayState(beatmap);
    }

    public void clickColumn(int column) {
        Queue<PlayNote> activeNotes = state.getColumns().get(column).getActiveNotes();

        // empty click, ignore
        if (activeNotes.isEmpty())
            return;

        long timeDiff = lastUpdate - activeNotes.peek().getTime();
        HitJudgement judgement = state.calcHitJudgement(timeDiff);

        // outside hit window, ignore
        if (judgement == null)
            return;

        activeNotes.remove();
        addHit(new Hit(judgement, lastUpdate, column, false));
    }


    public void catchLeft() {
        int numColumns = state.getNumColumns();
        // include central column if numColumns is odd
        catchColumns(0, numColumns - numColumns / 2);
    }

    public void catchRight() {
        int numColumns = state.getNumColumns();
        catchColumns(numColumns / 2, numColumns);
    }

    public void moveSlider(int starColumn, int endColumn) {
        state.setSliderColumns(starColumn, endColumn);

        Queue<PlayHoldPoint> holdPoints = state.getHoldPoints();
        PlayHoldPoint activePoint = state.getActiveHoldPoint();

        if (activePoint != null) {
            // slider is catching the current hold point

            if (state.isInSliderColumns(activePoint.getColumn())) {
                // we are still in column, all good
                // no need to handle activePoint.getTime() < lastUpdate since it's done in update()
                return;
            }

            endHold();
        }

        if (holdPoints.isEmpty())
            return;

        PlayHoldPoint holdPoint = holdPoints.peek();
        if (holdPoint.getTime() > lastUpdate)
            return;

        if (!state.isInSliderColumns(holdPoint.getColumn()))
            return;

        startHold();
    }

    public void end() {
        for (PlayObserver observer : observers)
            observer.onComplete(state);
    }

    public void update(long time) {
        lastUpdate = time;

        // add new notes
        List<BeatmapNote> notes = state.getBeatmap().getNotes();
        long endTime = lastUpdate + state.getSpeed();

        for (; nextNoteIndex < notes.size(); ++nextNoteIndex) {
            BeatmapNote note = notes.get(nextNoteIndex);
            if (note.getTime() > endTime)
                break;

            if (!note.isHoldNote()) {
                // normal note
                PlayColumn column = state.getColumns().get(note.getColumn());
                column.getActiveNotes().add(new PlayNote(note.getTime()));
            } else {
                // hold note
                Deque<PlayHoldPoint> holdPoints = state.getHoldPoints();
                if (holdPoints.isEmpty()) {
                    // no previous hold point, just add
                    holdPoints.add(new PlayHoldPoint(note.getTime(), note.getColumn()));
                    holdPoints.add(new PlayHoldPoint(note.getEndTime()));
                } else {
                    PlayHoldPoint holdPoint = holdPoints.getLast();
                    if (note.getTime() > holdPoint.getTime()) {
                        // there's space between previous end time and current start time, just add
                        holdPoints.add(new PlayHoldPoint(note.getTime(), note.getColumn()));
                        holdPoints.add(new PlayHoldPoint(note.getEndTime()));
                    } else if (note.getEndTime() > holdPoint.getTime()) {
                        // overlap with previous, remove previous end and add remaining
                        holdPoints.removeLast();
                        holdPoints.add(new PlayHoldPoint(holdPoint.getTime(), note.getColumn()));
                        holdPoints.add(new PlayHoldPoint(note.getEndTime()));
                    }
                }
            }
        }

        // remove missed notes

        // remove normal notes
        // use BAD here since if the note is already in MISS there's no way to hit it
        long missTime = lastUpdate - state.getHitWindow(HitJudgement.BAD);
        for (int i = 0; i < state.getNumColumns(); ++i) {
            PlayColumn column = state.getColumn(i);
            Queue<PlayNote> playNotes = column.getActiveNotes();
            while (!playNotes.isEmpty() && playNotes.peek().getTime() < missTime) {
                playNotes.remove();
                addHit(new Hit(HitJudgement.MISS, lastUpdate, i, false));
            }
        }

        // remove hold notes
        Queue<PlayHoldPoint> holdPoints = state.getHoldPoints();
        // now we switch to hold note's hit window
        missTime = lastUpdate - state.getHoldWindow(HitJudgement.BAD);
        while (!holdPoints.isEmpty()) {
            PlayHoldPoint holdPoint = holdPoints.peek();
            if (holdPoint.getTime() > lastUpdate)
                break;

            PlayHoldPoint activePoint = state.getActiveHoldPoint();
            if (activePoint != null)
                endHold();

            if (state.isInSliderColumns(holdPoint.getColumn())) {
                startHold();
            } else if (holdPoint.getTime() < missTime) {
                holdPoints.remove();
                if (holdPoints.peek().isEnd())
                    holdPoints.remove();
                addHit(new Hit(HitJudgement.MISS, lastUpdate, holdPoint.getColumn(), true));
            } else {
                // wait for the player to (possibly) move the slider to catch it
                break;
            }
        }
    }

    public void addObserver(PlayObserver observer) {
        observers.add(observer);
    }

    public PlayState getState() {
        return state;
    }

    private void addHit(Hit hit) {
        for (PlayObserver observer : observers)
            observer.onHit(state, hit);

        state.addHit(hit);
    }

    private void startHold() {
        Queue<PlayHoldPoint> holdPoints = state.getHoldPoints();

        PlayHoldPoint startPoint = holdPoints.peek();

        holdNoteStartTime = lastUpdate;
        state.setActiveHoldPoint(startPoint);
        holdPoints.remove();
    }

    private void endHold() {
        Queue<PlayHoldPoint> holdPoints = state.getHoldPoints();

        PlayHoldPoint startPoint = state.getActiveHoldPoint();
        PlayHoldPoint endPoint = holdPoints.peek();

        long actualDiff = endPoint.getTime() - startPoint.getTime();
        long userDiff = lastUpdate - holdNoteStartTime;
        long timeDiff = userDiff - actualDiff;

        HitJudgement judgement = state.calcHoldJudgement(timeDiff);
        if (judgement == null) {
            // window outside miss is still a miss
            judgement = HitJudgement.MISS;
        }

        if (endPoint.isEnd())
            holdPoints.remove();

        state.setActiveHoldPoint(null);
        addHit(new Hit(judgement, lastUpdate, startPoint.getColumn(), true));
    }

    private void catchColumns(int startColumn, int endColumn) {
        List<CatchInfo> columns = new ArrayList<>();
        long nearestTime = Long.MAX_VALUE;

        long missTime = lastUpdate + state.getHitWindow(HitJudgement.MISS);
        for (int i = startColumn; i < endColumn; ++i) {
            Queue<PlayNote> activeNotes = state.getColumn(i).getActiveNotes();
            if (activeNotes.isEmpty())
                continue;

            // if it's outside miss range, ignore
            int noteTime = activeNotes.peek().getTime();
            if (noteTime > missTime)
                continue;

            // if before nearest time, update
            nearestTime = Math.min(nearestTime, noteTime);

            columns.add(new CatchInfo(i, noteTime));
        }

        long catchTime = nearestTime + state.getCatchThreshold();
        List<CatchInfo> nearColumns = new ArrayList<>();
        for (int i = 0; i < columns.size(); ++i) {
            CatchInfo info = columns.get(i);
            if (info.time < catchTime)
                nearColumns.add(info);
        }

        // catch is for at least two columns
        if (columns.size() < 2)
            return;

        for (CatchInfo info : nearColumns)
            clickColumn(info.column);
    }

    private static class CatchInfo {
        int column;
        long time;

        CatchInfo(int column, long time) {
            this.column = column;
            this.time = time;
        }
    }
}
