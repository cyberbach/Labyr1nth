/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.logic.LabyrinthGen;


class AtmoPart implements Disposable {

    private DoubleFloatAnimator xy             = null;
    private FloatAnimator       alpha          = null;
    private Sprite              particleSprite = null;
    private float           distance;
    private FLING_DIRECTION direction;

    public AtmoPart() {

        direction = FLING_DIRECTION.CENTER;

        distance = Core.randomAfterPercent( 0.2f, 0.5f );

        int sizex = 5 + (int) (16 * distance);
        int sizey = 5 + (int) (16 * distance);

        // try to gen something interesting
        Pixmap pixmap = new Pixmap( sizex, sizey, Pixmap.Format.RGBA4444 );
        pixmap.setColor( new Color( 1, 1, 1, 0 ) );
        pixmap.fill();

        pixmap.setColor( new Color( 1, 1, 1, 1 ) );
        boolean[][] lab;// = new boolean[14][14];
        lab = LabyrinthGen.gen( sizex, sizey, 4 );
        for ( int j = 0; j < sizey; j++ )
            for ( int i = 0; i < sizex; i++ )
                if ( lab[ i ][ j ] ) pixmap.drawPixel( i, j );
        //else pixmap.drawPixel( i, j, ((int) (255 * 0) << 24) | ((int) (255 * 1) << 16) |((int) (255 * 1) << 8) | ((int) (255 * 1)) );
        lab = null;

        Texture texture = new Texture( pixmap );
        particleSprite = new Sprite( texture );
        pixmap.dispose();
        // end of try

        xy = new DoubleFloatAnimator();
        alpha = new FloatAnimator();

        final float size = Core.HEIGHT * 0.065f * distance;
        particleSprite.setSize( size, size );

        particleSprite.setOrigin(
                particleSprite.getWidth() / 2 - particleSprite.getWidth() * MathUtils.random(),
                particleSprite.getHeight() / 2 -
                particleSprite.getHeight() * MathUtils.random() );

        float animationTime = Core.randomAfterPercent( 0.4f, 4.0f ) * (1 / distance);
        xy.setAnimationTime( animationTime );
        alpha.setAnimationTime( animationTime * 0.5f );

        gen();
    }

    public void setFling( float velocityX, float velocityY ) {

        xy.fromCurrent();
        xy.setTo( xy.currentX + velocityX * distance * 0.5f,
                  xy.currentY - velocityY * distance * 0.5f ).resetTime();
    }

    public void gen() {

        float destX;
        float destY;

        xy.setFrom( Core.WIDTH * MathUtils.random(), Core.HEIGHT * MathUtils.random() );

        switch ( direction ) {
            case LEFT:
                destX = xy.currentX - particleSprite.getWidth() * 2 * (1 / distance);
                destY = xy.currentY + particleSprite.getWidth() * 2 -
                        4 * particleSprite.getWidth() * MathUtils.random();
                break;
            case RIGHT:
                destX = xy.currentX + particleSprite.getWidth() * 2 * (1 / distance);
                destY = xy.currentY + particleSprite.getWidth() * 2 -
                        4 * particleSprite.getWidth() * MathUtils.random();
                break;
            case UP:
                destY = xy.currentY + particleSprite.getWidth() * 2 * (1 / distance);
                destX = xy.currentX + particleSprite.getWidth() * 2 -
                        4 * particleSprite.getWidth() * MathUtils.random();
                break;
            case DOWN:
                destY = xy.currentY - particleSprite.getWidth() * 2 * (1 / distance);
                destX = xy.currentX + particleSprite.getWidth() * 2 -
                        4 * particleSprite.getWidth() * MathUtils.random();
                break;
            default:

                if ( MathUtils.randomBoolean() ) {
                    destX = xy.currentX + particleSprite.getWidth() * 2 -
                            4 * particleSprite.getWidth() * MathUtils.random();
                    destY = MathUtils.randomBoolean() ?
                            xy.currentY + particleSprite.getWidth() * 2 * (1 / distance)
                            : xy.currentY - particleSprite.getWidth() * 2 * (1 / distance);
                } else {
                    destY = xy.currentY + particleSprite.getWidth() * 2 -
                            4 * particleSprite.getWidth() * MathUtils.random();
                    destX = MathUtils.randomBoolean() ?
                            xy.currentX + particleSprite.getWidth() * 2 * (1 / distance)
                            : xy.currentX - particleSprite.getWidth() * 2 * (1 / distance);
                }

                break;
        }

        xy.setTo( destX, destY ).resetTime();

        alpha.setFrom( 0.0f ).setTo( 1.0f ).resetTime();
    }

    public void render( final Batch batch ) {
        batch.setColor( Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, alpha.current * distance );
        batch.draw(
                particleSprite,
                xy.currentX,
                xy.currentY,
                particleSprite.getOriginX(),
                particleSprite.getOriginY(),
                particleSprite.getWidth(),
                particleSprite.getHeight(),
                1,
                1,
                xy.currentX + xy.currentY );
    }

    public void render( final Batch batch, final Color color ) {
        batch.setColor( color.r, color.g, color.b, alpha.current * distance );
        batch.draw(
                particleSprite,
                xy.currentX,
                xy.currentY,
                particleSprite.getOriginX(),
                particleSprite.getOriginY(),
                particleSprite.getWidth(),
                particleSprite.getHeight(),
                1,
                1,
                xy.currentX + xy.currentY );
    }

    public void update( final float delta ) {

        alpha.update( delta );
        xy.update( delta );
        if ( !alpha.isNeedToUpdate() ) alpha.setFrom( 1.0f ).setTo( 0.0f ).resetTime();
        if ( !xy.isNeedToUpdate() ) gen();
    }

    @Override
    public void dispose() {
        particleSprite = null;
    }

    public void fling( FLING_DIRECTION direction ) {
        this.direction = direction;
        switch ( direction ) {
            case LEFT:
                xy.setTo( xy.currentX - 100 * distance * 0.5f, xy.currentY - 0 * distance * 0.5f );
                break;
            case RIGHT:
                xy.setTo( xy.currentX + 100 * distance * 0.5f, xy.currentY - 0 * distance * 0.5f );
                break;
            case UP:
                xy.setTo( xy.currentX + 0 * distance * 0.5f, xy.currentY + 100 * distance * 0.5f );
                break;
            case DOWN:
                xy.setTo( xy.currentX + 0 * distance * 0.5f, xy.currentY - 100 * distance * 0.5f );
                break;
            default:
                break;
        }
    }

    public void render( Batch batch, float x, float y ) {
        batch.setColor( Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, alpha.current * distance );
        batch.draw(
                particleSprite,
                xy.currentX + x * distance,
                xy.currentY + y * distance,
                particleSprite.getOriginX(),
                particleSprite.getOriginY(),
                particleSprite.getWidth(),
                particleSprite.getHeight(),
                1,
                1,
                xy.currentX + xy.currentY );
    }

    public void render( Batch batch, Color color, float x, float y ) {
        batch.setColor( color.r, color.g, color.b, alpha.current * distance );
        batch.draw(
                particleSprite,
                xy.currentX + x * distance,
                xy.currentY + y * distance,
                particleSprite.getOriginX(),
                particleSprite.getOriginY(),
                particleSprite.getWidth(),
                particleSprite.getHeight(),
                1,
                1,
                xy.currentX + xy.currentY );
    }
}
