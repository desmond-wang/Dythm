package troid.dythm.ui.page;

import troid.dythm.game.play.PlayState;
import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.ImageText;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class ResultPage extends Page {

    private GameView gameView;
    private PlayState playState;

    private Image background;

    private Text musicTitle;
    private Text score;
    private Text maxCombo;
    private Text accuracy;
    private Text fullCombo;

    private ImageText rank;

    private Button replayButton;
    private Button backButton;

    ResultPage(GameView gameView, PlayState playState) {
        super();

        this.gameView = gameView;
        this.playState = playState;

        background = new Image(ResourceHelper.getImagePath("bg/result"), Image.Scaling.FILL);
        addElement(background);

        musicTitle = new Text(playState.getBeatmap().getTitle(), Text.Alignment.LEFT);
        addElement(musicTitle);

        score = new Text("Score: " + playState.getScore() + " / " + playState.getTotalScore(), Text.Alignment.LEFT);
        addElement(score);

        maxCombo = new Text("Max Combo: " + playState.getMaxCombo() + " / " + playState.getTotalCombo(), Text.Alignment.LEFT);
        addElement(maxCombo);

        accuracy = new Text("Accuracy: " + playState.getAccuracy() + "%", Text.Alignment.LEFT);
        addElement(accuracy);

        fullCombo = new Text(Text.Alignment.LEFT);
        if (playState.getMaxCombo() == playState.getTotalCombo())
            fullCombo.setText("Full Combo");
        addElement(fullCombo);

        rank = new ImageText("rank/" + playState.getRank().toString().toLowerCase());
        addElement(rank);

        replayButton = new Button("control/replay");
        replayButton.setAction(() -> gameView.setPage(new PlayPage(gameView, playState.getBeatmap())));
        addElement(replayButton);

        backButton = new Button("control/back");
        backButton.setAction(this::onBack);
        addElement(backButton);
    }

    @Override
    public void onBack() {
        gameView.setPage(new SelectionPage(gameView, playState.getBeatmap()));
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        background.resize(x - 5, y - 5, width + 10, height + 10);

        final int offset = height / 10;

        final int numLines = 5;
        final int lineHeight = height / 12;
        final int lineSpacing = (height - offset * 2 - lineHeight * numLines) / (numLines - 1) + lineHeight;
        musicTitle.resize(x + offset, y + offset, width * 2 / 3, lineHeight);
        score.resize(x + offset, y + offset + lineSpacing, width * 2 / 3, lineHeight);
        maxCombo.resize(x + offset, y + offset + lineSpacing * 2, width * 2 / 3, lineHeight);
        accuracy.resize(x + offset, y + offset + lineSpacing * 3, width * 2 / 3, lineHeight);
        fullCombo.resize(x + offset, y + offset + lineSpacing * 4, width * 2 / 3, lineHeight);

        rank.resize(x + width - width / 3 - offset, y + offset, width / 3, height / 2);

        final int buttonSize = height / 4;
        replayButton.resize(x + width - offset * 2 - buttonSize * 2, y + height - offset - buttonSize, buttonSize, buttonSize);
        backButton.resize(x + width - offset - buttonSize, y + height - offset - buttonSize, buttonSize, buttonSize);
    }

}
