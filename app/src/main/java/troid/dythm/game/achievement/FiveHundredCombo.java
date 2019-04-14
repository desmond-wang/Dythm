package troid.dythm.game.achievement;

import troid.dythm.game.profile.Profile;

public class FiveHundredCombo implements Achievement {
    @Override
    public String getName() {
        return "500 Combo";
    }

    @Override
    public String getDescription() {
        return "Reach 500 combo";
    }

    @Override
    public boolean isAchieved(Profile profile) {
        return profile.getMaxCombo() >= 500;
    }
}
