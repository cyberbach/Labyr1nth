package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public enum Fonts {

    TITLE( "k.otf", 80, 2, Color.WHITE, Color.DARK_GRAY ),
    GUI_TEXT( "k.otf", 44, 1, Color.WHITE, Color.GRAY ),
    LEVEL_INTRO_TEXT( "k.otf", 38, 1.4f, Color.WHITE, Color.GRAY ),;

    private final String     path;
    private final int        size;
    private final float      borderSize;
    private final Color      color;
    private final Color      borderColor;
    private       BitmapFont font;

    private Fonts( final String path, final int size, final float borderSize ) {
        this( path, size, borderSize, Color.WHITE, Color.BLACK );
    }

    private Fonts( final String path, final int size, final float borderSize, final Color color ) {
        this( path, size, borderSize, color, Color.BLACK );
    }

    private Fonts( final String path, final int size, final float borderSize, final Color color,
                   final Color borderColor ) {

        this.path = path;
        this.size = size;
        this.borderSize = borderSize;
        this.color = color;
        this.borderColor = borderColor;
    }

    /**
     * build all fonts
     */
    public static void build( final AssetManager manager ) {
        final float scale = 480.0f / Gdx.graphics.getHeight();

        for ( int i = 0; i < Fonts.values().length; i++ ) {
            FreeTypeFontGenerator myFontGenerator;
            FreeTypeFontParameter myFontParameters = new FreeTypeFontParameter();
            myFontParameters.borderWidth = Fonts.values()[ i ].borderSize;
            myFontParameters.borderColor = Fonts.values()[ i ].borderColor;
            myFontParameters.characters = createChars();

            myFontGenerator = manager.get( Fonts.values()[ i ].path, FreeTypeFontGenerator.class );
            myFontParameters.size = (int) (Fonts.values()[ i ].size * scale);
            myFontParameters.color = Fonts.values()[ i ].color;
            Fonts.values()[ i ].font = myFontGenerator.generateFont( myFontParameters );
        }
    }

    /**
     * load all font-files
     */
    public static void load( final AssetManager manager ) {

        for ( int i = 0; i < Fonts.values().length; i++ ) {
            if ( !manager.isLoaded( Fonts.values()[ i ].path ) )
                manager.load( Fonts.values()[ i ].path, FreeTypeFontGenerator.class );
        }
    }

    /**
     * dispose all fonts & unload all font-files
     */
    public static void unload( final AssetManager manager ) {

        for ( int i = 0; i < Fonts.values().length; i++ ) {
            if ( Fonts.values()[ i ].font != null ) {
                Fonts.values()[ i ].font.dispose();
                Fonts.values()[ i ].font = null;
                if ( manager.isLoaded( Fonts.values()[ i ].path ) )
                    manager.unload( Fonts.values()[ i ].path );
            }
        }
    }

    private static String createChars() {
        String fontChars = "";

        for ( int i = 32; i < 127; i++ )
            fontChars += (char) i;

        for ( int i = 1024; i < 1104; i++ )
            fontChars += (char) i;

        return fontChars;
    }

    public BitmapFont get() {
        return font;
    }
}