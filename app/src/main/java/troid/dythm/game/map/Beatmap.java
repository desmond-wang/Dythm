package troid.dythm.game.map;

import java.util.ArrayList;
import java.util.List;

public class Beatmap {
    // general
    private String audioFilename;
    private int previewTime;

    //for test
    //String path = "/u/";

    // metadata
    private String title;
    private String artist;
    private String creator;
    private String version;
    // event
    private String background;

    // hit objects
    private int numColumns;
    private List<BeatmapNote> notes = new ArrayList<>();
    private List<TimingPoints> points = new ArrayList<>();

    public Beatmap(String path, List<String> lines) {
        String section = "default";
        int offset = 1;
        for (String line : lines) {
            if (line.equals("")) {
                continue;
            }
            if (line.charAt(0) == '[') {
                section = line;
                continue;
            }

            switch (section) {
                case "[General]": {
                    int endIndex = line.indexOf(':');
                    String key = line.substring(0, endIndex);
                    switch (key) {
                        case "AudioFilename":
                            audioFilename = path + line.substring(endIndex + offset).trim();
                            break;
                        case "PreviewTime":
                            String timeString;
                            timeString = line.substring(endIndex + offset).trim();
                            previewTime = Integer.parseInt(timeString);
                            break;
                        case "Mode":
                            if (Integer.parseInt(line.substring(endIndex + offset).trim()) != 3){
                                throw new RuntimeException("not a mania map");
                            }
                    }
                    break;
                }
                case "[Metadata]": {
                    int endIndex = line.indexOf(':');
                    String key = line.substring(0, endIndex);
                    switch (key) {
                        case "Title":
                            title = line.substring(endIndex + offset).trim();
                            break;
                        case "Artist":
                            artist = line.substring(endIndex + offset).trim();
                            break;

                        case "Creator":
                            creator = line.substring(endIndex + offset).trim();
                            break;
                        case "Version":
                            version = line.substring(endIndex + offset).trim();
                            break;
                    }
                    break;
                }
                case "[Difficulty]": {
                    int endIndex = line.indexOf(':');
                    String key = line.substring(0, endIndex);
                    if (key.equals("CircleSize")) {
                        String numColumnString;
                        numColumnString = line.substring(endIndex + offset).trim();
                        numColumns = Integer.parseInt(numColumnString);
                    }
                    break;
                }
                case "[Events]": {
                    // background is specified by line:
                    // 0,0,"BG.png",0,0
                    if (line.charAt(0) == '0') {
                        String[] columns = line.split(",");
                        String text = columns[2];
                        background = path + text.substring(1, text.length() - 1);
                    }
                    break;
                }
                case "[TimingPoints]":
                    if (line.equals("[TimingPoints]")) {
                        break;
                    }
                    TimingPoints point = new TimingPoints(line);
                    points.add(point);
                    break;
                case "[HitObjects]":
                    int curIndex = 0;
                    if (line.equals("[HitObjects]")) {
                        break;
                    }
                    String[] items = line.split(",");
                    if (Integer.parseInt(items[2]) > points.get(curIndex).getOffset()
                            && curIndex < points.size() - 1) {
                        curIndex++;
                    }
                    BeatmapNote note = new BeatmapNote(line, numColumns, points.get(curIndex), path);

                    notes.add(note);
                    break;
                default:
                    break;
            }
        }
    }

    public String getAudioFilename() {
        return audioFilename;
    }

    public int getPreviewTime() {
        return previewTime;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCreator() {
        return creator;
    }

    public String getVersion() {
        return version;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public List<BeatmapNote> getNotes() {
        return notes;
    }

    public String getBackground() {
        return background;
    }

    public List<TimingPoints> getPoints() {
        return points;
    }
}
