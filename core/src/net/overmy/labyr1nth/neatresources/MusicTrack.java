/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import net.overmy.labyr1nth.Core;


public enum MusicTrack {

    TRACK( "track.mp3" ),;

    private final String DEFAULT_DIR = "music/";
    private final String path;
    private Music music = null;

    private MusicTrack( final String path ) {
        this.path = DEFAULT_DIR + path;
    }

    public static void stopAll() {
        for ( int i = 0; i < MusicTrack.values().length; i++ )
            MusicTrack.values()[ i ].music.stop();
    }

    public static void build( final AssetManager manager ) {

        for ( int i = 0; i < MusicTrack.values().length; i++ )
            MusicTrack.values()[ i ].music = manager.get( MusicTrack.values()[ i ].path,
                                                          Music.class );
    }

    public static void load( final AssetManager manager ) {

        for ( int i = 0; i < MusicTrack.values().length; i++ )
            manager.load( MusicTrack.values()[ i ].path, Music.class );
    }

    public static void unload( final AssetManager manager ) {

        for ( int i = 0; i < MusicTrack.values().length; i++ ) {

            if ( MusicTrack.values()[ i ].music != null ) {
                MusicTrack.values()[ i ].music.dispose();
                MusicTrack.values()[ i ].music = null;

                manager.unload( MusicTrack.values()[ i ].path );
            }
        }
    }

    public void play( boolean loop ) {

        if ( Core.music ) {
            this.music.setLooping( loop );
            this.music.play();
        } else {
            stopAll();
        }
    }

    public void setPan( float x ) {
        this.music.setPan( x, 1 );
    }

    public void play() {

        if ( Core.music ) {
            this.music.play();
        } else {
            stopAll();
        }
    }

    public void stop() {
        this.music.stop();
    }

    // HARDCODE!
    //public Music get() { return Gdx.audio.newMusic( Gdx.files.internal( this.path ) ); }

    public Music get() {
        return this.music;
    }
}
