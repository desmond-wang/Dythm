package troid.dythm.ui.util;

public class MathHelper {
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
