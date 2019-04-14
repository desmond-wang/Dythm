package troid.dythm.ui.page;

import java.util.List;

import troid.dythm.game.ClassList;
import troid.dythm.game.achievement.Achievement;
import troid.dythm.game.profile.Profile;
import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.ImageText;
import troid.dythm.ui.component.Selector;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class AchievementPage extends Page {

    private GameView gameView;
    private Profile profile;
    private List<Achievement> achievements;

    private Image background;
    private Selector selector;
    private Text name;
    private Text acquired;
    private Text description;
    private Button back;

    AchievementPage(GameView gameView) {
        this.gameView = gameView;
        profile = gameView.getGame().getProfile();
        achievements = ClassList.getAchievements();

        background = new Image(ResourceHelper.getImagePath("bg/achievement"), Image.Scaling.FILL);
        addElement(background);

        selector = new Selector();
        selector.setElementFactors(3, 3);
        selector.setElementDistanceFactor(6);
        for (Achievement achievement : achievements) {
            ImageText imageText = new ImageText("achievement/" + achievement.getId());
            selector.addElement(imageText);
        }
        selector.setSelectionChangedListener(this::updateTexts);
        addElement(selector);

        name = new Text();
        addElement(name);

        acquired = new Text();
        addElement(acquired);

        description = new Text();
        addElement(description);

        back = new Button("control/back");
        back.setAction(this::onBack);
        addElement(back);

        selector.setSelectionIndex(0);
    }

    private void updateTexts() {
        Achievement achievement = achievements.get(selector.getSelectionIndex());
        name.setText(achievement.getName());
        acquired.setText(achievement.isAchieved(profile) ? "Acquired" : "Not acquired");
        description.setText(achievement.getDescription());
    }

    @Override
    public void onBack() {
        gameView.setPage(new MenuPage(gameView));
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);
        background.resize(x - 5, y - 5, width + 10, height + 10);
        int offset = height / 10;
        selector.resize(x, y, width, height);
        name.resize(x, y + offset, width, height / 8);
        acquired.resize(x, y + height * 2 / 3 + offset * 2 / 3, width, height / 15);
        description.resize(x + width / 12, y + height - offset * 3 / 2, width * 5 / 6, height / 12);
        back.resize(x + width - height * 3 / 16, y + height / 16, height / 8, height / 8);
    }
}
