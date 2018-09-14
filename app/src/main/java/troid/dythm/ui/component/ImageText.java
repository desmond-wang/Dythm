package troid.dythm.ui.component;

import android.graphics.Canvas;
import android.graphics.Paint;

import troid.dythm.ui.base.Component;
import troid.dythm.ui.util.ResourceHelper;

public class ImageText extends Component {

    private Text text;

    private Image image;

    public ImageText(String name) {
        super();

        text = new Text(name.substring(name.lastIndexOf('/') + 1));
        text.setBackground(0x64000000);

        image = new Image(ResourceHelper.getImagePath(name));
    }

    public ImageText(String txt, String img) {
        super();

        text = new Text(txt);
        text.setBackground(0x64000000);

        image = new Image(img);
    }

    public Text getText() {
        return text;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        text.resize(x, y, width, height);
        image.resize(x, y, width, height);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (image.getBitmap() == null) {
            text.draw(canvas, paint);
        } else {
            image.draw(canvas, paint);
        }
    }
}
