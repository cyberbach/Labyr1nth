/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Logger;


public final class NeatResources {

    final private static String             className  = NeatResources.class.getSimpleName();
    private final static FileHandleResolver resolver   = new InternalFileHandleResolver();
    private final static String             ATLAS_PATH = "pack";
    private static       AssetManager       manager    = null;
    private static       TextureAtlas       atlas      = null;

    private NeatResources() {}

    public static void load() {
        manager = new AssetManager();
        manager.getLogger().setLevel( Logger.DEBUG );
        manager.setLoader( FreeTypeFontGenerator.class,
                           new FreeTypeFontGeneratorLoader( resolver ) );
        manager.setLoader( BitmapFont.class, ".ttf", new FreetypeFontLoader( resolver ) );
        manager.setLoader( ParticleEffect.class, new ParticleEffectLoader( resolver ) );

        manager.load( ATLAS_PATH, TextureAtlas.class );
        Fonts.load( manager );
        MusicTrack.load( manager );
        SoundTrack.load( manager );

        Gdx.app.debug( className, "load" );
    }

    public static void build() {
        flush();
        atlas = manager.get( ATLAS_PATH, TextureAtlas.class );
        Fonts.build( manager );
        MusicTrack.build( manager );
        SoundTrack.build( manager );
        Skins.build();
        Gdx.app.debug( className, "builded" );
    }

    public static void unload() {
        //manager.unload( ATLAS_PATH );
        Fonts.unload( manager );
        MusicTrack.unload( manager );
        SoundTrack.unload( manager );
        flush();
        atlas.dispose();
        manager.dispose();
        Gdx.app.debug( className, "unload" );
    }

    public static TextureAtlas getAtlas() {
        return atlas;
    }

    public static AssetManager getManager() {
        return manager;
    }

    public static void flush() {
        manager.finishLoading();
    }
}
