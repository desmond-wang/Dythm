package troid.dythm.ui.play;

import android.graphics.Canvas;
import android.graphics.Paint;

import troid.dythm.ui.base.ActionListener;
import troid.dythm.ui.base.Component;
import troid.dythm.ui.component.ImageText;

public class CatchButton extends Component {

    private ImageText content;
    private ActionListener action;

    public CatchButton() {
        content = new ImageText("control/catch");
    }

    public void setAction(ActionListener action) {
        this.action = action;
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);
        content.resize(x, y, width, height);
    }

    @Override
    public boolean pointerDownMulti(int x, int y) {
        if (!contains(x, y))
            return false;

        action.execute();
        return true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        content.draw(canvas, paint);
    }

}
