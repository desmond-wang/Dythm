package troid.dythm.ui.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import troid.dythm.ui.base.Component;
import troid.dythm.ui.util.GraphicHelper;

public class Text extends Component {

    private int foreground = Color.WHITE;
    private int background = Color.TRANSPARENT;
    private String font = "";
    private String text;
    private Alignment alignment;

    public Text(String text, Alignment alignment) {
        super();

        this.text = text;
        this.alignment = alignment;
    }

    public Text(String text) {
        this(text, Alignment.CENTER);
    }

    public Text(Alignment alignment) {
        this("", alignment);
    }

    public Text() {
        this("");
    }

    public int getForeground() {
        return foreground;
    }

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String texts) {
        this.text = texts;
    }

    public void clearText() {
        text = "";
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (text.isEmpty())
            return;

        paint.setColor(background);
        canvas.drawRect(new Rect(x, y, x + width, y + height), paint);

        paint.setColor(foreground);
        paint.setFontFeatureSettings(font);
        GraphicHelper.setMaxFontByHeight(paint, height);
        Rect bound = new Rect();
        paint.getTextBounds(text, 0, text.length(), bound);
        int bottom = y - (int) paint.getFontMetrics().ascent;
        int extraWidth = width - bound.width();
        int textX;
        switch (alignment) {
            case LEFT:
                textX = x;
                break;
            case CENTER:
                textX = x + extraWidth / 2;
                break;
            case RIGHT:
                textX = x + extraWidth;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        canvas.drawText(text, textX, bottom, paint);
    }

    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

}
