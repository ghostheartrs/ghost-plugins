package com.ghost.plugins.gemcrab.script;

import java.util.Random;

public class Util {

    public static long getRandomDelay(int min, int max) {
        Random random = new Random();
        return min + random.nextInt(max - min + 1);
    }
}
