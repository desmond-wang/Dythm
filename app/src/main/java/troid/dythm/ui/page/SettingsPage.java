package troid.dythm.ui.page;

import troid.dythm.game.profile.Profile;
import troid.dythm.ui.GameView;
import troid.dythm.ui.base.Page;
import troid.dythm.ui.component.Button;
import troid.dythm.ui.component.Image;
import troid.dythm.ui.component.Text;
import troid.dythm.ui.util.ResourceHelper;

public class SettingsPage extends Page {

    private static final int SPEED_MIN = 2000;
    private static final int SPEED_MAX = 100;
    private static final int SENSITIVITY_MIN = 1;
    private static final int SENSITIVITY_MAX = 16;

    private GameView gameView;

    private Image background;

    private Text speed;
    private Button speedDecrease;
    private Text speedNum;
    private Button speedIncrease;

    private Text sliderSensitivity;
    private Button sensitivityDecrease;
    private Text sensitivityNum;
    private Button sensitivityIncrease;

    private Button back;

    SettingsPage(GameView gameView) {
        this.gameView = gameView;

        Profile profile = gameView.getGame().getProfile();

        background = new Image(ResourceHelper.getImagePath("bg/settings"), Image.Scaling.FILL);
        addElement(background);

        speed = new Text("Fall Speed:", Text.Alignment.LEFT);
        addElement(speed);

        speedNum = new Text(Integer.toString((SPEED_MIN + 100 - (int) profile.getSpeed()) / 100));
        addElement(speedNum);

        speedDecrease = new Button("control/decrease");
        speedDecrease.setAction(() -> {
            if (profile.getSpeed() < SPEED_MIN) {
                profile.setSpeed(profile.getSpeed() + 100);
                speedNum.setText(Integer.toString((SPEED_MIN + 100 - (int) profile.getSpeed()) / 100));
            }
        });
        addElement(speedDecrease);

        speedIncrease = new Button("control/increase");
        speedIncrease.setAction(() -> {
            if (profile.getSpeed() > SPEED_MAX) {
                profile.setSpeed(profile.getSpeed() - 100);
                speedNum.setText(Integer.toString((SPEED_MIN + 100 - (int) profile.getSpeed()) / 100));
            }
        });
        addElement(speedIncrease);

        sliderSensitivity = new Text("Sensitivity:", Text.Alignment.LEFT);
        addElement(sliderSensitivity);

        sensitivityNum = new Text(Integer.toString(Math.round((float) Math.PI / profile.getSliderSensitivity())));
        addElement(sensitivityNum);

        sensitivityDecrease = new Button("control/decrease");
        sensitivityDecrease.setAction(() -> {
            int n = Math.round((float) Math.PI / profile.getSliderSensitivity());
            if (n > SENSITIVITY_MIN) {
                profile.setSliderSensitivity((float) Math.PI / (n - 1));
                sensitivityNum.setText(Integer.toString(n - 1));
            }
        });
        addElement(sensitivityDecrease);

        sensitivityIncrease = new Button("control/increase");
        sensitivityIncrease.setAction(() -> {
            int n = Math.round((float) Math.PI / profile.getSliderSensitivity());
            if (n < SENSITIVITY_MAX) {
                profile.setSliderSensitivity((float) Math.PI / (n + 1));
                sensitivityNum.setText(Integer.toString(n + 1));
            }
        });
        addElement(sensitivityIncrease);

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

        int horOffset = width / 8;
        int verOffset = height / 3;
        int nameWidth = (width - horOffset * 2) / 2;
        int valueWidth = (width - horOffset * 2) / 4;
        int textHeight = height / 8;
        int buttonSize = height / 8;

        int numRows = 2;
        int rowHeight = height / 8;
        int rowStart = y + verOffset;
        int rowOffset = (height - verOffset * 2 - rowHeight) / (numRows - 1);

        speed.resize(x + horOffset, rowStart, nameWidth, textHeight);
        speedNum.resize(x + horOffset + nameWidth, rowStart, valueWidth, textHeight);
        speedDecrease.resize(x + horOffset + nameWidth + valueWidth, rowStart, buttonSize, buttonSize);
        speedIncrease.resize(x + width - horOffset - buttonSize, rowStart, buttonSize, buttonSize);

        sliderSensitivity.resize(x + horOffset, rowStart + rowOffset, nameWidth, textHeight);
        sensitivityNum.resize(x + horOffset + nameWidth, rowStart + rowOffset, valueWidth, textHeight);
        sensitivityDecrease.resize(x + horOffset + nameWidth + valueWidth, rowStart + rowOffset, buttonSize, buttonSize);
        sensitivityIncrease.resize(x + width - horOffset - buttonSize, rowStart + rowOffset, buttonSize, buttonSize);

        back.resize(x + width - height * 3 / 16, y + height / 16, height / 8, height / 8);
    }

}
