package net.overmy.labyr1nth.logic;

/**
 * Created by Andrey (cb) Mikheev
 * Labyr1nth
 * 07.10.2016
 */

public final class MyLevel {

    private static int current;
    private static MyLevel ourInstance    = new MyLevel();
    private static int[]   labyrinthSizes = new int[]{
            8, 9, 10, 15, 11, 12, 12, 14, 14, 15,
            20, 14, 17, 18, 17, 18, 17, 18, 20, 16,
            21, 16, 17, 18, 17, 18, 17, 18, 12, 16,
            20, 16, 17, 18, 17, 18, 17, 18, 24, 16,
            18, 16, 17, 14, 12, 15, 14, 12, 9, 8
    };
    public static final int maxLevel = labyrinthSizes.length - 1;

    private MyLevel() {}

    public static void set( int level ) {
        current = level;
    }

    public static int getCurrent() {
        return current;
    }

    public static void next() {
        if ( current < maxLevel ) { current++; }
    }

    public static int getLabyrinthSize() {
        return labyrinthSizes[ current ];
    }

    public static boolean isLast() {
        return current == maxLevel;
    }
}
