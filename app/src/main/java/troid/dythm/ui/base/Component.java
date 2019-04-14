package troid.dythm.ui.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import troid.dythm.ui.GameView;

public abstract class Component {

    protected int x;

    protected int y;

    protected int width;

    protected int height;

    public Component() {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void resize(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(GameView gameView) {
    }

    public boolean pointerDown(int x, int y) {
        return false;
    }

    public boolean pointerMove(int x, int y) {
        return false;
    }

    public boolean pointerUp(int x, int y) {
        return false;
    }

    public boolean pointerDownMulti(int x, int y) {
        return false;
    }

    public boolean pointerUpMulti(int x, int y) {
        return false;
    }

    public boolean pointerCancel(int x, int y) {
        return false;
    }

    public boolean contains(int x, int y) {
        return this.width > 0 && this.height > 0  // check for empty first
                && x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public abstract void draw(Canvas canvas, Paint paint);
}
