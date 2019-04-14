package troid.dythm.ui.component;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.File;

import troid.dythm.ui.base.Component;
import troid.dythm.ui.util.GraphicHelper;

public class Image extends Component {

    private Bitmap bitmap = null;
    private Bitmap cached = null;
    private Scaling scaling = Scaling.FIT;
    private boolean cacheDisabled = false;
    private Rect scaled;
    private Rect dest;

    public Image() {
        super();
    }

    public Image(String url) {
        this();

        setBitmap(url);
    }

    public Image(String url, Scaling scaling) {
        this(url);

        this.scaling = scaling;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(String url) {
        if (url == null) {
            bitmap = null;
            return;
        }

        File file = new File(url);
        if (!file.exists()) {
            System.err.println("Image file does not exist: " + url);
            bitmap = null;
            return;
        }

        bitmap = BitmapFactory.decodeFile(url);
        if (!isEmpty())
            generateCache();
    }

    public void setScaling(Scaling scaling) {
        this.scaling = scaling;
        if (!isEmpty())
            generateCache();
    }

    private void generateCache() {
        switch (scaling) {
            case FIT:
                scaled = GraphicHelper.centerScaleFit(bitmap.getWidth(), bitmap.getHeight(), width, height);
                break;
            case FILL:
                scaled = GraphicHelper.centerScaleFill(bitmap.getWidth(), bitmap.getHeight(), width, height);
                break;
            case STRETCH:
                scaled = new Rect(0, 0, width, height);
                break;
            default:
                throw new UnsupportedOperationException();
        }

        generateDest();

        if (!cacheDisabled) {
            cached = Bitmap.createScaledBitmap(bitmap, scaled.width(), scaled.height(), false);
        }
    }

    private void generateDest() {
        dest = new Rect(x + scaled.left, y + scaled.top, x + scaled.right, y + scaled.bottom);
    }


    private boolean isEmpty() {
        return bitmap == null || width == 0 || height == 0;
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        int prevWidth = this.width;
        int prevHeight = this.height;

        super.resize(x, y, width, height);

        if (width != prevWidth || height != prevHeight) {
            // disable caching when we detect a resize - caching is slow when we resize a lot
            // TODO: maybe re-enable caching after some condition (e.g. no resize for some time)?
            if (cached != null)
                cacheDisabled = true;

            if (!isEmpty())
                generateCache();
        } else if (scaled != null) {
            generateDest();
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (isEmpty())
            return;

        // TODO: this seems hacky - is there another place to initialize or another condition to check?
        if (scaled == null)
            generateCache();

        paint.setARGB(255, 0, 0, 0);

        if (!cacheDisabled) {
            canvas.drawBitmap(cached, x + scaled.left, y + scaled.top, paint);
        } else {
            canvas.drawBitmap(bitmap, null, dest, paint);
        }
    }

    public enum Scaling {
        FILL,
        FIT,
        STRETCH,
    }

}
