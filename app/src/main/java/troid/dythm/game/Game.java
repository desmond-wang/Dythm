package troid.dythm.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import troid.dythm.game.item.Item;
import troid.dythm.game.map.Beatmap;
import troid.dythm.game.play.Play;
import troid.dythm.game.play.PlayState;
import troid.dythm.game.profile.Profile;

public class Game {
    private List<Beatmap> beatmaps = new ArrayList<>();
    private Profile profile = new Profile();
    private Play play = null;
    private Set<Item> selectedItems = new HashSet<>();

    public void startPlay(Beatmap beatmap) {
        play = new Play(beatmap);

        selectedItems.clear();
        selectedItems.addAll(profile.getItems().keySet());

        PlayState state = play.getState();
        state.setSpeed(profile.getSpeed());
        state.setSliderSensitivity(profile.getSliderSensitivity());

        for (Item item : selectedItems)
            play.addObserver(item.copy());
    }

    public void endPlay() {
        if (play != null)
            play.end();

        for (Item item : selectedItems)
            profile.removeItem(item);

        play = null;
    }

    public List<Beatmap> getBeatmaps() {
        return beatmaps;
    }

    public Play getPlay() {
        return play;
    }

    public Profile getProfile() {
        return profile;
    }
}
