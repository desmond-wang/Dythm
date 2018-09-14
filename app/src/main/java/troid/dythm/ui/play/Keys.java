package troid.dythm.ui.play;

import android.graphics.Canvas;
import android.graphics.Paint;

import troid.dythm.game.play.Play;
import troid.dythm.ui.base.Component;
import troid.dythm.ui.page.PlayPage;

class Keys extends Component {

    private Play play;
    private int numColumns;

    Keys(PlayPage playPage) {
        super();

        play = playPage.getPlay();
        numColumns = play.getState().getBeatmap().getNumColumns();
    }

    @Override
    public boolean pointerDownMulti(int x, int y) {
        if (!contains(x, y))
            return false;

        int keyIndex = numColumns * (x - this.x) / width;
        play.clickColumn(keyIndex);
        return true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // nothing to draw - component for input only
    }

}
