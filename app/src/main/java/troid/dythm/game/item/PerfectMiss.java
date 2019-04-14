package troid.dythm.game.item;

import troid.dythm.game.play.Hit;
import troid.dythm.game.play.HitJudgement;
import troid.dythm.game.play.PlayState;

public class PerfectMiss implements Item {
    private int count = 1;

    @Override
    public String getDescription() {
        return "Change " + (count == 1 ? "1 miss" : count + " misses") + " to perfect";
    }

    @Override
    public int getPrice() {
        return 100 * count * count;
    }

    @Override
    public PerfectMiss copy() {
        return new PerfectMiss();
    }

    @Override
    public void onHit(PlayState state, Hit hit) {
        if (hit.getJudgement() == HitJudgement.MISS && count != 0) {
            hit.setJudgement(HitJudgement.PERFECT);
            count--;
        }
    }
}
