package troid.dythm.ui.play;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import java.util.ArrayList;
import java.util.List;

import troid.dythm.game.play.Hit;
import troid.dythm.game.play.HitJudgement;
import troid.dythm.game.play.PlayHoldPoint;
import troid.dythm.game.play.PlayState;
import troid.dythm.ui.base.Component;
import troid.dythm.ui.page.PlayPage;

public class Lighting extends Component {

    private static final int DURATION = 200;

    private PlayPage playPage;
    private PlayState playState;

    private Shader normalShader;
    private Shader missShader;
    private Shader holdShader;
    private List<Hit> columnHits = new ArrayList<>();

    Lighting(PlayPage playPage) {
        this.playPage = playPage;
        playState = playPage.getPlay().getState();

        int numColumns = playState.getNumColumns();
        for (int i = 0; i < numColumns; ++i)
            columnHits.add(null);
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        normalShader = new LinearGradient(x, y, x, y + height, Color.TRANSPARENT, Color.WHITE, Shader.TileMode.CLAMP);
        missShader = new LinearGradient(x, y, x, y + height, Color.TRANSPARENT, Color.RED, Shader.TileMode.CLAMP);
        holdShader = new LinearGradient(x, y, x, y + height, Color.TRANSPARENT, Color.YELLOW, Shader.TileMode.CLAMP);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        long audioTime = playPage.getAudioTime();
        int numColumns = playState.getNumColumns();

        for (int i = playState.getNumHits() - 1; i >= 0; --i) {
            Hit hit = playState.getHit(i);

            long time = hit.getTime();
            if (time + DURATION <= audioTime)
                break;

            int column = hit.getColumn();
            Hit columnHit = columnHits.get(column);
            if (columnHit == null || columnHit.getTime() < time)
                columnHits.set(column, hit);
        }

        int buttonWidth = width / numColumns;
        for (int i = 0; i < numColumns; i++) {
            Hit hit = columnHits.get(i);
            if (hit == null)
                continue;

            long endTime = hit.getTime() + DURATION;

            if (endTime > audioTime) {
                int alpha = (int) (255 * (endTime - audioTime) / DURATION);

                paint.setARGB(alpha, 255, 255, 255);
                if (hit.getJudgement() != HitJudgement.MISS)
                    paint.setShader(normalShader);
                else
                    paint.setShader(missShader);

                int buttonX = x + i * width / numColumns;
                canvas.drawRect(buttonX, y, buttonX + buttonWidth, y + height, paint);
            }
        }

        PlayHoldPoint holdPoint = playState.getActiveHoldPoint();
        if (holdPoint != null) {
            paint.setARGB(255, 255, 255, 255);
            paint.setShader(holdShader);

            int buttonX = x + holdPoint.getColumn() * width / numColumns;
            canvas.drawRect(buttonX, y, buttonX + buttonWidth, y + height, paint);
        }

        paint.setShader(null);
    }

}
