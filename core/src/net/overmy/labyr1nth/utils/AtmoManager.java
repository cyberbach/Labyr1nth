/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;


public class AtmoManager {

    private final static int MAX_PARTS = 40;

    private static ArrayList< AtmoPart > parts = null;

    private AtmoManager() {}

    public static void init() {

        parts = new ArrayList< AtmoPart >();

        for ( int i = 0; i < MAX_PARTS; i++ )
            parts.add( new AtmoPart() );
    }

    public static void setFling( float velocityX, float velocityY ) {

        for ( AtmoPart part : parts )
            part.setFling( velocityX, velocityY );
    }

    public static void render( final Batch batch ) {

        for ( AtmoPart part : parts )
            part.render( batch );
    }

    public static void render( final Batch batch, final Color c ) {

        for ( AtmoPart part : parts )
            part.render( batch, c );
    }

    public static void update( final float delta ) {

        for ( AtmoPart part : parts ) {

            part.update( delta );
        }
    }

    public static void dispose() {

        for ( AtmoPart part : parts ) {
            part.dispose();
        }

        parts.clear();
    }
}
