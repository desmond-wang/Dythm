package troid.dythm.game.achievement;

import troid.dythm.game.play.PlayRank;
import troid.dythm.game.profile.Profile;

public class SSRanker implements Achievement {
    @Override
    public String getName() {
        return "SS-Ranker";
    }

    @Override
    public String getDescription() {
        return "Achieve 10 SS ranks";
    }

    @Override
    public boolean isAchieved(Profile profile) {
        return profile.getRankCount(PlayRank.SS) >= 10;
    }
}
