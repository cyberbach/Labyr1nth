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
    Level( Integer.TYPE )
    //Inv( String.class ),
    ;

    static final private String className = Settings.class.getSimpleName();

    private final static String      SETTINGS = "game.prefs";
    private static       Preferences prefs    = null;

    private final Class< ? > cls;
    private       boolean    bData;
    private       int        iData;
    private       String     sData;

    private Settings( Class< ? > cls ) {
        this.cls = cls;
    }

    public static void init() {
        prefs = Gdx.app.getPreferences( SETTINGS );
        Gdx.app.log( className, "init" );
    }

    public static void load() {
        init();
        for ( int i = 0; i < Settings.values().length; i++ ) {

            if ( Settings.values()[ i ].cls.equals( Integer.TYPE ) )
                Settings.values()[ i ].iData = prefs.getInteger(
                        Settings.values()[ i ].toString() );

            if ( Settings.values()[ i ].cls.equals( Boolean.TYPE ) )
                Settings.values()[ i ].bData = prefs.getBoolean(
                        Settings.values()[ i ].toString() );

            // Base64Coder.encodeString(
            if ( Settings.values()[ i ].cls.equals( String.class ) )
                Settings.values()[ i ].sData = prefs.getString( Settings.values()[ i ].toString() );

            Gdx.app.log( className + " " + Settings.values()[ i ].toString() + " " +
                         Settings.values()[ i ].cls.toString(),
                         Settings.values()[ i ].iData + " " +
                         Settings.values()[ i ].bData + " " + Settings.values()[ i ].sData );
        }


        Gdx.app.log( className, "load" );
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
            if ( Settings.values()[ i ].cls.equals( Integer.TYPE ) )
                prefs.putInteger( Settings.values()[ i ].toString(), Settings.values()[ i ].iData );

            if ( Settings.values()[ i ].cls.equals( Boolean.TYPE ) )
                prefs.putBoolean( Settings.values()[ i ].toString(), Settings.values()[ i ].bData );

            if ( Settings.values()[ i ].cls.equals( String.class ) )
                prefs.putString( Settings.values()[ i ].toString(), Settings.values()[ i ].sData );
        }

        prefs.flush();
        Gdx.app.log( className, "save" );
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

}
