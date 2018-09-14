package troid.dythm.game.map;

class TimingPoints {
    private int offset;
    private String sampleSet;
    private int sampleIndex;


    public TimingPoints(String line) {
        String[] items = line.split(",");
        offset = Integer.parseInt(items[0].trim());
        int sampleSetInt = Integer.parseInt(items[3].trim());
        if (sampleSetInt == 1) {
            sampleSet = "normal";
        } else if (sampleSetInt == 2) {
            sampleSet = "soft";
        } else {
            sampleSet = "drum";
        }
        sampleIndex = Integer.parseInt(items[4].trim());
    }

    public int getOffset() {
        return offset;
    }

    public String getSampleSet() {
        return sampleSet;
    }

    public int getSampleIndex() {
        return sampleIndex;
    }
}