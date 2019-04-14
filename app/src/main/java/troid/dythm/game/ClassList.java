package troid.dythm.game;

import java.util.Arrays;
import java.util.List;

import troid.dythm.game.achievement.Achievement;
import troid.dythm.game.achievement.FirstPlay;
import troid.dythm.game.achievement.FiveHundredCombo;
import troid.dythm.game.achievement.Millionaire;
import troid.dythm.game.achievement.OneHundredCombo;
import troid.dythm.game.achievement.OneHundredPlays;
import troid.dythm.game.achievement.OneThousandCombo;
import troid.dythm.game.achievement.SSRanker;
import troid.dythm.game.item.BadMiss;
import troid.dythm.game.item.ComboBonus;
import troid.dythm.game.item.GoodMiss;
import troid.dythm.game.item.Item;
import troid.dythm.game.item.PerfectMiss;
import troid.dythm.game.item.ScoreMultiplier;

public class ClassList {
    private static List<Achievement> achievements;
    private static List<Item> items;
    public static List<Achievement> getAchievements() {
        if (achievements == null) {
            achievements = Arrays.asList(
                    new FirstPlay(),
                    new OneHundredPlays(),
                    new SSRanker(),
                    new OneHundredCombo(),
                    new FiveHundredCombo(),
                    new OneThousandCombo(),
                    new Millionaire()
            );
        }
        return achievements;
    }

    public static List<Item> getItems() {
        if (items == null) {
            items = Arrays.asList(
                    new ScoreMultiplier(),
                    new PerfectMiss(),
                    new GoodMiss(),
                    new BadMiss(),
                    new ComboBonus()
            );
        }
        return items;
    }
}
