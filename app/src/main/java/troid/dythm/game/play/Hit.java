package troid.dythm.game.play;

public class Hit {
    private long time;
    private int column;
    private HitJudgement judgement;
    private boolean isHold;

    Hit(HitJudgement judgement, long time, int column, boolean isHold) {
        this.judgement = judgement;
        this.time = time;
        this.column = column;
        this.isHold = isHold;
    }

    public HitJudgement getJudgement() {
        return judgement;
    }

    public long getTime() {
        return time;
    }

    public int getColumn() {
        return column;
    }

    public void setJudgement(HitJudgement judgement) {
        this.judgement = judgement;
    }

    public boolean isHold() {
        return isHold;
    }
}
