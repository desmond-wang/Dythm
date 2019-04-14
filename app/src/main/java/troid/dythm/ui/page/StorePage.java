package troid.dythm.ui.page;

import java.util.List;

import troid.dythm.game.ClassList;
import troid.dythm.game.item.Item;
import troid.dythm.game.profile.Profile;
import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.ImageText;
import troid.dythm.ui.component.Selector;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class StorePage extends Page {

    private GameView gameView;
    private Profile profile;
    private List<Item> items;

    private Image background;
    private Selector selector;
    private ImageText coinsIcon;
    private Text coins;
    private Text price;
    private Text count;
    private Text description;
    private Button back;

    StorePage(GameView gameView) {
        super();

        this.gameView = gameView;
        profile = gameView.getGame().getProfile();
        items = ClassList.getItems();

        background = new Image(ResourceHelper.getImagePath("bg/store"), Image.Scaling.FILL);
        addElement(background);

        selector = new Selector();
        selector.setElementFactors(3, 3);
        selector.setElementDistanceFactor(6);
        for (Item item : items) {
            Button button = new Button("item/" + item.getId());
            button.setAction(() -> purchase(item));
            selector.addElement(button);
        }
        selector.setSelectionChangedListener(this::updateTexts);
        addElement(selector);

        coinsIcon = new ImageText("icon/coin");
        addElement(coinsIcon);

        coins = new Text(Text.Alignment.LEFT);
        addElement(coins);

        price = new Text();
        addElement(price);

        count = new Text();
        addElement(count);

        description = new Text();
        addElement(description);

        back = new Button("control/back");
        back.setAction(this::onBack);
        addElement(back);

        selector.setSelectionIndex(0);
    }

    private void purchase(Item item) {
        if (profile.getCoins() < item.getPrice()) {
            return;
        }

        profile.addItem(item);
        profile.removeCoins(item.getPrice());
        updateTexts();
    }

    private void updateTexts() {
        Item item = items.get(selector.getSelectionIndex());
        coins.setText(Integer.toString(profile.getCoins()));
        price.setText("Price: " + item.getPrice());
        count.setText("Count: " + profile.getItemCount(item));
        description.setText(item.getDescription());
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
        int offset = height / 16;
        int iconSize = height / 8;
        coinsIcon.resize(x + offset, y + offset, iconSize, iconSize);
        coins.resize(x + offset * 2 + iconSize, y + offset, width / 3, iconSize);
        price.resize(x, y + height / 5, width, height / 15);
        count.resize(x, y + height * 2 / 3 + height / 15, width, height / 15);
        description.resize(x + width / 12, y + height - height * 3 / 20, width * 5 / 6, height / 12);
        back.resize(x + width - height * 3 / 16, y + height / 16, height / 8, height / 8);
    }

}
