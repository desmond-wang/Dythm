package troid.dythm.ui.page;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import troid.dythm.game.map.Beatmap;
import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.ImageText;
import troid.dythm.ui.component.ProgressBar;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class StartPage extends Page {

    private static final float BLINK_SPEED = 0.05f;
    private static final int BLINK_MIN = 127;
    private static final int BLINK_MAX = 255;

    private final GameView gameView;

    private final Image background;
    private final ImageText title;
    private final Text touchToStart;
    private final Text progressText;
    private final ProgressBar progressBar;

    private volatile boolean loaded;
    private float blinkTime;

    public StartPage(GameView gameView) {
        super();

        this.gameView = gameView;

        List<String> mapDirs = ResourceHelper.listDirectories("map/");

        background = new Image(ResourceHelper.getImagePath("bg/start"), Image.Scaling.FILL);
        addElement(background);

        title = new ImageText("Dythm", ResourceHelper.getImagePath("icon/title"));
        addElement(title);

        touchToStart = new Text("Touch Screen To Start");

        progressText = new Text();

        progressBar = new ProgressBar();
        progressBar.setMaximum(mapDirs.size());

        Thread worker = new Thread(() -> {
            int progress = 0;
            for (String mapDir : mapDirs) {
                for (String mapFile : ResourceHelper.listFiles(mapDir, "osu")) {
                    List<String> content = ResourceHelper.readFile(mapDir + mapFile);
                    Beatmap beatmap;
                    try {
                        beatmap = new Beatmap(ResourceHelper.getHome() + mapDir, content);
                    } catch (Throwable e) {
                        System.err.println("Failed to load " + mapFile + ": " + e.getMessage());
                        continue;
                    }

                    gameView.getGame().getBeatmaps().add(beatmap);
                }

                ++progress;
                synchronized (this) {
                    progressText.setText("Loading: " + progress + " / " + mapDirs.size() + "...");
                    progressBar.setValue(progress);
                }
            }

            loaded = true;
        });
        worker.start();
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        background.resize(x - 5, y - 5, width + 10, height + 10);
        title.resize(x, y + height / 4, width, height / 2);
        touchToStart.resize(x, y + height * 5 / 6, width, height / 15);
        progressText.resize(x + width / 20, y + height * 3 / 4, width * 9 / 10, height / 15);
        progressBar.resize(x + width / 20, y + height * 5 / 6, width * 9 / 10, height / 15);
    }

    @Override
    public boolean pointerUp(int x, int y) {
        if (super.pointerUp(x, y))
            return true;

        if (loaded)
            gameView.setPage(new MenuPage(gameView));

        return loaded;
    }

    @Override
    public void update(GameView gameView) {
        super.update(gameView);

        if (!loaded)
            return;

        float center = BLINK_MIN + BLINK_MAX;
        float range = (BLINK_MAX - BLINK_MIN) / 2f;
        int alpha = (int) (center + range * Math.sin(blinkTime));
        touchToStart.setForeground(Color.argb(alpha, 255, 255, 255));
        blinkTime += BLINK_SPEED;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        super.draw(canvas, paint);

        if (loaded) {
            touchToStart.draw(canvas, paint);
        } else {
            synchronized (this) {
                progressText.draw(canvas, paint);
                progressBar.draw(canvas, paint);
            }
        }
    }
}
