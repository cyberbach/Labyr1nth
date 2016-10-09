/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public enum Settings {

    NotFirstRun( Boolean.TYPE ),
    SoundFlag( Boolean.TYPE ),
    MusicFlag( Boolean.TYPE ),
    Level( Integer.TYPE ),
    FinishedLevels( Long.TYPE ),
    Steps( Long.TYPE ),
    Keys( Long.TYPE ),
    Zooms( Integer.TYPE ),
    LevelsWithoutZoom( Integer.TYPE ),
    FullGameFinished( Integer.TYPE ),
    //Inv( String.class ),
    ;

    static final private String className = Settings.class.getSimpleName();

    private final static String      SETTINGS = "game.prefs";
    private static       Preferences prefs    = null;

    private final Class< ? > type;
    private       boolean    bData;
    private       int        iData;
    private       String     sData;
    private       long       lData;

    private Settings( Class< ? > cls ) {
        this.type = cls;
    }

    public static void init() {
        prefs = Gdx.app.getPreferences( SETTINGS );
    }

    public static void load() {
        init();
        for ( int i = 0; i < Settings.values().length; i++ ) {
            String settingName  = Settings.values()[ i ].toString();
            String settingValue = "";
            Class<?> settingType = Settings.values()[ i ].type;

            if ( settingType.equals( Integer.TYPE ) ) {
                Settings.values()[ i ].iData = prefs.getInteger( settingName );
                settingValue += Settings.values()[ i ].iData;
            }

            if ( settingType.equals( Boolean.TYPE ) ) {
                Settings.values()[ i ].bData = prefs.getBoolean( settingName );
                settingValue += Settings.values()[ i ].bData;
            }

            if ( settingType.equals( Long.TYPE ) ) {
                Settings.values()[ i ].lData = prefs.getLong( settingName );
                settingValue += Settings.values()[ i ].lData;
            }

            // Base64Coder.encodeString(
            if ( settingType.equals( String.class ) ) {
                Settings.values()[ i ].sData = prefs.getString( settingName );
                settingValue += Settings.values()[ i ].sData;
            }

            Gdx.app.debug( className + " " + settingName + " (" + settingType + ")", settingValue );
        }
    }

    public static void save() {

        /*
       Settings.Inv.setString( "rV1S2S3BhV2S3BwV0BlV3" );
        Settings.Level.setInteger( 7 );
        Settings.CurrentWheel.setInteger( 2 );
        Settings.SoundFlag.setBoolean( true );
        Settings.MusicFlag.setBoolean( true );

        Settings.CurrentScene.setInteger( 2 );
        Settings.save();*/

        for ( int i = 0; i < Settings.values().length; i++ ) {
            String settingName = Settings.values()[ i ].toString();

            Class<?> settingType = Settings.values()[ i ].type;

            if ( settingType.equals( Integer.TYPE ) ) {
                prefs.putInteger( settingName, Settings.values()[ i ].iData );
            }

            if ( settingType.equals( Long.TYPE ) ) {
                prefs.putLong( settingName, Settings.values()[ i ].lData );
            }

            if ( settingType.equals( Boolean.TYPE ) ) {
                prefs.putBoolean( settingName, Settings.values()[ i ].bData );
            }

            if ( settingType.equals( String.class ) ) {
                prefs.putString( settingName, Settings.values()[ i ].sData );
            }
        }

        prefs.flush();
        Gdx.app.debug( className, "save" );
    }

    public String getString() {
        return sData;
    }

    public void setString( final String value ) {
        sData = value;
    }

    public boolean getBoolean() {
        return bData;
    }

    public void setBoolean( final boolean value ) {
        bData = value;
    }

    public int getInteger() {
        return iData;
    }

    public void setInteger( final int value ) {
        iData = value;
    }

    public long getLong() { return lData; }

    public void setLong( final long value ) { lData = value; }
}
