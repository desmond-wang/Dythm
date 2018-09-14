package troid.dythm.game.achievement;

import troid.dythm.game.profile.Profile;

public interface Achievement {
    String getName();
    String getDescription();
    boolean isAchieved(Profile profile);

    default String getId() {
        return getClass().getSimpleName();
    }
}
