package troid.dythm.ui.util;

import android.graphics.Paint;
import android.graphics.Rect;

public class GraphicHelper {

    public static void setMaxFontByHeight(Paint paint, float height) {
        final float largeTextSize = 48f;
        paint.setTextSize(largeTextSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float maxFontSizeHeight = largeTextSize * height / (fontMetrics.descent - fontMetrics.ascent);
        paint.setTextSize(maxFontSizeHeight);
    }

    public static Rect centerScaleFit(int baseWidth, int baseHeight, int width, int height) {
        int widthByHeight = baseWidth * height / baseHeight;
        int heightByWidth = baseHeight * width / baseWidth;

        int trueWidth;
        int trueHeight;
        if (widthByHeight > width) {
            trueWidth = width;
            trueHeight = heightByWidth;
        }
        else {
            trueWidth = widthByHeight;
            trueHeight = height;
        }

        return new Rect((width - trueWidth) / 2, (height - trueHeight) / 2, (width + trueWidth) / 2, (height + trueHeight) / 2);
    }

    public static Rect centerScaleFill(int baseWidth, int baseHeight, int width, int height) {
        int widthByHeight = baseWidth * height / baseHeight;
        int heightByWidth = baseHeight * width / baseWidth;

        int trueWidth;
        int trueHeight;
        if (widthByHeight < width) {
            trueWidth = width;
            trueHeight = heightByWidth;
        }
        else {
            trueWidth = widthByHeight;
            trueHeight = height;
        }

        return new Rect((width - trueWidth) / 2, (height - trueHeight) / 2, (width + trueWidth) / 2, (height + trueHeight) / 2);
    }

}
