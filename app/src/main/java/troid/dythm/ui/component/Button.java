package troid.dythm.ui.component;

import android.graphics.Canvas;
import android.graphics.Paint;

import troid.dythm.ui.base.ActionListener;
import troid.dythm.ui.base.Component;

public class Button extends Component {

    private static final int CLICK_THRESHOLD = 20;

    private Component content;
    private ActionListener listener = null;

    private boolean pressed = false;
    private int downX;
    private int downY;

    public Button(Component content) {
        super();

        this.content = content;
    }

    public Button(String name) {
        this(new ImageText(name));
    }

    public Component getContent() {
        return content;
    }

    public void setAction(ActionListener listener) {
        this.listener = listener;
    }

    private boolean done() {
        boolean p = pressed;
        pressed = false;
        return p;
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);
        content.resize(x, y, width, height);
    }

    @Override
    public boolean pointerDown(int x, int y) {
        if (contains(x, y)) {
            pressed = true;
            downX = x;
            downY = y;
        }
        return pressed;
    }

    @Override
    public boolean pointerMove(int x, int y) {
        if (pressed && (Math.abs(x - downX) > CLICK_THRESHOLD || Math.abs(y - downY) > CLICK_THRESHOLD))
            pressed = false;

        return pressed;
    }

    @Override
    public boolean pointerUp(int x, int y) {
        if (pressed && listener != null) {
            listener.execute();
        }
        return done();
    }

    @Override
    public boolean pointerCancel(int x, int y) {
        return done();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        content.draw(canvas, paint);

        if (pressed) {
            paint.setColor(0x64000000);
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

}
