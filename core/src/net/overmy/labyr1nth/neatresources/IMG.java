/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public enum IMG {

    GRADIENT( "grad" );

    private final String name;

    private IMG( final String text ) {
        this.name = text;
    }

    public static Sprite generateSquareSprite( final int x, final int y ) {
        Pixmap pixmap = new Pixmap( x, y, Pixmap.Format.RGB888 );
        pixmap.setColor( Color.WHITE );
        pixmap.fill();
        Texture texture = new Texture( pixmap );
        return new Sprite( texture );
    }

    public TextureAtlas.AtlasRegion getRegion() {
        return NeatResources.getAtlas().findRegion( this.name );
    }

    public Image getImageActor( final float sizeX, final float sizeY ) {
        Image img = new Image( this.getRegion() );
        img.setSize( sizeX, sizeY );
        img.setPosition( -sizeX / 2, -sizeY / 2 );
        img.setOrigin( sizeX / 2, sizeY / 2 );
        return img;
    }

    public Sprite createSprite( final int x, final int y ) {
        Sprite sprite = NeatResources.getAtlas().createSprite( this.name );
        sprite.setRegionWidth( x );
        sprite.setRegionHeight( y );
        return sprite;
    }

    public Sprite createSprite() {
        return NeatResources.getAtlas().createSprite( this.name );
    }

    public Drawable getDrawable() {
        return new TextureRegionDrawable( this.getRegion() );
    }
}
