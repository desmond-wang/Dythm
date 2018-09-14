package troid.dythm.game.play;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import troid.dythm.game.map.Beatmap;

public class PlayState {
    private static final long[] HIT_WINDOW = {50, 100, 200, 500};
    private static final long[] HOLD_WINDOW = {100, 200, 400, 1000};
    private static final int[] HIT_SCORE = {100, 50, 10, 0};
    private static final int[] HIT_ACCURACY = {100, 50, 25, 0};
    private static final int[] RANK_ACCURACY = {100, 95, 90, 80, 70, 50, 0};

    private Beatmap beatmap;

    // current state
    private long speed;
    private float sliderSensitivity;
    private List<PlayColumn> columns;
    private Deque<PlayHoldPoint> holdPoints = new ArrayDeque<>();
    private PlayHoldPoint activeHoldPoint = null;
    private int sliderStartColumn = 0;
    private int sliderEndColumn = 0;

    // recorded stats
    private List<Hit> hits = new ArrayList<>();

    // generated stats
    private int combo = 0;
    private int maxCombo = 0;
    private int score = 0;
    private int accuracySum = 0;
    private int accuracy = 100;

    // total stats
    private int totalCombo = 0;
    private int totalScore = 0;

    PlayState(Beatmap beatmap) {
        this.beatmap = beatmap;

        columns = new ArrayList<>(beatmap.getNumColumns());
        for (int i = 0; i < beatmap.getNumColumns(); ++i) {
            columns.add(new PlayColumn());
        }
    }

    public void addHit(Hit hit) {
        hits.add(hit);

        // update combo
        if (hit.getJudgement() != HitJudgement.MISS)
            setCombo(combo + 1);
        else
            setCombo(0);
        totalCombo += 1;

        // update score
        score += getHitScore(hit.getJudgement(), combo, accuracy);
        totalScore += getHitScore(HitJudgement.PERFECT, totalCombo, 100);

        // update accuracy
        accuracySum += getHitAccuracy(hit.getJudgement());
        accuracy = accuracySum / hits.size();
    }

    private HitJudgement calcJudgement(long time, long[] window) {
        long timeDiff = Math.abs(time);

        for (int i = 0; i < window.length; ++i) {
            if (timeDiff <= window[i])
                return HitJudgement.values()[i];
        }

        return null;
    }

    public HitJudgement calcHitJudgement(long time) {
        return calcJudgement(time, HIT_WINDOW);
    }

    public HitJudgement calcHoldJudgement(long time) {
        return calcJudgement(time, HOLD_WINDOW);
    }

    public long getHitWindow(HitJudgement judgement) {
        return HIT_WINDOW[judgement.ordinal()];
    }

    public long getHoldWindow(HitJudgement judgement) {
        return HOLD_WINDOW[judgement.ordinal()];
    }

    public int getHitScore(HitJudgement judgement, int combo, int accuracy) {
        return HIT_SCORE[judgement.ordinal()] * ((int) (Math.log(combo + 1) * 10.0) + accuracy) / 100;
    }

    public int getHitAccuracy(HitJudgement judgement) {
        return HIT_ACCURACY[judgement.ordinal()];
    }

    public long getCatchThreshold() {
        return getHitWindow(HitJudgement.PERFECT);
    }

    public int getTotalCombo() {
        return totalCombo;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public PlayRank getRank() {
        for (int i = 0; i < RANK_ACCURACY.length; ++i) {
            if (accuracy >= RANK_ACCURACY[i])
                return PlayRank.values()[i];
        }
        return null;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }

    /**
     * time in ms of how long the player can see ahead
     */
    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public float getSliderSensitivity() {
        return sliderSensitivity;
    }

    public void setSliderSensitivity(float sliderSensitivity) {
        this.sliderSensitivity = sliderSensitivity;
    }

    @Deprecated
    public List<PlayColumn> getColumns() {
        return columns;
    }

    public int getNumColumns() {
        return columns.size();
    }

    public PlayColumn getColumn(int index) {
        return columns.get(index);
    }

    public Deque<PlayHoldPoint> getHoldPoints() {
        return holdPoints;
    }

    public PlayHoldPoint getActiveHoldPoint() {
        return activeHoldPoint;
    }

    public void setActiveHoldPoint(PlayHoldPoint activeHoldPoint) {
        this.activeHoldPoint = activeHoldPoint;
    }

    public void setSliderColumns(int startColumn, int endColumn) {
        sliderStartColumn = startColumn;
        sliderEndColumn = endColumn;
    }

    public int getSliderStartColumn() {
        return sliderStartColumn;
    }

    public int getSliderEndColumn() {
        return sliderEndColumn;
    }

    public boolean isInSliderColumns(int column) {
        return column >= sliderStartColumn && column < sliderEndColumn;
    }

    @Deprecated
    public List<Hit> getHits() {
        return hits;
    }

    public int getNumHits() {
        return hits.size();
    }

    public Hit getHit(int index) {
        return hits.get(index);
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
        maxCombo = Math.max(maxCombo, combo);
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public void setMaxCombo(int maxCombo) {
        this.maxCombo = maxCombo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}
