package troid.dythm.ui.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.ImageText;
import troid.dythm.ui.component.Selector;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class ThemePage extends Page {

    private GameView gameView;

    private Image background;
    private Selector selector;
    private Text name;
    private Text current;
    private Button back;

    ThemePage(GameView gameView) {
        super();

        this.gameView = gameView;
        String cur = gameView.getGame().getProfile().getTheme();

        List<String> themes = new ArrayList<>();
        File[] files = new File(ResourceHelper.getHome() + "/theme/").listFiles();
        if (files != null)
            for (File file : files)
                themes.add(file.getName());

        background = new Image(ResourceHelper.getImagePath("bg/achievement"), Image.Scaling.FILL);
        addElement(background);

        selector = new Selector();
        selector.setElementFactors(3, 3);
        selector.setElementDistanceFactor(6);
        selector.setSelectionChangedListener(() -> {
            String theme = themes.get(selector.getSelectionIndex());
            name.setText(theme);
        });
        for (String theme : themes) {
            String image = ResourceHelper.getHome() + "theme/" + theme + "/control/theme.png";
            Button button = new Button(new ImageText(theme, image));
            button.setAction(() -> {
                gameView.setTheme(theme);
                onBack();
            });
            selector.addElement(button);
        }
        addElement(selector);

        name = new Text();
        addElement(name);

        current = new Text("Current: " + cur, Text.Alignment.LEFT);
        addElement(current);

        back = new Button("control/back");
        back.setAction(this::onBack);
        addElement(back);


        selector.setSelectionIndex(themes.indexOf(cur));
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
        int textHeight = height / 15;
        int textOffset = offset + (size - textHeight) / 2;

        background.resize(x, y, width, height);
        selector.resize(x, y, width, height);
        name.resize(x + width / 3, y + height * 3 / 4, width / 3, height / 12);
        current.resize(x + textOffset, y + textOffset, width / 2, textHeight);
        back.resize(x + width - height * 3 / 16, y + height / 16, height / 8, height / 8);
    }

}
