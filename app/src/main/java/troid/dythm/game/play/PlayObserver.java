package troid.dythm.game.play;

public interface PlayObserver {
    default void onHit(PlayState state, Hit hit) {}
    default void onComplete(PlayState state) {}
}
