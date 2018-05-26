/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Logger;


public final class NeatResources {

    private final static FileHandleResolver resolver = new InternalFileHandleResolver();
    private static       AssetManager       manager  = null;


    private NeatResources () {
    }


    public static void load () {
        AssetLoader fontsGenerator = new FreeTypeFontGeneratorLoader( resolver );
        AssetLoader fontsLoader = new FreetypeFontLoader( resolver );
        //AssetLoader particlesLoader = new ParticleEffectLoader( resolver );

        manager = new AssetManager();
        manager.getLogger().setLevel( Logger.DEBUG );
        manager.setLoader( FreeTypeFontGenerator.class, fontsGenerator );
        manager.setLoader( BitmapFont.class, ".ttf", fontsLoader );
        //manager.setLoader( ParticleEffect.class, particlesLoader );

        Texture.setAssetManager( manager );

        IMG.load( manager );
        Fonts.load( manager );
        MusicTrack.load( manager );
        SoundTrack.load( manager );
    }


    public static void build () {
        manager.finishLoading();

        IMG.build( manager );
        Fonts.build( manager );
        MusicTrack.build( manager );
        SoundTrack.build( manager );
    }


    public static void unload () {
        IMG.unload( manager );
        Fonts.unload( manager );
        MusicTrack.unload( manager );
        SoundTrack.unload( manager );

        manager.finishLoading();
        manager.dispose();
    }


    public static AssetManager getManager () {
        return manager;
    }
}
