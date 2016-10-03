package net.overmy.labyr1nth;

import com.badlogic.gdx.math.MathUtils;

import net.overmy.labyr1nth.neatresources.Settings;


/**
 * Created by Andrey (cb) Mikheev
 * MoFishing
 * 22.05.2016
 */
public final class Core {

    public static int   WIDTH;
    public static int   HEIGHT;
    public static int   WIDTH_HALF;
    public static int   HEIGHT_HALF;
    public static float aspectRatio;

    public static boolean sound = true;
    public static boolean music = true;
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
            sound = Settings.SoundFlag.getBoolean();
            music = Settings.MusicFlag.getBoolean();
            // TODO load hero
            level=Settings.Level.getInteger();
        }
        else {
            sound = true;
            music = true;

            level=0;
        }
    }

    public static int level =0;

    public static void saveSettings() {
        Settings.Level.setInteger( level );
        Settings.NotFirstRun.setBoolean( true );

        Settings.SoundFlag.setBoolean( sound );
        Settings.MusicFlag.setBoolean( music );

        Settings.save();
    }

    public static float randomAfterPercent( final float percent, final float rndValue ) {
        return rndValue * percent + MathUtils.random() * rndValue * (1 - percent);
    }

    public final static float FADE       = 0.63f;
}
