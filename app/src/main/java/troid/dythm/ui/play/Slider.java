package troid.dythm.ui.play;

import android.graphics.Canvas;
import android.graphics.Paint;

import troid.dythm.game.play.Play;
import troid.dythm.ui.base.Component;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.util.ResourceHelper;
import troid.dythm.ui.util.MathHelper;

public class Slider extends Component {

    private Play play;
    private int numColumns;
    private int sliderWidth;

    private Image slider;

    Slider(Play play) {
        this.play = play;
        numColumns = play.getState().getBeatmap().getNumColumns();
        sliderWidth = numColumns / 2;

        slider = new Image(ResourceHelper.getImagePath("icon/slider"), Image.Scaling.STRETCH);
    }

    public void moveSlider(float roll) {
        float center = numColumns * (0.5f - roll / play.getState().getSliderSensitivity());
        int left = MathHelper.clamp(Math.round(center - sliderWidth / 2f), 0, numColumns - sliderWidth);
        int right = left + sliderWidth;
        play.moveSlider(left, right);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        int left = width * play.getState().getSliderStartColumn() / numColumns;
        int right = width * play.getState().getSliderEndColumn() / numColumns;
        if (slider.getBitmap() == null) {
            paint.setARGB(255, 0, 128, 0);
            canvas.drawRect(x + left, y, x + right, y + height, paint);
        }
        else {
            slider.resize(x + left, y, right - left, height);
            slider.draw(canvas, paint);
        }
    }

}
