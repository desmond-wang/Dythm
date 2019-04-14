package troid.dythm.ui.component;

import android.graphics.Canvas;
import android.graphics.Paint;

import troid.dythm.ui.base.Component;

public class ProgressBar extends Component {

    private long value = 0;
    private long maximum = 0;

    public void setValue(long value) {
        this.value = value;
    }

    public void setMaximum(long maximum) {
        this.maximum = maximum;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        int offset = height / 6;

        paint.setARGB(255, 0, 0, 0);
        canvas.drawRect(x, y, x + width, y + height, paint);

        // Math.max to prevent divide by 0
        long progress = (width - 2 * offset) * value / Math.max(1, maximum);
        paint.setARGB(255, 255, 255, 255);
        canvas.drawRect(x + offset, y + offset, x + offset + progress, y + height - offset, paint);
    }

}
