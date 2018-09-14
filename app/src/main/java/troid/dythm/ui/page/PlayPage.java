package troid.dythm.ui.page;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

import troid.dythm.game.map.Beatmap;
import troid.dythm.game.play.Hit;
import troid.dythm.game.play.Play;
import troid.dythm.game.play.PlayState;
import troid.dythm.ui.GameView;
import troid.dythm.ui.audio.Music;
import troid.dythm.ui.base.Component;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.ProgressBar;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.play.CatchButton;
import troid.dythm.ui.play.PlayArea;

public class PlayPage extends Page {

    private static final long JUDGEMENT_DURATION = 1000;

    private GameView gameView;
    private Play play;
    private Music music;

    private Image background;
    private PlayArea playArea;
    private CatchButton leftCatch;
    private CatchButton rightCatch;
    private Text judgement;
    private Text combo;
    private Text score;
    private Text accuracy;
    private ProgressBar progressBar;
    private Button pause;

    private List<Component> pausedComponents;
    private Button resume;
    private Button replay;
    private Button back;

    PlayPage(GameView gameView, Beatmap beatmap) {
        super();

        gameView.getGame().startPlay(beatmap);

        this.gameView = gameView;
        play = gameView.getGame().getPlay();
        music = gameView.getMusic();


        background = new Image(beatmap.getBackground(), Image.Scaling.FILL);
        addElement(background);

        playArea = new PlayArea(this);
        addElement(playArea);

        leftCatch = new CatchButton();
        leftCatch.setAction(() -> play.catchLeft());
        addElement(leftCatch);

        rightCatch = new CatchButton();
        rightCatch.setAction(() -> play.catchRight());
        addElement(rightCatch);

        judgement = new Text(Text.Alignment.CENTER);
        judgement.setForeground(0xFFF49535);
        addElement(judgement);

        combo = new Text();
        combo.setForeground(0xFFF49535);
        addElement(combo);

        score = new Text(Text.Alignment.LEFT);
        score.setBackground(0x7F000000);
        addElement(score);

        accuracy = new Text(Text.Alignment.LEFT);
        accuracy.setBackground(0x7F000000);
        addElement(accuracy);

        progressBar = new ProgressBar();
        addElement(progressBar);

        pause = new Button("control/pause");
        pause.setAction(() -> music.pause());
        addElement(pause);


        pausedComponents = new ArrayList<>();

        resume = new Button("control/play");
        resume.setAction(() -> music.resume());
        pausedComponents.add(resume);

        replay = new Button("control/replay");
        replay.setAction(() -> leave(new PlayPage(gameView, beatmap)));
        pausedComponents.add(replay);

        back = new Button("control/back");
        back.setAction(() -> leave(new SelectionPage(gameView, beatmap)));
        pausedComponents.add(back);


        music.load(beatmap);
        music.setMode(Music.Mode.ONCE);
        music.start();

        progressBar.setMaximum(music.getDuration());
    }

    public Play getPlay() {
        return play;
    }

    public long getAudioTime() {
        return music.getPosition();
    }

    private void leave(Page page) {
        gameView.getGame().endPlay();
        gameView.setPage(page);
    }

    @Override
    public void onMusicCompletion() {
        gameView.getGame().endPlay();

        PlayState playState = play.getState();
        gameView.getGame().getProfile().updateProfile(playState);
        gameView.save();
        gameView.setPage(new ResultPage(gameView, playState));
    }

    @Override
    public void onRotation(float[] values) {
        if (!music.isPlaying())
            return;

        float[] mat = new float[9];
        float[] angles = new float[3];
        SensorManager.getRotationMatrixFromVector(mat, values);
        SensorManager.getOrientation(mat, angles);
        float roll = angles[1];

        playArea.onRotation(roll);
    }

    @Override
    public void onPause() {
        music.pause();
    }

