package troid.dythm.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import troid.dythm.game.Game;
import troid.dythm.ui.audio.Music;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.page.StartPage;
import troid.dythm.ui.util.ResourceHelper;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements Runnable, SensorEventListener {

    private static final int UPS = 60;
    private static final int FPS = 60;
    private static final int SENSOR_SAMPLING_PERIOD = 10 * 1000;

    private Thread thread;
    private volatile boolean running;
    private SensorManager sensorManager;
    private Sensor rotation;

    private Game game;
    private Page page;
    private Music music;

    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context, SensorManager sensorManager) {
        super(context);

        game = new Game();
        music = new Music(this);

        this.sensorManager = sensorManager;
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        setOnTouchListener((v, event) -> {
            int x = (int) event.getX(event.getActionIndex());
            int y = (int) event.getY(event.getActionIndex());
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    synchronized (this) {
                        page.pointerDown(x, y);
                        page.pointerDownMulti(x, y);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    synchronized (this) {
                        page.pointerDownMulti(x, y);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    synchronized (this) {
                        page.pointerMove(x, y);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    synchronized (this) {
                        page.pointerUpMulti(x, y);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    synchronized (this) {
                        page.pointerUpMulti(x, y);
                        page.pointerUp(x, y);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    synchronized (this) {
                        page.pointerCancel(x, y);
                    }
                    break;
            }
            return true;
        });
        setKeepScreenOn(true);

        load();

        setPage(new StartPage(this));
    }

    @Override
    public void run() {
        final int NANO_PER_SECOND = 1_000_000_000;
        final int NANO_PER_MILLISECOND = 1_000_000;

        long nextUpdate, nextDraw;
        nextUpdate = nextDraw = Long.MIN_VALUE;
        while (running) {
            long time = System.nanoTime();

            // prioritize update before draw
            if (time >= nextUpdate) {
                update();
                nextUpdate = time + NANO_PER_SECOND / UPS;
            } else if (time >= nextDraw) {
                draw();
                nextDraw = time + NANO_PER_SECOND / FPS;
            } else {
                int sleep = (int) (Math.min(nextUpdate, nextDraw) - time);
                try {
                    Thread.sleep(sleep / NANO_PER_MILLISECOND, sleep % NANO_PER_MILLISECOND);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }

        save();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ROTATION_VECTOR:
                synchronized (this) {
                    page.onRotation(event.values);
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected synchronized void onSizeChanged(int w, int h, int oldw, int oldh) {
        page.resize(0, 0, w, h);
    }

    synchronized void onDestroy() {
        music.release();
    }

    void onPause() {
        running = false;

        synchronized (this) {
            page.onPause();
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            // ignore
        }

        sensorManager.unregisterListener(this);
    }

    void onResume() {
        sensorManager.registerListener(this, rotation, SENSOR_SAMPLING_PERIOD);

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    synchronized void onBack() {
        page.onBack();
    }

    private synchronized void update() {
        page.update(this);
    }

    private void draw() {
        SurfaceHolder holder = getHolder();
        if (!holder.getSurface().isValid())
            return;

        Canvas canvas = holder.lockCanvas();
        Paint paint = new Paint();

        synchronized (this) {
            page.draw(canvas, paint);
        }

        holder.unlockCanvasAndPost(canvas);
    }

    public void load() {
        game.getProfile().load(ResourceHelper.readFile(ResourceHelper.getProfilePath()));
        ResourceHelper.setTheme(game.getProfile().getTheme());
    }

    public void save() {
        ResourceHelper.writeFile(ResourceHelper.getProfilePath(), game.getProfile().save());
    }

    public void setTheme(String theme) {
        game.getProfile().setTheme(theme);
        ResourceHelper.setTheme(theme);
    }

    public Game getGame() {
        return game;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        page.resize(0, 0, getWidth(), getHeight());
    }

    public Music getMusic() {
        return music;
    }
}
