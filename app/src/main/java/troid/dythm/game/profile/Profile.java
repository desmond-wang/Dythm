package troid.dythm.game.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import troid.dythm.game.ClassList;
import troid.dythm.game.item.Item;
import troid.dythm.game.play.PlayRank;
import troid.dythm.game.play.PlayState;

public class Profile {
    public static final int SCORE_COIN_RATIO = 1_000;

    private int coins = 1_000;
    private Map<Item, Integer> items = new HashMap<>();

    // history
    private int playCount = 0;
    private long timePlayed = 0;
    private int maxCombo = 0;
    private int coinsEarned = 0;
    private Map<PlayRank, Integer> ranks = new HashMap<>();

    // settings
    private String theme = "Contrast";
    private long speed = 1000;
    private float sliderSensitivity = (float) Math.PI / 4;

    public void updateProfile(PlayState playState) {
        updatePlayCount();
        addTimePlayed(playState.getHits().get(playState.getHits().size() - 1).getTime());
        updateMaxCombo(playState.getMaxCombo());
        addCoins(playState.getScore() / SCORE_COIN_RATIO);
        addRank(playState.getRank());
    }

    public int getPlayCount() {
        return playCount;
    }

    private void updatePlayCount() {
        ++playCount;
    }

    public int getCoins() {
        return coins;
    }

    private void addCoins(int amount) {
        coins += amount;
        coinsEarned += amount;
    }

    public void removeCoins(int amount) {
        if (amount > coins)
            throw new IllegalStateException("not enough money");

        coins -= amount;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    private void addTimePlayed(long time) {
        timePlayed += time;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    private void updateMaxCombo(int maxCombo) {
        this.maxCombo = Math.max(this.maxCombo, maxCombo);
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public float getSliderSensitivity() {
        return sliderSensitivity;
    }

    public void setSliderSensitivity(float sliderSensitivity) {
        this.sliderSensitivity = sliderSensitivity;
    }

    public int getItemCount(Item item) {
        Integer count = items.get(item);
        return count != null ? count : 0;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void addItem(Item item) {
        Integer count = items.get(item);
        items.put(item, count != null ? count + 1 : 1);
    }

    public void removeItem(Item item) {
        Integer count = items.get(item);
        if (count == null)
            throw new IllegalStateException("no item " + item.getId() + " remaining");

        if (count == 1)
            items.remove(item);
        else
            items.put(item, count - 1);
    }

    private void addRank(PlayRank rank) {
        Integer value = ranks.get(rank);
        ranks.put(rank, value != null ? value + 1 : 1);
    }

    public int getRankCount(PlayRank rank) {
        Integer count = ranks.get(rank);
        return count != null ? count : 0;
    }

    public List<String> save() {
        List<String> lines = new ArrayList<>();

        lines.add("playCount: " + playCount);
        lines.add("timePlayed: " + timePlayed);
        lines.add("maxCombo: " + maxCombo);
        lines.add("coins: " + coins);
        lines.add("coinsEarned: " + coinsEarned);
        lines.add("theme: " + theme);
        lines.add("speed: " + speed);
        lines.add("sliderSensitivity: " + sliderSensitivity);

        lines.add("[items]");
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            lines.add(entry.getKey().getId() + ": " + entry.getValue());
        }

        lines.add("[ranks]");
        for (Map.Entry<PlayRank, Integer> entry : ranks.entrySet()) {
            lines.add(entry.getKey() + ": " + entry.getValue());
        }

        return lines;
    }

    public void load(List<String> lines) {
        String section = "default";

        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }

            if (line.charAt(0) == '[') {
                section = line.substring(1, line.length() - 1);
                continue;
            }

            String[] segs = line.split(":", 2);
            String key = segs[0].trim();
            String value = segs[1].trim();

            switch (section) {
                case "items":
                    items.put(findItem(key), Integer.parseInt(value));
                    break;
                case "ranks":
                    ranks.put(PlayRank.valueOf(key), Integer.parseInt(value));
                    break;
                case "default":
                    switch (key) {
                        case "playCount":
                            playCount = Integer.parseInt(value);
                            break;
                        case "timePlayed":
                            timePlayed = Long.parseLong(value);
                            break;
                        case "maxCombo":
                            maxCombo = Integer.parseInt(value);
                            break;
                        case "coins":
                            coins = Integer.parseInt(value);
                            break;
                        case "coinsEarned":
                            coinsEarned = Integer.parseInt(value);
                            break;
                        case "theme":
                            theme = value;
                            break;
                        case "speed":
                            speed = Long.parseLong(value);
                            break;
                        case "sliderSensitivity":
                            sliderSensitivity = Float.parseFloat(value);
                            break;
                    }
                    break;
            }
        }
    }

    private Item findItem(String id) {
        for (Item item : ClassList.getItems()) {
            if (item.getId().equals(id))
                return item;
        }
        return null;
    }
}
