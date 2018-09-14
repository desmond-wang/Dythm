package troid.dythm.game.achievement;

import troid.dythm.game.profile.Profile;

public class Millionaire implements Achievement {
    @Override
    public String getName() {
        return "Millionaire";
    }

    @Override
    public String getDescription() {
        return "Earn 1,000,000 coins";
    }

    @Override
    public boolean isAchieved(Profile profile) {
        return profile.getCoinsEarned() >= 1_000_000;
    }
}
