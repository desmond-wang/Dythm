package troid.dythm.game.item;

import troid.dythm.game.play.Hit;
import troid.dythm.game.play.PlayState;
import troid.dythm.game.profile.Profile;

public class ComboBonus implements Item {
    private boolean given = false;
    private int effectNum = 300;
    private int scoreBonus = 100_000;

    @Override
    public String getDescription() {
        return "Bonus score " + scoreBonus + " when reaching " + effectNum + " combo";
    }

    @Override
    public int getPrice() {
        return scoreBonus / Profile.SCORE_COIN_RATIO / 2;
    }

    @Override
    public ComboBonus copy() {
        return new ComboBonus();
    }

    @Override
    public void onHit(PlayState state, Hit hit) {
        if (!given && state.getCombo() >= effectNum) {
            state.setScore(state.getScore() + scoreBonus);
            given = true;
        }
    }
}
