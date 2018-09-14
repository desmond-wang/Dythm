package troid.dythm.ui.audio;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.Random;

import troid.dythm.game.map.Beatmap;
import troid.dythm.ui.GameView;

public class Music {

    private final GameView gameView;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Random random = new Random(System.currentTimeMillis());

    private Beatmap playing = null;
    private Mode mode = Mode.RANDOM;

    public Music(GameView gameView) {
        this.gameView = gameView;

        onReset();
    }

    public void load(Beatmap beatmap) {
        mediaPlayer.reset();
        onReset();
        try {
            mediaPlayer.setDataSource(beatmap.getAudioFilename());
            mediaPlayer.prepare();
        } catch (IOException e) {
            System.err.println("Failed to load music: " + beatmap.getAudioFilename());
            playing = null;
            return;
        }

        playing = beatmap;
        gameView.getPage().onMusicChange();
    }

    public void seekTo(int msec) {
        if (playing == null)
            return;
        if (msec >= 0)
            mediaPlayer.seekTo(msec);
    }

    public void start() {
        if (playing == null)
            return;
        mediaPlayer.start();
    }

    public void pause() {
        if (playing == null)
            return;
        mediaPlayer.pause();
    }

    public void resume() {
        if (playing == null)
            return;
        mediaPlayer.start();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        onReset();
    }

    public Beatmap getPlayingBeatmap() {
        return playing;
    }

    public boolean isPlayingAudio(Beatmap beatmap) {
        return isPlaying() && playing != null && playing.getAudioFilename().equals(beatmap.getAudioFilename());
    }

    public int randomIndex() {
        return random.nextInt(gameView.getGame().getBeatmaps().size());
    }

    public void playRandom() {
        if (gameView.getGame().getBeatmaps().size() == 0)
            return;

        load(gameView.getGame().getBeatmaps().get(randomIndex()));
        start();
    }

    private void onReset() {
        mediaPlayer.setOnCompletionListener(this::onCompletion);
        mediaPlayer.setLooping(mode == Mode.REPEAT);
    }

    private void onCompletion(MediaPlayer mp) {
        // synchronized because this listener runs on a different thread
        synchronized (gameView) {
            if (mode == Mode.RANDOM) {
                playRandom();
            }
            gameView.getPage().onMusicCompletion();
        }
    }

    public enum Mode {
        ONCE,
        RANDOM,
        REPEAT,
    }

}
