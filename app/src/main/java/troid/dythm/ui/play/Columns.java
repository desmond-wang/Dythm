package troid.dythm.ui.play;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import troid.dythm.ui.base.Component;

class Columns extends Component {

    private int numColumns;

    Columns(int numColumns) {
        super();

        this.numColumns = numColumns;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(0x64000000);
        canvas.drawRect(x, y, x + width, y + height, paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * width / numColumns + x, y, i * width / numColumns + x, y + height, paint);
        }
    }

}
