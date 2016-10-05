/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.utils;

import com.badlogic.gdx.math.Interpolation;


public class DoubleFloatAnimator {

    public float currentX = 0.0f;
    public float currentY = 0.0f;

    private float fromX = 0.0f;
    private float toX   = 0.0f;

    private float fromY = 0.0f;
    private float toY   = 0.0f;

    private float time          = 0.0f;
    private float animationTime = 1.0f;

    private Interpolation interpX = null;
    private Interpolation interpY = null;

    public DoubleFloatAnimator() {
        this( 0.0f, 0.0f, 1.0f, 1.0f, 0.35f, Interpolation.fade );
    }

    public DoubleFloatAnimator( final float x1, final float y1, final float x2, final float y2, final float time ) {
        this( x1, y1, x2, y2, time, Interpolation.fade );
    }

    public DoubleFloatAnimator( final float x1, final float y1, final float x2, final float y2, final float time, final Interpolation interp ) {
        setFrom( x1, y1 );
        setTo( x2, y2 );
        setAnimationTime( time );
        setInterpolation( interp );
        resetTime();
    }

    public DoubleFloatAnimator( final float x1, final float y1, final float x2, final float y2, final float time, final Interpolation interpX, final Interpolation interpY ) {
        setFrom( x1, y1 );
        setTo( x2, y2 );
        setAnimationTime( time );
        setInterpolationX( interpX );
        setInterpolationY( interpY );
        resetTime();
    }

    public DoubleFloatAnimator setFrom( final float x, final float y ) {
        currentX = x;
        fromX = x;
        currentY = y;
        fromY = y;
        return this;
    }

    public DoubleFloatAnimator setTo( final float x, final float y ) {
        toX = x;
        toY = y;
        return this;
    }

    public DoubleFloatAnimator setInterpolation( final Interpolation interp ) {
        setInterpolationX( interp );
        setInterpolationY( interp );
        return this;
    }

    public DoubleFloatAnimator setInterpolationX( final Interpolation interp ) {
        this.interpX = interp;
        return this;
    }

    public DoubleFloatAnimator setInterpolationY( final Interpolation interp ) {
        this.interpY = interp;
        return this;
    }

    public DoubleFloatAnimator setAnimationTime( final float time ) {
        animationTime = time;
        return this;
    }

    public DoubleFloatAnimator resetTime() {
        time = 0.0f;
        return this;
    }

    public void update( final float delta ) {

        if ( !isNeedToUpdate() ) { return; }

        time += delta;

        currentX = interpX.apply( fromX, toX, time / animationTime );
        currentY = interpY.apply( fromY, toY, time / animationTime );
    }

    // Если "отведённое время" минус "прошедшее время" всё ещё больше нуля, то апдейтим
    public boolean isNeedToUpdate() {
        return animationTime - time > 0;
    }

    public DoubleFloatAnimator decX() {
        toX -= 1.0f;
        return this;
    }

    public DoubleFloatAnimator incX() {
        toX += 1.0f;
        return this;
    }

    public DoubleFloatAnimator decY() {
        toY -= 1.0f;
        return this;
    }

    public DoubleFloatAnimator incY() {
        toY += 1.0f;
        return this;
    }

    public DoubleFloatAnimator fromCurrent() {
        fromX = currentX;
        fromY = currentY;
        return this;
    }
}
