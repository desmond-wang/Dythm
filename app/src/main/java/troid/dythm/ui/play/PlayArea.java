package troid.dythm.ui.play;

import troid.dythm.game.play.Play;
import troid.dythm.ui.base.Container;
import troid.dythm.ui.page.PlayPage;

public class PlayArea extends Container {

    private Columns columns;
    private Notes notes;
    private Lighting lighting;
    private Slider slider;
    private Keys keys;

    public PlayArea(PlayPage playPage) {
        Play play = playPage.getPlay();

        columns = new Columns(play.getState().getBeatmap().getNumColumns());
        addElement(columns);

        notes = new Notes(playPage);
        addElement(notes);

        lighting = new Lighting(playPage);
        addElement(lighting);

        slider = new Slider(play);
        addElement(slider);

        keys = new Keys(playPage);
        addElement(keys);
    }

    public void onRotation(float roll) {
        slider.moveSlider(roll);
    }

    @Override
    public void resize(int x, int y, int width, int height) {
        super.resize(x, y, width, height);

        columns.resize(x, y, width, height);
        notes.resize(x, y, width, height);
        lighting.resize(x, y + height / 2, width, height / 2);
        slider.resize(x, y + height * 11 / 12, width, height / 12);
        keys.resize(x, y + height / 2, width, height / 2);
    }

}
