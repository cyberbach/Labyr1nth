/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import net.overmy.labyr1nth.Core;


public enum SoundTrack {

    KEY1( "key1.mp3" ),
    KEY2( "key2.mp3" ),
    CLICK( "click.mp3" ),
    BACK( "back.mp3" ),
     ;

    private final String DEFAULT_DIR = "sound/";
    private final String path;
    private Sound snd = null;

    private SoundTrack( final String path ) {
        this.path = DEFAULT_DIR + path;
    }

    public static void stopAll() {
        for ( int i = 0; i < SoundTrack.values().length; i++ )
            SoundTrack.values()[ i ].snd.stop();
    }

    public static void build( final AssetManager manager ) {

        for ( int i = 0; i < SoundTrack.values().length; i++ )
            SoundTrack.values()[ i ].snd = manager.get( SoundTrack.values()[ i ].path,
                                                        Sound.class );
    }

    public static void load( final AssetManager manager ) {

        for ( int i = 0; i < SoundTrack.values().length; i++ )
            manager.load( SoundTrack.values()[ i ].path, Sound.class );
    }

    public static void unload( final AssetManager manager ) {

        for ( int i = 0; i < SoundTrack.values().length; i++ ) {

            if ( SoundTrack.values()[ i ].snd != null ) {
                SoundTrack.values()[ i ].snd.dispose();
                SoundTrack.values()[ i ].snd = null;

                manager.unload( SoundTrack.values()[ i ].path );
            }
        }
    }

    public void play() {
        if ( Core.sound ) {
            this.snd.play();
        } else {
            stopAll();
        }
    }

    public void stop() {
        this.snd.stop();
    }

    // HARDCODE!
    //public Music get() { return Gdx.audio.newMusic( Gdx.files.internal( this.path ) ); }

    public Sound get() {
        return this.snd;
    }

}
