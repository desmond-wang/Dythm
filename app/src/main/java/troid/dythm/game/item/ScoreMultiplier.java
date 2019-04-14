package troid.dythm.game.item;

import troid.dythm.game.play.PlayState;

public class ScoreMultiplier implements Item {
    private int multiplier = 2;

    @Override
    public String getDescription() {
        return multiplier + "x the final score";
    }

    @Override
    public int getPrice() {
        return 100 * multiplier;
    }

    @Override
    public ScoreMultiplier copy() {
        return new ScoreMultiplier();
    }

    @Override
    public void onComplete(PlayState state) {
        state.setScore(state.getScore() * multiplier);
    }
}
