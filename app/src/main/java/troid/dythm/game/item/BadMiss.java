package troid.dythm.game.item;

import troid.dythm.game.play.Hit;
import troid.dythm.game.play.HitJudgement;
import troid.dythm.game.play.PlayState;

public class BadMiss implements Item {
    private int count = 1;

    @Override
    public String getDescription() {
        return "Change " + (count == 1 ? "1 miss" : count + " misses") + " to bad";
    }

    @Override
    public int getPrice() {
        return 50 * count * count;
    }

    @Override
    public BadMiss copy() {
        return new BadMiss();
    }

    @Override
    public void onHit(PlayState state, Hit hit) {
        if (hit.getJudgement() == HitJudgement.MISS && count != 0) {
            hit.setJudgement(HitJudgement.BAD);
            count--;
        }
    }
}