    @Override
    public void onBack() {
        if (music.isPlaying())
            music.pause();
        else
            music.resume();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        super.draw(canvas, paint);

        if (!music.isPlaying()) {
            paint.setARGB(200, 192, 192, 192);
            canvas.drawRect(x, y, width, height, paint);
            for (Component component : pausedComponents) {
                component.draw(canvas, paint);
            }
        }
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        int catchWidth = width / 8;

        background.resize(x, y, width, height);
        playArea.resize(x + catchWidth, y + height / 12, width - catchWidth * 2, height * 11 / 12);
        leftCatch.resize(x, y + height / 2, catchWidth, height / 2);
        rightCatch.resize(x + width - catchWidth, y + height / 2, catchWidth, height / 2);
        judgement.resize(x + width * 3 / 8, y + height * 3 / 4, width / 4, height / 8);
        combo.resize(x + width * 3 / 8, y + height / 2, width / 4, height / 8);
        score.resize(x + width / 16, y + height / 16, width / 4, height / 16);
        accuracy.resize(x + width / 16, y + height * 2 / 16, width / 4, height / 16);
        progressBar.resize(x + width / 8, y + height / 64, width * 3 / 4, height / 32);
        pause.resize(x + width - height * 3 / 16, y + height / 16, height / 8, height / 8);

        int buttonSize = height / 3;
        int buttonY = height / 2 - buttonSize / 2;
        resume.resize(width / 4 - buttonSize / 2, buttonY, buttonSize, buttonSize);
        replay.resize(width / 2 - buttonSize / 2, buttonY, buttonSize, buttonSize);
        back.resize(width * 3 / 4 - buttonSize / 2, buttonY, buttonSize, buttonSize);
    }

    @Override
    public boolean pointerDown(int x, int y) {
        if (music.isPlaying()) {
            return super.pointerDown(x, y);
        } else {
            for (int i = pausedComponents.size() - 1; i >= 0; --i) {
                if (pausedComponents.get(i).pointerDown(x, y))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean pointerDownMulti(int x, int y) {
        if (music.isPlaying()) {
            return super.pointerDownMulti(x, y);
        } else {
            for (int i = pausedComponents.size() - 1; i >= 0; --i) {
                if (pausedComponents.get(i).pointerDownMulti(x, y))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean pointerMove(int x, int y) {
        if (music.isPlaying()) {
            return super.pointerMove(x, y);
        } else {
            for (int i = pausedComponents.size() - 1; i >= 0; --i) {
                if (pausedComponents.get(i).pointerMove(x, y))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean pointerUpMulti(int x, int y) {
        if (music.isPlaying()) {
            return super.pointerUpMulti(x, y);
        } else {
            for (int i = pausedComponents.size() - 1; i >= 0; --i) {
                if (pausedComponents.get(i).pointerUpMulti(x, y))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean pointerUp(int x, int y) {
        if (music.isPlaying()) {
            return super.pointerUp(x, y);
        } else {
            for (int i = pausedComponents.size() - 1; i >= 0; --i) {
                if (pausedComponents.get(i).pointerUp(x, y))
                    return true;
            }
            return false;
        }
    }

    @Override
    public boolean pointerCancel(int x, int y) {
        if (music.isPlaying()) {
            return super.pointerCancel(x, y);
        } else {
            for (int i = pausedComponents.size() - 1; i >= 0; --i) {
                if (pausedComponents.get(i).pointerCancel(x, y))
                    return true;
            }
            return false;
        }
    }

    @Override
    public void update(GameView gameView) {
        super.update(gameView);

        if (!music.isPlaying())
            return;

        long audioTime = getAudioTime();
        play.update(audioTime);

        List<Hit> hits = play.getState().getHits();
        if (hits.size() > 0) {
            Hit lastHit = hits.get(hits.size() - 1);
            if (audioTime - lastHit.getTime() < JUDGEMENT_DURATION) {
                judgement.setText(lastHit.getJudgement().toString());
            } else {
                judgement.clearText();
            }
        }

        combo.setText(Integer.toString(play.getState().getCombo()));
        score.setText("Score: " + play.getState().getScore());
        accuracy.setText("Accuracy: " + play.getState().getAccuracy() + "%");

        progressBar.setValue(audioTime);
    }

}
