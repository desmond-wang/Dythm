package troid.dythm.ui.page;

import java.util.ArrayList;
import java.util.List;

import troid.dythm.game.play.PlayRank;
import troid.dythm.game.profile.Profile;
import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.ImageText;
import troid.dythm.ui.component.Selector;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class ProfilePage extends Page {

    private GameView gameView;

    private List<String> descriptions = new ArrayList<>();

    private Image background;
    private Selector selector;
    private Text description;

    private Image timeImage;
    private Text timePlayed;

    private Button back;

    ProfilePage(GameView gameView) {
        super();

        this.gameView = gameView;

        Profile profile = gameView.getGame().getProfile();

        background = new Image(ResourceHelper.getImagePath("bg/profile"), Image.Scaling.FILL);
        addElement(background);

        selector = new Selector();
        selector.setElementFactors(3, 3);
        selector.setElementDistanceFactor(6);
        selector.setSelectionChangedListener(() -> description.setText(descriptions.get(selector.getSelectionIndex())));
        addElement(selector);

        description = new Text();
        addElement(description);

        timeImage = new Image(ResourceHelper.getImagePath("icon/time"));
        addElement(timeImage);

        timePlayed = new Text(formatTime(profile.getTimePlayed()), Text.Alignment.LEFT);
        addElement(timePlayed);

        back = new Button("control/back");
        back.setAction(this::onBack);
        addElement(back);


        addStat("icon/count", "Times played", profile.getPlayCount());
        addStat("icon/combo", "Max combo", profile.getMaxCombo());
        addStat("icon/coin", "Coins earned", profile.getCoinsEarned());

        for (PlayRank rank : PlayRank.values()) {
            String name = rank + " rank achieved";
            int value = profile.getRankCount(rank);
            addStat("rank/" + rank.toString().toLowerCase(), name, value);
        }


        selector.setSelectionIndex(0);
    }

    private void addStat(String img, String name, String value) {
        ImageText imageText = new ImageText(img);
        selector.addElement(imageText);
        descriptions.add(name + ": " + value);
    }

    private void addStat(String img, String name, int value) {
        addStat(img, name, Integer.toString(value));
    }

    private String formatTime(long milli) {
        long sec = milli / 1000;
        long min = sec / 60;
        long hou = min / 60;
        return hou + "H " + (min % 60) + "M";
    }

    @Override
    public void onBack() {
        gameView.setPage(new MenuPage(gameView));
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        background.resize(x - 5, y - 5, width + 10, height + 10);
        selector.resize(x, y, width, height);
        description.resize(x + width / 3, y + height * 3 / 4, width / 3, height / 12);

        final int offset = height / 16;
        final int iconSize = height / 8;
        timeImage.resize(x + offset, y + offset, iconSize, iconSize);
        timePlayed.resize(x + offset * 2 + iconSize, y + offset, width / 3, iconSize);

        back.resize(x + width - height * 3 / 16, y + height / 16, height / 8, height / 8);
    }

}
