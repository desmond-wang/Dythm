package troid.dythm.game.achievement;

import troid.dythm.game.profile.Profile;

public class FirstPlay implements Achievement {
    @Override
    public String getName() {
        return "First Play";
    }

    @Override
    public String getDescription() {
        return "Finish your first game";
    }

    @Override
    public boolean isAchieved(Profile profile) {
        return profile.getPlayCount() > 0;
    }
}
