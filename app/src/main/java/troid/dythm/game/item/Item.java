package troid.dythm.game.item;

import troid.dythm.game.play.PlayObserver;

public interface Item extends PlayObserver {

    String getDescription();
    int getPrice();
    Item copy();

    default String getId() {
        return getClass().getSimpleName();
    }
}
