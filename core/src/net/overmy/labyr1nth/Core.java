package net.overmy.labyr1nth;

import com.badlogic.gdx.math.MathUtils;

import net.overmy.labyr1nth.logic.MyLevel;
import net.overmy.labyr1nth.neatresources.Settings;


/**
 * Created by Andrey (cb) Mikheev
 * MoFishing
 * 22.05.2016
 */
public final class Core {

    public final static float FADE = 0.35f;
    public static int   WIDTH;
    public static int   HEIGHT;
    public static int   WIDTH_HALF;
    public static int   HEIGHT_HALF;
    public static float aspectRatio;
    public static boolean sound = true;
    public static boolean music = true;
    public static long keys;
    public static int  zooms;
    public static long levelKeys;
    public static long steps;
    public static long finishedLevels;
    public static int  levelsWithoutZOOM;
    public static int  fullGameFinished;
    private static Core ourInstance = new Core();

    private Core() { }

    public static void init( final int width, final int height ) {
        WIDTH = width;
        HEIGHT = height;
        WIDTH_HALF = width >> 1;
        HEIGHT_HALF = height >> 1;

        // TODO check the aspect ratio
        aspectRatio = (float) height / 480.0f;
    }

    public static void loadSettings() {
        Settings.load();

        if ( Settings.NotFirstRun.getBoolean() ) {
            MyLevel.set( Settings.Level.getInteger() );
            sound = Settings.SoundFlag.getBoolean();
            music = Settings.MusicFlag.getBoolean();
            steps = Settings.Steps.getLong();
            keys = Settings.Keys.getLong();
            finishedLevels = Settings.FinishedLevels.getLong();
            zooms = Settings.Zooms.getInteger();
            levelsWithoutZOOM = Settings.LevelsWithoutZoom.getInteger();
            fullGameFinished = Settings.FullGameFinished.getInteger();
        }
        else {
            MyLevel.set( 0 );
            sound = true;
            music = true;
            steps = 0;
            keys = 0;
            finishedLevels = 0;
            zooms = 0;
            levelsWithoutZOOM = 0;
            fullGameFinished = 0;
        }
    }

    public static void saveSettings() {
        Settings.NotFirstRun.setBoolean( true );
        Settings.Level.setInteger( MyLevel.getCurrent() );
        Settings.SoundFlag.setBoolean( sound );
        Settings.MusicFlag.setBoolean( music );
        Settings.Steps.setLong( steps );
        Settings.Keys.setLong( keys );
        Settings.FinishedLevels.setLong( finishedLevels );
        Settings.Zooms.setInteger( zooms );
        Settings.LevelsWithoutZoom.setInteger( levelsWithoutZOOM );
        Settings.FullGameFinished.setInteger( fullGameFinished );

        Settings.save();
    }

    public static float randomAfterPercent( final float percent, final float rndValue ) {
        return rndValue * percent + MathUtils.random() * rndValue * (1 - percent);
    }
}
