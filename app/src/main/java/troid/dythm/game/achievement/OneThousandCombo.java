package troid.dythm.game.achievement;

import troid.dythm.game.profile.Profile;

public class OneThousandCombo implements Achievement {

    @Override
    public String getName() {
        return "1000 Combo";
    }

    @Override
    public String getDescription() {
        return "Reach 1,000 combo";
    }

    @Override
    public boolean isAchieved(Profile profile) {
        return profile.getMaxCombo() >= 1000;
    }
}
