package net.overmy.labyr1nth.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.neatresources.Fonts;
import net.overmy.labyr1nth.neatresources.Text;


/**
 * Created by Andrey (cb) Mikheev
 * Labyr1nth
 * 04.10.2016
 */

public final class LabelHelper {

    private static LabelHelper ourInstance = new LabelHelper();

    private LabelHelper() { }

    public static Label create( final Text text, final Fonts currentFont ) {
        return create( text.get(), currentFont );
    }

    public static Label create( final String text, final Fonts currentFont ) {
        Label returnLabel = new Label( text, currentFont.getStyle() );
        float x           = -returnLabel.getWidth() / 2;
        float y           = -returnLabel.getHeight() / 2;
        returnLabel.setPosition( x, y );
        return returnLabel;
    }

    public static Label createWithWrap( final Text text, final Fonts currentFont ) {
        return createWithWrap( text.get(), currentFont );
    }

    public static Label createWithWrap( final String text, final Fonts currentFont ) {
        Label returnLabel = new Label( text, currentFont.getStyle() );
        returnLabel.setWidth( Core.WIDTH * 0.7f );
        returnLabel.setWrap( true );
        float x = -returnLabel.getWidth() / 2;
        float y = -returnLabel.getHeight() / 2;
        returnLabel.setPosition( x, y );
        return returnLabel;
    }
}
