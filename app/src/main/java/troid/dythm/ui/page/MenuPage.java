package troid.dythm.ui.page;

import troid.dythm.game.map.Beatmap;
import troid.dythm.ui.GameView;
import troid.dythm.ui.component.ImageText;
import troid.dythm.ui.audio.Music;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.Selector;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class MenuPage extends Page {

    private Music music;

    private Image background;
    private Selector selector;
    private Text buttonName;
    private Text musicTitle;
    private Button randomButton;
    private Button resumeButton;
    private Button pauseButton;

    MenuPage(GameView gameView) {
        super();

        music = gameView.getMusic();

        background = new Image(ResourceHelper.getImagePath("bg/menu"), Image.Scaling.STRETCH);
        addElement(background);

        Button profileButton = new Button("control/profile");
        profileButton.setAction(() -> gameView.setPage(new ProfilePage(gameView)));

        Button achievementButton = new Button("control/achievement");
        achievementButton.setAction(() -> gameView.setPage(new AchievementPage(gameView)));

        Button storeButton = new Button("control/store");
        storeButton.setAction(() -> gameView.setPage(new StorePage(gameView)));

        Button playButton = new Button("control/play");
        playButton.setAction(() -> gameView.setPage(new SelectionPage(gameView, music.getPlayingBeatmap())));

        Button themeButton = new Button("control/theme");
        themeButton.setAction(() -> gameView.setPage(new ThemePage(gameView)));

        Button settingsButton = new Button("control/settings");
        settingsButton.setAction(() -> gameView.setPage(new SettingsPage(gameView)));

        Button infoButton = new Button("control/info");
        infoButton.setAction(() -> gameView.setPage(new InfoPage(gameView)));

        selector = new Selector(
                profileButton,
                achievementButton,
                storeButton,
                playButton,
                themeButton,
                settingsButton,
                infoButton
        );
        selector.setElementFactors(3, 2);
        selector.setElementDistanceFactor(6);
        selector.setSelectionChangedListener(() -> {
            Button button = (Button) (selector.getSelection());
            ImageText imageText = (ImageText) button.getContent();
            String name = imageText.getText().getText();
            buttonName.setText(name.toUpperCase());
        });
        addElement(selector);

        buttonName = new Text();
        addElement(buttonName);

        musicTitle = new Text(Text.Alignment.LEFT);
        addElement(musicTitle);

        randomButton = new Button("control/random");
        randomButton.setAction(music::playRandom);
        addElement(randomButton);

        resumeButton = new Button("control/play");
        resumeButton.setAction(music::resume);
        addElement(resumeButton);

        pauseButton = new Button("control/pause");
        pauseButton.setAction(music::pause);
        addElement(pauseButton);

        music.setMode(Music.Mode.RANDOM);

        selector.setSelectionIndex(selector.getElements().size() / 2);
        updateTexts();
    }

    private void updateTexts() {
        Beatmap beatmap = music.getPlayingBeatmap();
        if (beatmap != null)
            musicTitle.setText(beatmap.getTitle());
    }

    @Override
    public void onMusicChange() {
        updateTexts();
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        int offset = height / 16;
        int size = height / 8;
        int textHeight = height / 15;
        int textOffset = offset + (size - textHeight) / 2;

        background.resize(x - 5, y - 5, width + 10, height + 10);
        selector.resize(x, y, width, height);
        buttonName.resize(x + width / 3, y + height * 3 / 4, width / 3, height / 8);
        musicTitle.resize(x + textOffset, y + textOffset, width / 2, textHeight);
        randomButton.resize(x + width - 3 * offset - 3 * size, y + offset, size, size);
        resumeButton.resize(x + width - 2 * offset - 2 * size, y + offset, size, size);
        pauseButton.resize(x + width - offset - size, y + offset, size, size);
    }

}
