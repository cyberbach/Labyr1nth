package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.StringBuilder;


public enum Fonts {

    TITLE( "k.otf", 80, 0, Color.WHITE ),
    GUI_TEXT1( "k.otf", 44, 2, Color.WHITE, Color.DARK_GRAY ),
    GUI_TEXT2( "k.otf", 32, 2, Color.WHITE, Color.BLACK ),
    TABLE_TEXT( "k.otf", 32, 1, Color.WHITE, Color.BLACK ),
    ;

    public static final String extention = ".ttf";
    private final String     path;
    private final int        size;
    private final float      borderSize;
    private final Color      color;
    private final Color      borderColor;
    private       BitmapFont font;

    private Fonts( String path, int size, float borderSize ) {
        this( path, size, borderSize, Color.WHITE, Color.DARK_GRAY );
    }

    private Fonts( String path, int size, float borderSize, Color color ) {
        this( path, size, borderSize, color, Color.DARK_GRAY );
    }

    private Fonts( String path, int size, float borderSize, Color color, Color borderColor ) {
        this.path = path;
        this.size = size;
        this.borderSize = borderSize;
        this.color = color;
        this.borderColor = borderColor;
    }

    /**
     * build all fonts
     */
    public static void build( AssetManager manager ) {
        float scale = Gdx.graphics.getHeight() / 480.0f;

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
    public static void load( AssetManager manager ) {
        for ( int i = 0; i < Fonts.values().length; i++ ) {
            if ( !manager.isLoaded( Fonts.values()[ i ].path ) ) {
                manager.load( Fonts.values()[ i ].path, FreeTypeFontGenerator.class );
            }
        }
    }

    /**
     * dispose all fonts & unload all font-files
     */
    public static void unload( AssetManager manager ) {
        for ( int i = 0; i < Fonts.values().length; i++ ) {
            if ( Fonts.values()[ i ].font != null ) {
                Fonts.values()[ i ].font.dispose();
                Fonts.values()[ i ].font = null;
                if ( manager.isLoaded( Fonts.values()[ i ].path ) ) {
                    manager.unload( Fonts.values()[ i ].path );
                }
            }
        }
    }

    private static String createChars() {
        StringBuilder fontChars = new StringBuilder();

        for ( int i = 32; i < 127; i++ ) {
            fontChars.append( (char) i );
        }

        for ( int i = 1024; i < 1104; i++ ) {
            fontChars.append( (char) i );
        }

        return fontChars.toString();
    }

    public BitmapFont get() {
        return font;
    }

    public Label.LabelStyle getStyle(){
        return new Label.LabelStyle( this.get(), this.color );
    }
}