/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.neatresources;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;


public enum GameColor {

    BG( 0x000000 ),
    ORANGE1( 0xF2CD5C ),
    ORANGE2( 0xFFD350 ),
    RED1( 0x731209 ),
    //RED2( 0xC01503 ),
    PURPLE1( 0xA6774E ),

    CUSTOM1( 0x341C1C ),
    CUSTOM2( 0x4E302F ),
    CUSTOM3( 0x674A49 ),
    CUSTOM4( 0x51393A ),
    CUSTOM5( 0x997371 ),

    //sandy stone beach ocean diver
    THEME11( 0xE6E2AF ),
    THEME12( 0xEFECCA ),
    THEME13( 0xA7A37E ),

    //Honey Pot
    THEME21( 0x105B63 ),
    THEME25( 0xFFFAD5 ),
    THEME23( 0xBD4932 ),
    THEME22( 0xDB9E36 ),
    THEME24( 0xFFD34E ),

    //1944mustang
    THEME32( 0x7E8AA2 ),
    THEME33( 0x263248 ),
    THEME34( 0xFFFFFF ),
    THEME35( 0xFF9800 ),

    //Quiet Cry
    THEME55( 0xEEEFF7 ),

    //Dolores
    THEME61( 0x40627C ),
    THEME62( 0xD0A825 ),
    THEME63( 0xE8E595 ),
    THEME65( 0xFFFAE4 ),

    //Bloggy Gradient Blues
    THEME71( 0xABC8E2 ),
    THEME72( 0x375D81 ),
    THEME73( 0x183152 ),
    THEME74( 0xE1E6FA ),
    THEME75( 0xC4D7ED ),

    //Campfire
    THEME84( 0xF2E394 ),
    THEME83( 0x8C4646 ),
    THEME85( 0xD96459 ),
    THEME82( 0xF2AE72 ),

    //Vintage Ralph Lauren
    THEME95( 0x2F343B ),
    THEME91( 0x703030 ),
    THEME94( 0x7E827A ),
    THEME92( 0xE3CDA4 ),
    THEME93( 0xC77966 ),;

    private final Color color;

    GameColor( final int newColor ) {
        this.color = hexToColor( newColor );
    }

    GameColor( float r, float g, float b, float a ) {
        this.color = new Color( r, g, b, a );
    }

    public static Color getByNumber( final int n ) {
        return GameColor.values()[ n ].color;
    }

    public static Color random() {
        int allColors   = GameColor.values().length;
        int colorNumber = MathUtils.random( allColors - 1 );
        return GameColor.values()[ colorNumber ].get();
    }

    public final static Color hexToColor( int valueInHex ) {
        final int mask = 0xFF;

        final int int_r = (valueInHex >> 16);
        final int int_g = (valueInHex >> 8) & mask;
        final int int_b = valueInHex & mask;

        final float float_r = (float) int_r / (float) 0xFF;
        final float float_g = (float) int_g / (float) 0xFF;
        final float float_b = (float) int_b / (float) 0xFF;

        return new Color( float_r, float_g, float_b, 1 );
    }

    public Color get() {
        return color;
    }
}
