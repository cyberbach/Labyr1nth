package net.overmy.labyr1nth.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

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

    /**
     * Создаем LABEL и центрируем её относительно нуля
     * */
    public static Label create( final Text text, final Fonts currentFont ) {
        Label returnLabel = new Label( text.get(), currentFont.getStyle() );
        float x           = -returnLabel.getWidth() / 2;
        float y           = -returnLabel.getHeight() / 2;
        returnLabel.setPosition( x, y );
        return returnLabel;
    }
}
