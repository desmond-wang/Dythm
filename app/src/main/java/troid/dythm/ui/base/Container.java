package troid.dythm.ui.base;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import troid.dythm.ui.GameView;

public abstract class Container extends Component {

    private List<Component> elements;

    protected Container() {
        this(new ArrayList<>());
    }

    protected Container(List<Component> elements) {
        this.elements = elements;
    }

    protected void addElement(Component element) {
        elements.add(element);
    }

    protected Component getElement(int index) {
        return elements.get(index);
    }

    protected List<Component> getElements() {
        return elements;
    }

    @Override
    public boolean pointerDown(int x, int y) {
        for (int i = elements.size() - 1; i >= 0; --i) {
            if (elements.get(i).pointerDown(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean pointerDownMulti(int x, int y) {
        for (int i = elements.size() - 1; i >= 0; --i) {
            if (elements.get(i).pointerDownMulti(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean pointerMove(int x, int y) {
        for (int i = elements.size() - 1; i >= 0; --i) {
            if (elements.get(i).pointerMove(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean pointerUpMulti(int x, int y) {
        for (int i = elements.size() - 1; i >= 0; --i) {
            if (elements.get(i).pointerUpMulti(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean pointerUp(int x, int y) {
        for (int i = elements.size() - 1; i >= 0; --i) {
            if (elements.get(i).pointerUp(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean pointerCancel(int x, int y) {
        for (int i = elements.size() - 1; i >= 0; --i) {
            if (elements.get(i).pointerCancel(x, y))
                return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        for (Component element : elements) {
            element.draw(canvas, paint);
        }
    }

    @Override
    public void update(GameView gameView) {
        for (Component element : elements) {
            element.update(gameView);
        }
    }

}
