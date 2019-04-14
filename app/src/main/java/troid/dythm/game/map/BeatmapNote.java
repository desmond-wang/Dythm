package troid.dythm.game.map;

public class BeatmapNote {
    private static final int MAX_WIDTH = 512;
    private static final int HOLD_NOTE = 128;
    private int column;
    private int time;
    private boolean isHoldNote;
    private int endTime;
    private String soundPath;

    BeatmapNote(String line, int numColumn, TimingPoints point, String path) {
        String[] items = line.split(",");
        int columnWidth = MAX_WIDTH / numColumn;
        column = Integer.parseInt(items[0].trim()) / columnWidth;
        time = Integer.parseInt(items[2].trim());
        if (Integer.parseInt(items[3].trim()) == HOLD_NOTE) {
            isHoldNote = true;
            int endIndex = items[5].indexOf(':');
            endTime = Integer.parseInt(items[5].substring(0, endIndex).trim());
        } else {
            isHoldNote = false;
            endTime = 0;
        }
        String[] extra = items[5].split(":");
        if (extra.length == 5) {
            soundPath = path + extra[4];
        } else {
            soundPath = path + point.getSampleSet() + "-hit";
            int hitSound = Integer.parseInt(items[4]);
            if (hitSound == 1) {
                soundPath += "normal";
            } else if (hitSound == 2) {
                soundPath += "whistle";
            } else if (hitSound == 4) {
                soundPath += "finish";
            } else {
                soundPath += "clap";
            }
            if (point.getSampleIndex() == 1 || point.getSampleIndex() == 0) {
                soundPath += ".wav";
            } else {
                soundPath = soundPath + point.getSampleIndex() + ".wav";
            }
        }
    }

    public int getColumn() {
        return column;
    }

    public int getTime() {
        return time;
    }

    public boolean isHoldNote() {
        return isHoldNote;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getSoundPath() {
        return soundPath;
    }

}
