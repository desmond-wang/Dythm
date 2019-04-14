package troid.dythm.ui.page;

import java.util.List;

import troid.dythm.game.map.Beatmap;
import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.Selector;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.audio.Music;

public class SelectionPage extends Page {

    private GameView gameView;

    private Image background;
    private Selector selector;
    private Text emptyText;
    private Button random;
    private Button back;

    SelectionPage(GameView gameView, Beatmap selection) {
        super();

        this.gameView = gameView;
        List<Beatmap> beatmaps = gameView.getGame().getBeatmaps();

        // config
        gameView.getMusic().setMode(Music.Mode.REPEAT);

        // elements
        background = new Image(null, Image.Scaling.FILL);
        addElement(background);

        selector = new Selector();
        selector.setVertical(true);
        selector.setElementFactors(1, 12);
        selector.setElementDistanceFactor(6);
        selector.setFadeDistance(3f);
        selector.setFadeScale(0.5f);
        for (Beatmap beatmap : beatmaps) {
            String name = beatmap.getTitle();
            String version = beatmap.getVersion();
            if (version != null && !version.isEmpty())
                name += " [" + version + "]";
            Text text = new Text(name);
            text.setBackground(0x64000000);
            Button button = new Button(text);
            button.setAction(() -> gameView.setPage(new PlayPage(gameView, beatmap)));
            selector.addElement(button);
        }
        selector.setSelectionChangedListener(() -> {
            Beatmap beatmap = beatmaps.get(selector.getSelectionIndex());

            background.setBitmap(beatmap.getBackground());

            playBeatmap(beatmap);
        });
        addElement(selector);

        emptyText = new Text("No beatmaps");
        if (beatmaps.isEmpty())
            addElement(emptyText);

        random = new Button("control/random");
        random.setAction(this::selectRandom);
        addElement(random);

        back = new Button("control/back");
        back.setAction(this::onBack);
        addElement(back);

        // initial selection
        if (selection != null)
            selector.setSelectionIndex(beatmaps.indexOf(selection));
        else
            selectRandom();
    }

    private void selectRandom() {
        if (gameView.getGame().getBeatmaps().size() != 0)
            selector.setSelectionIndex(gameView.getMusic().randomIndex());
    }

    private void playBeatmap(Beatmap beatmap) {
        Music music = gameView.getMusic();
        if (music.isPlayingAudio(beatmap))
            return;

        music.load(beatmap);
        music.seekTo(beatmap.getPreviewTime());
        music.start();
    }

    @Override
    public void onBack() {
        gameView.setPage(new MenuPage(gameView));
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        int offset = height / 16;
        int size = height / 8;

        background.resize(x, y, width, height);
        selector.resize(x, y, width, height);
        emptyText.resize(x, y + height / 2 - size / 2, width, size);
        random.resize(x + width - 2 * offset - 2 * size, y + offset, size, size);
        back.resize(x + width - offset - size, y + offset, size, size);
    }

}
