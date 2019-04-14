package troid.dythm.ui.page;

import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class InfoPage extends Page {

    private GameView gameView;

    private Image background;

    private Text gameName;

    private Text developedBy;
    private Text bao;
    private Text wang;
    private Text zhu;

    private Text copyright;

    private Button back;

    InfoPage(GameView gameView) {
        this.gameView = gameView;

        background = new Image(ResourceHelper.getImagePath("bg/info"), Image.Scaling.FILL);
        addElement(background);

        gameName = new Text("Dythm", Text.Alignment.CENTER);
        addElement(gameName);

        developedBy = new Text("Developed By:", Text.Alignment.LEFT);
        addElement(developedBy);

        bao = new Text("Jiaming (Hash) Bao", Text.Alignment.RIGHT);
        addElement(bao);

        wang = new Text("Daiyang (Desmond) Wang", Text.Alignment.RIGHT);
        addElement(wang);

        zhu = new Text("Hongjian (Daniel) Zhu", Text.Alignment.RIGHT);
        addElement(zhu);

        copyright = new Text("Copyright © 2019 by Team Troid", Text.Alignment.CENTER);
        addElement(copyright);

        back = new Button("control/back");
        back.setAction(this::onBack);
        addElement(back);
    }

    @Override
    public void onBack() {
        gameView.setPage(new MenuPage(gameView));
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);
        background.resize(x - 5, y - 5, width + 10, height + 10);

        final int offset = height / 12;
        final int lineWidth = width - offset * 2;
        final int lineHeight = height / 10;
        gameName.resize(x + offset, y + offset - lineHeight / 2, lineWidth, lineHeight * 3 / 2);
        developedBy.resize(x + offset, y + offset * 2 + lineHeight, lineWidth, lineHeight);
        bao.resize(x + offset, y + offset * 2 + lineHeight, lineWidth, lineHeight);
        wang.resize(x + offset, y + offset * 3 + lineHeight * 2, lineWidth, lineHeight);
        zhu.resize(x + offset, y + offset * 4 + lineHeight * 3, lineWidth, lineHeight);
        copyright.resize(x + offset, y + offset * 5 + lineHeight * 4 + lineHeight / 2, lineWidth, lineHeight / 2);
        back.resize(x + width - height * 3 / 16, y + height / 16, height / 8, height / 8);
    }
}
