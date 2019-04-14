package troid.dythm.ui.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class Page extends Container {

    public void onMusicCompletion() {
    }

    public void onMusicChange() {
    }

    public void onRotation(float[] values) {
    }

    public void onPause() {
    }

    public void onBack() {
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.BLACK);

        super.draw(canvas, paint);
    }

}
