package troid.dythm.game.achievement;

import troid.dythm.game.profile.Profile;

public class OneHundredPlays implements Achievement {
    @Override
    public String getName() {
        return "100 Plays";
    }

    @Override
    public String getDescription() {
        return "Complete 100 songs";
    }

    @Override
    public boolean isAchieved(Profile profile) {
        return profile.getPlayCount() >= 100;
    }
}
