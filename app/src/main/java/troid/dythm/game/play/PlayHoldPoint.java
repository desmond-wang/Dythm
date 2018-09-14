package troid.dythm.game.play;

public class PlayHoldPoint {
    private int time;
    private int column;

    PlayHoldPoint(int time, int column) {
        this.time = time;
        this.column = column;
    }

    PlayHoldPoint(int time) {
        this.time = time;
        this.column = -1;
    }

    public int getTime() {
        return time;
    }

    public int getColumn() {
        return column;
    }

    public boolean isEnd() {
        return column == -1;
    }
}
