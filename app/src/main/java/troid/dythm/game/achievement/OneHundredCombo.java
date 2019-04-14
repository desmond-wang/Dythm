package troid.dythm.game.achievement;

import troid.dythm.game.profile.Profile;

public class OneHundredCombo implements Achievement {
    @Override
    public String getName() {
        return "100 Combo";
    }

    @Override
    public String getDescription() {
        return "Reach 100 combo";
    }

    @Override
    public boolean isAchieved(Profile profile) {
        return profile.getMaxCombo() >= 100;
    }
}
