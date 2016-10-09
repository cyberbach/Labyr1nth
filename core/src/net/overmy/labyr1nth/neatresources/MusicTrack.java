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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import net.overmy.labyr1nth.Core;


public enum MusicTrack {

    TRACK1( "track1.mp3" ),
    TRACK2( "track2.mp3" ),
    TRACK3( "track3.mp3" ),
    TRACK4( "track4.mp3" ),
    ;

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

    public static void playRandom() {
        stopAll();

        int allMusicTracks   = MusicTrack.values().length;
        int randomTrack = MathUtils.random( allMusicTracks - 1 );

        MusicTrack.values()[ randomTrack ].play( true );
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
