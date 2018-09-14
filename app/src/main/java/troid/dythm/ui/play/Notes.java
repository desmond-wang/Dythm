package troid.dythm.ui.play;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import troid.dythm.game.play.PlayColumn;
import troid.dythm.game.play.PlayHoldPoint;
import troid.dythm.game.play.PlayNote;
import troid.dythm.game.play.PlayState;
import troid.dythm.ui.base.Component;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.page.PlayPage;
import troid.dythm.ui.util.ResourceHelper;

class Notes extends Component {

    private PlayPage playPage;
    private PlayState playState;
    private int numColumns;

    private Image note;

    Notes(PlayPage playPage) {
        super();

        this.playPage = playPage;
        playState = playPage.getPlay().getState();
        numColumns = playState.getBeatmap().getNumColumns();

        note = new Image(ResourceHelper.getImagePath("icon/note"), Image.Scaling.STRETCH);
    }

    private int getNoteHeight(long deltaTime) {
        int offset = (int) (height * deltaTime / playState.getSpeed());
        return Math.max(height - offset, 0) + y;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setARGB(255, 0, 0, 0);
        long audioTime = playPage.getAudioTime();
        int noteWidth = width / numColumns;
        int noteHeight = height / 8;

        // hold notes
        paint.setARGB(255, 56, 226, 136);
        PlayHoldPoint prevPoint = playState.getActiveHoldPoint();
        for (PlayHoldPoint nextPoint : playState.getHoldPoints()) {
            if (prevPoint != null && !prevPoint.isEnd()) {
                int startY = getNoteHeight(prevPoint.getTime() - audioTime);
                int endY = getNoteHeight(nextPoint.getTime() - audioTime);

                int noteX = prevPoint.getColumn() * width / numColumns + x;
                canvas.drawRect(noteX, endY, noteX + noteWidth, startY, paint);
            }
            prevPoint = nextPoint;
        }

        // normal notes
        for (int i = 0; i < numColumns; i++) {
            PlayColumn playColumn = playState.getColumns().get(i);
            int noteX = i * width / numColumns + x;

            paint.setColor(Color.BLUE);
            for (PlayNote playNote : playColumn.getActiveNotes()) {
                int noteY = getNoteHeight(playNote.getTime() - audioTime);

                if (note.getBitmap() != null) {
                    note.resize(noteX, noteY - noteHeight, noteWidth, noteHeight);
                    note.draw(canvas, paint);
                } else {
                    canvas.drawRect(noteX, noteY - noteHeight, noteX + noteWidth, noteY, paint);
                }
            }
        }
    }

}
