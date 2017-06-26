package x.logic.random;

import java.util.Random;

/**
 * Created by орда on 09.04.2017.
 */
public class XRandomGenerator {
    private static Random random = new Random();
    public static boolean generateBoolean() {
        return random.nextBoolean();
    }
    public static final boolean generateBoolean(int probability) {
        probability = getvalueInRange(probability, 0, 100);
        return (random.nextInt(100) < probability );
    }

    public static int getvalueInRange (int value, int min, int max) {
        return Math.max(Math.min(value,max),min);
    }
    public static int generateInteger(int min, int max) {
        return min+random.nextInt(max-min+1); // max - min +1
    }
    public static final float generateDeviation(float start) {
        float num = (float) XRandomGenerator.generateInteger(Math.round(-start*10000), Math.round(start*10000) )/10000.0f;
        return num;
    }


}
