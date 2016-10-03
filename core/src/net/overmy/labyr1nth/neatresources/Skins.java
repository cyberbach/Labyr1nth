/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public enum Skins {

    TITLE, MENU_BUTTON, NUMBER, GUI_TEXT, GUI_SCORE;

    final Skin mySkin;

    private Skins() {
        mySkin = new Skin();
    }

    public static void build() {

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = Fonts.TITLE.get();
        Skins.TITLE.mySkin.add( "default", labelStyle );

        labelStyle = new LabelStyle();
        labelStyle.font = Fonts.TITLE.get();
        Skins.NUMBER.mySkin.add( "default", labelStyle );

        labelStyle = new LabelStyle();
        labelStyle.font = Fonts.GUI_TEXT1.get();
        Skins.GUI_TEXT.mySkin.add( "default", labelStyle );

        labelStyle = new LabelStyle();
        labelStyle.font = Fonts.GUI_TEXT1.get();
        Skins.GUI_SCORE.mySkin.add( "default", labelStyle );

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = Fonts.GUI_TEXT1.get();

        Pixmap pixmap = new Pixmap( 16, 16, Pixmap.Format.RGB888 );
        pixmap.setColor( GameColor.hexToColor( 0x1c1f27 ) );
        pixmap.fill();
        Texture       texture       = new Texture( pixmap );
        TextureRegion textureRegion = new TextureRegion( texture );
        Drawable      d             = new TextureRegionDrawable( textureRegion );
        textButtonStyle.down = d;
        pixmap.dispose();

        Pixmap pixmap2 = new Pixmap( 16, 16, Pixmap.Format.RGB888 );
        pixmap2.setColor( GameColor.hexToColor( 0x33363d ) );
        pixmap2.fill();
        Texture       texture2       = new Texture( pixmap2 );
        TextureRegion textureRegion2 = new TextureRegion( texture2 );
        Drawable      d2             = new TextureRegionDrawable( textureRegion2 );
        textButtonStyle.up = d2;
        pixmap2.dispose();

        Skins.MENU_BUTTON.mySkin.add( "default", textButtonStyle );
    }

    public Skin get() {

        return this.mySkin;
    }

}
