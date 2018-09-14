package troid.dythm.ui.component;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Arrays;
import java.util.List;

import troid.dythm.ui.GameView;
import troid.dythm.ui.base.ActionListener;
import troid.dythm.ui.base.Component;
import troid.dythm.ui.base.Container;
import troid.dythm.ui.util.MathHelper;

public class Selector extends Container {

    private static final float ALIGN_SPEED_FACTOR = 0.1f;
    private static final float ALIGN_STOP_THRESHOLD = 0.01f;
    private static final float INERTIA_DECAY_FACTOR = 0.9f;
    private static final float INERTIA_STOP_THRESHOLD = 0.01f;
    private static final int CLICK_THRESHOLD = 20;

    // config
    private boolean vertical = false;
    private int elementWidthFactor = 3;
    private int elementHeightFactor = 3;
    private int elementDistanceFactor = 3;
    private float fadeDistance = 1f;
    private float fadeScale = 0.5f;
    private ActionListener selectionChanged = null;

    // state
    private float focus = -3f;
    private int selection = 0;

    // drag selection
    private boolean dragging = false;
    private int prevPixel;
    private int nextPixel;

    // drag inertia sliding
    private boolean sliding = false;
    private float inertia;

    // pointer
    private boolean inbound;
    private boolean pressed;
    private int downX;
    private int downY;

    public Selector(Component... elements) {
        super(Arrays.asList(elements));
    }

    public Selector() {
        super();
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public void setElementFactors(int widthFactor, int heightFactor) {
        elementWidthFactor = widthFactor;
        elementHeightFactor = heightFactor;
    }

    public void setElementDistanceFactor(int distanceFactor) {
        elementDistanceFactor = distanceFactor;
    }

    public void setFadeDistance(float fadeDistance) {
        this.fadeDistance = fadeDistance;
    }

    public void setFadeScale(float fadeScale) {
        this.fadeScale = fadeScale;
    }

    public int getSelectionIndex() {
        return selection;
    }

    public void setSelectionIndex(int index) {
        if (getElements().size() == 0)
            return;

        if (index < 0 || index >= getElements().size())
            index = 0;

        selection = index;
        onSelectionChanged();
    }

    public Component getSelection() {
        return getElement(selection);
    }

    public void setSelectionChangedListener(ActionListener selectionChanged) {
        this.selectionChanged = selectionChanged;
    }

    private int getRange() {
        return elementDistanceFactor / 2 + 1;
    }

    private int getStartIndex() {
        return Math.max(0, (int) (focus - getRange()));
    }

    private int getEndIndex() {
        return Math.min(getElements().size() - 1, (int) (focus + getRange()));
    }

    private int getFocusIndex() {
        return MathHelper.clamp(Math.round(focus), 0, getElements().size() - 1);
    }

    private void onSelectionChanged() {
        if (selectionChanged != null)
            selectionChanged.execute();
    }

    private void resizeElements() {
        int centerX = x + width / 2;
        int centerY = y + height / 2;

        int deltaX = 0;
        int deltaY = 0;
        if (vertical)
            deltaY = height / elementDistanceFactor;
        else
            deltaX = width / elementDistanceFactor;

        int end = getEndIndex();
        for (int i = getStartIndex(); i <= end; ++i) {
            float off = i - focus;
            float scale = 1f - (1f - fadeScale) * Math.min(1f, Math.abs(off) / fadeDistance);
            int elementWidth = (int) (scale * width / elementWidthFactor);
            int elementHeight = (int) (scale * height / elementHeightFactor);

            Component element = getElement(i);
            element.resize(
                    centerX + (int) (off * deltaX) - elementWidth / 2,
                    centerY + (int) (off * deltaY) - elementHeight / 2,
                    elementWidth,
                    elementHeight);
        }
    }

    @Override
    public void addElement(Component element) {
        super.addElement(element);
    }

    @Override
    public List<Component> getElements() {
        return super.getElements();
    }

    @Override
    public boolean pointerDown(int x, int y) {
        inbound = contains(x, y);

        if (!inbound)
            return false;

        pressed = true;
        downX = x;
        downY = y;

        prevPixel = nextPixel = vertical ? y : x;
        dragging = true;
        sliding = false;

        return true;
    }

    @Override
    public boolean pointerMove(int x, int y) {
        if (!inbound)
            return false;

        if (pressed && (Math.abs(x - downX) > CLICK_THRESHOLD || Math.abs(y - downY) > CLICK_THRESHOLD))
            pressed = false;

        nextPixel = vertical ? y : x;
        return true;
    }

    @Override
    public boolean pointerUp(int x, int y) {
        if (!inbound)
            return false;

        if (pressed && getElements().size() > 0) {
            Component f = getElement(getFocusIndex());
            if (f.contains(x, y)) {
                f.pointerDown(x, y);
                f.pointerUp(x, y);
            } else {
                float centerX = this.x + width / 2;
                float centerY = this.y + height / 2;
                float target;
                if (vertical)
                    target = focus + (y - centerY) * elementDistanceFactor / height;
                else
                    target = focus + (x - centerX) * elementDistanceFactor / width;
                setSelectionIndex(MathHelper.clamp(Math.round(target), 0, getElements().size() - 1));
            }
        } else {
            sliding = true;
        }
        dragging = false;
        return true;
    }

    @Override
    public boolean pointerCancel(int x, int y) {
        if (!inbound)
            return false;

        dragging = false;
        sliding = true;
        return true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (getElements().size() == 0)
            return;

        // draw from side to center so that the center ones are on top of ones to the side
        int focusIndex = getFocusIndex();
        for (int i = getStartIndex(); i < focusIndex; ++i)
            getElement(i).draw(canvas, paint);
        for (int i = getEndIndex(); i > focusIndex; --i)
            getElement(i).draw(canvas, paint);
        getElement(focusIndex).draw(canvas, paint);
    }

    @Override
    public void update(GameView gameView) {
        super.update(gameView);

        if (dragging) {
            float elementDistance;
            if (vertical)
                elementDistance = height / elementDistanceFactor;
            else
                elementDistance = width / elementDistanceFactor;
            inertia = (prevPixel - nextPixel) / elementDistance;
            prevPixel = nextPixel;
            focus += inertia;
        } else if (sliding) {
            if (Math.abs(inertia) > INERTIA_STOP_THRESHOLD) {
                focus += inertia;
                inertia *= INERTIA_DECAY_FACTOR;
            } else {
                sliding = false;
                int index = getFocusIndex();
                if (!getElements().isEmpty() && index != selection)
                    setSelectionIndex(index);
            }
        } else {
            float diff = selection - focus;
            if (Math.abs(diff) > ALIGN_STOP_THRESHOLD) {
                focus += diff * ALIGN_SPEED_FACTOR;
            } else {
                focus = selection;
                return;
            }
        }

        resizeElements();
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        resizeElements();
    }

}
