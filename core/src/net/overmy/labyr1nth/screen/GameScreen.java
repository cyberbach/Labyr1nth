/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.MyGdxGame.SCREEN;
import net.overmy.labyr1nth.logic.LabyrinthGen;
import net.overmy.labyr1nth.neatresources.Fonts;
import net.overmy.labyr1nth.neatresources.GameColor;
import net.overmy.labyr1nth.neatresources.IMG;
import net.overmy.labyr1nth.neatresources.SoundTrack;
import net.overmy.labyr1nth.neatresources.Text;
import net.overmy.labyr1nth.utils.AtmoManager;
import net.overmy.labyr1nth.utils.DoubleFloatAnimator;
import net.overmy.labyr1nth.utils.FloatAnimator;

import java.util.ArrayList;


public class GameScreen extends Base2DScreen {

    private final String              className          = GameScreen.class.getSimpleName();
    private       BitmapFont          font1              = Fonts.GUI_TEXT1.get();
    private       BitmapFont          font2              = Fonts.GUI_TEXT2.get();
    private       FloatAnimator       scalingScene       = null;
    private       float               scale              = 1.0f;
    private       boolean             swiped             = false;
    private       int                 squareSize         = 0;
    private       float               heightOffset       = 0;
    private       float               widthOffset        = 0;
    private       boolean             exit               = false;
    private       boolean[][]         lab                = null;
    private       Sprite              sprite             = null;
    private       Sprite              gradientSprite     = null;
    private       GridPoint2          workPosition       = null;
    private       GridPoint2          exitPosition       = null;
    private       Color               currentLevelColor  = null;
    private       DoubleFloatAnimator workPositionOffset = null;
    private       float               time               = 0.0f;
    private       FloatAnimator       textAnimator       = null;
    private       float               textAnimationTime  = 10.0f;
    private       boolean             textAnimationStop  = false;
    private       boolean             gameOverFlag       = false;
    private       String              textLevel          = Text.LEVEL.get() + (Core.level + 1);
    private GlyphLayout introWordsBeforeGameLayout;
    private GlyphLayout gameoverLayout;
    private ArrayList< Sprite > keys           = null;
    private Sprite              handSprite     = null;
    private Sprite              doorSprite     = null;
    private int                 secretStuff    = 0;
    private Sprite              secretSprite   = null;
    private GridPoint2          secretPosition = null;

    public GameScreen( final MyGdxGame game ) {
        super( game );
    }

    @Override
    public void show() {
        super.show();
        
        // TODO test acheivements on errors
        game.gpgs.unlockAchievement( 50 );

        squareSize = (int) (Core.HEIGHT * 0.6f);
        heightOffset = Core.HEIGHT * 0.2f;
        widthOffset = (Core.WIDTH - squareSize) / 2;
        currentLevelColor = GameColor.randomBut1();

        int labyrinthSize = 0;

        if ( Core.level > 47 ) { labyrinthSize = 10; }
        else if ( Core.level > 45 ) { labyrinthSize = 12; }
        else if ( Core.level > 42 ) { labyrinthSize = 20; }
        else if ( Core.level > 40 ) { labyrinthSize = 16; }
        else if ( Core.level > 35 ) { labyrinthSize = 22; }
        else if ( Core.level > 33 ) { labyrinthSize = 12; }
        else if ( Core.level > 30 ) { labyrinthSize = 20; }
        else if ( Core.level > 20 ) { labyrinthSize = 18; }
        else if ( Core.level > 10 ) { labyrinthSize = 16; }
        else if ( Core.level > 8 ) { labyrinthSize = 22; }
        else { labyrinthSize = 8; }

        lab = LabyrinthGen.gen( labyrinthSize, labyrinthSize, 5 + Core.level / 10 );

        ArrayList< GridPoint2 > points = new ArrayList< GridPoint2 >();

        // заполняем список, основаный на 2х-мерном массиве
        for ( int j = 0; j < labyrinthSize; j++ )
            for ( int i = 0; i < labyrinthSize; i++ )
                if ( lab[ i ][ j ] ) { points.add( new GridPoint2( i, j ) ); }

        // вытаскиваем из этого списка случайные точки
        
        // СТАРТОВАЯ ТОЧКА ЛАБИРИНТА
        int rndPosition = MathUtils.random( points.size() - 1 );
        workPosition = new GridPoint2();
        workPosition.set( points.get( rndPosition ).x, points.get( rndPosition ).y );
        points.remove( rndPosition );

        // Секретный СЕКРЕТ
        if ( MathUtils.random( 55 - Core.level ) == 0 ) {
            // Самая большая вероятность найти секрет - на последних уровнях
            secretStuff = 1 + MathUtils.random( 5 );
            rndPosition = MathUtils.random( points.size() - 1 );
            secretPosition = new GridPoint2();
            secretPosition.set( points.get( rndPosition ).x, points.get( rndPosition ).y );
            points.remove( rndPosition );

            int offsetSecretIMG = IMG.SECRET1.ordinal() + secretStuff;
            secretSprite = IMG.values()[ offsetSecretIMG ].createSprite();

            String debugString = "--- Secret" + (secretStuff - 1) + " present in";
            Gdx.app.debug( debugString, secretPosition.toString() );
        }

        // КЛЮЧИ
        int keysCount = 1 + MathUtils.random( labyrinthSize );
        keys = new ArrayList< Sprite >();
        for ( int i = 0; i < keysCount; i++ ) {
            if ( points.size() < 2 ) { break; }
            int    nOfSprite = IMG.KEY1.ordinal() + MathUtils.random( 5 ); // 0 1 2 3 4 5
            Sprite spr       = IMG.values()[ nOfSprite ].createSprite();

            rndPosition = MathUtils.random( points.size() - 1 );
            spr.setPosition( points.get( rndPosition ).x, points.get( rndPosition ).y );

            Color sprColor = GameColor.randomBut1();
            if ( sprColor.equals( currentLevelColor ) ) { sprColor.set( Color.WHITE ); }
            spr.setColor( sprColor );

            keys.add( spr );
            points.remove( rndPosition );
        }

        // ДВЕРЬ
        rndPosition = MathUtils.random( points.size() - 1 );
        exitPosition = new GridPoint2();
        exitPosition.set( points.get( rndPosition ).x, points.get( rndPosition ).y );
        int offsetDoorIMG = IMG.DOOR1.ordinal() + MathUtils.random( 2 );
        doorSprite = IMG.values()[ offsetDoorIMG ].createSprite();

        points.clear();

        // TODO вывод в консоль лабиринта
        String s = "\n";
        for ( int j = lab[ 0 ].length - 1; j >= 0; j-- ) {
            for ( int i = 0; i < lab.length; i++ ) {
                s += lab[ i ][ j ] ? "X" : " ";
            }
            s += "\n";
        }
        Gdx.app.debug( className + " Core.level " + Core.level, "" + s );

        String text      = Text.values()[ Core.level ].get();
        Color  textColor = GameColor.randomBut1();
        introWordsBeforeGameLayout = new GlyphLayout( Fonts.GUI_TEXT1.get(), text, textColor,
                                                      Core.WIDTH * 0.8f, Align.left, true );
        gameoverLayout = new GlyphLayout( Fonts.GUI_TEXT1.get(), Text.GAMEOVER.get(), Color.YELLOW,
                                          Core.WIDTH * 0.9f, Align.left, true );

        textAnimator = new FloatAnimator( 1, 0, Core.FADE );
        scalingScene = new FloatAnimator( 0, 1, Core.FADE * 3 );
        workPositionOffset = new DoubleFloatAnimator( 0, 0, 0, 0, Core.FADE * 0.5f );

        handSprite = IMG.HAND.createSprite();
        sprite = IMG.generateSquareSprite( squareSize, squareSize );
        gradientSprite = IMG.GRADIENT.createSprite();
    }

    @Override
    public void update( float delta ) {
        super.update( delta );

        if ( !textAnimationStop ) {
            textAnimationTime -= delta;
            if ( textAnimationTime < 0 ) {
                if ( gameOverFlag ) {
                    transitionTo( SCREEN.MENU );
                }
                textAnimationStop = true;
                textAnimator.fromCurrent().setTo( 1 ).resetTime();
            }
        }

        textAnimator.update( delta );

        AtmoManager.update( delta );

        time += delta;

        if ( workPositionOffset.isNeedToUpdate() ) {
            workPositionOffset.update( delta );

            if ( !workPositionOffset.isNeedToUpdate() ) {
                workPosition.x += workPositionOffset.currentX;
                workPosition.y += workPositionOffset.currentY;
                workPositionOffset.setFrom( 0, 0 ).setTo( 0, 0 ).resetTime();
                swiped = false;
            }
        }

        if ( scalingScene.isNeedToUpdate() ) {
            scale = scalingScene.current;
            scalingScene.update( delta );
            if ( !scalingScene.isNeedToUpdate() ) {
                if ( scalingScene.current > 0.9f ) { scale = 1.0f; }
            }
        }
    }

    @Override
    public void backButton() {
        if ( inTransition() || isScaling() ) { return; }

        SoundTrack.FINISH.play();

        transitionTo( SCREEN.MENU );
    }

    private boolean isScaling() {
        return scalingScene.isNeedToUpdate();
    }

    @Override
    public boolean fling( float velocityX, float velocityY, int button ) {
        super.fling( velocityX, velocityY, button );

        AtmoManager.setFling( velocityX, velocityY );

        if ( swiped || scale < 1.0f ) { return false; }

        float modVelX = (velocityX > 0) ? velocityX : -velocityX;
        float modVelY = (velocityY > 0) ? velocityY : -velocityY;

        if ( modVelX > modVelY ) {
            if ( velocityX > 0 ) {
                if ( lab[ workPosition.x - 1 ][ workPosition.y ] ) {
                    workPositionOffset.fromCurrent().setTo( -1, 0 ).resetTime();
                }
            }
            else {
                if ( lab[ workPosition.x + 1 ][ workPosition.y ] ) {
                    workPositionOffset.fromCurrent().setTo( 1, 0 ).resetTime();
                }
            }
        }
        else {
            if ( velocityY > 0 ) {
                if ( lab[ workPosition.x ][ workPosition.y + 1 ] ) {
                    workPositionOffset.fromCurrent().setTo( 0, 1 ).resetTime();
                }
            }
            else {
                if ( lab[ workPosition.x ][ workPosition.y - 1 ] ) {
                    workPositionOffset.fromCurrent().setTo( 0, -1 ).resetTime();
                }
            }
        }

        swiped = true;

        return true;
    }

    @Override
    public boolean tap( float x, float y, int count, int button ) {
        super.tap( x, y, count, button );

        if ( scale < 1.0f ) { return false; }

        if ( gameOverFlag ) {
            transitionTo( SCREEN.MENU );
        }

        if ( !textAnimationStop ) {
            textAnimationStop = true;
            textAnimator.fromCurrent().setTo( 1 ).resetTime();
        }

        for ( int i = 0; i < keys.size(); i++ ) {
            float keyX = keys.get( i ).getX();
            float keyY = keys.get( i ).getY();
            if ( workPosition.x == keyX && workPosition.y == keyY ) {
                if ( MathUtils.randomBoolean() ) { SoundTrack.KEY1.play(); }
                else { SoundTrack.KEY2.play(); }
                keys.remove( i );
                break;
            }
        }

        if ( !exit && keys.isEmpty() && workPosition.equals( exitPosition ) ) { exit = true; }

        if ( !exit && workPosition.equals( secretPosition ) ) {
            // TODO продублировать в настройки
            game.gpgs.unlockAchievement( 8 + secretStuff - 1 );
            secretStuff = 0;
        }

        if ( exit ) {
            if ( MathUtils.randomBoolean() ) { SoundTrack.EXIT1.play(); }
            else { SoundTrack.EXIT2.play(); }

            Core.level++;
            if ( Core.level > 49 ) {
                Core.level = 0;
                gameOverFlag = true;
                textAnimationStop = false;
                textAnimationTime = 10.0f;
                textAnimator.setFrom( 1 ).setTo( 0 ).resetTime();
            }
            if ( !gameOverFlag ) {
                transitionTo( SCREEN.GAME );
            }
        }

        Gdx.app.debug( className, "tap " + x + " " + y );
        return false;
    }

    @Override
    public void draw() {

        batch.begin();
        AtmoManager.render( batch, currentLevelColor );

        if ( !gameOverFlag ) {
            float w      = Core.WIDTH;
            float h      = Core.HEIGHT;
            float workX  = workPosition.x + workPositionOffset.currentX;
            float workY  = workPosition.y + workPositionOffset.currentY;
            float size   = squareSize;
            float sizeD2 = squareSize / 2; // D2 - это разделить (Square Size DIV 2)

            // Лабиринт
            batch.setColor( currentLevelColor );
            for ( int j = lab[ 0 ].length - 1; j >= 0; j-- ) {
                for ( int i = 0; i < lab.length; i++ ) {
                    // Выводим только TRUE клетки лабиринта (FALSE - это стены)
                    if ( lab[ i ][ j ] ) {
                        float x = widthOffset + (i * size - workX * size) * scale;
                        float y = heightOffset + (j * size - workY * size) * scale;
                        batch.draw( sprite, x, y, sizeD2, sizeD2, size, size, scale, scale, 0 );
                    }
                }
            }

            // Все ключи
            float sizeD4 = squareSize / 4;
            for ( int i = 0; i < keys.size(); i++ ) {
                Sprite spr  = keys.get( i );
                float  sprX = keys.get( i ).getX();
                float  sprY = keys.get( i ).getY();
                float  keyX = widthOffset + (sprX * size - workX * size + sizeD4) * scale;
                float  keyY = heightOffset + (sprY * size - workY * size + sizeD4) * scale;
                batch.setColor( spr.getColor() );
                batch.draw( spr, keyX, keyY, sizeD2, sizeD2, sizeD2, sizeD2, scale, scale, 0 );
            }

            // Дверь
            batch.setColor( Color.WHITE );
            float doorX = widthOffset + (exitPosition.x * size - workX * size + sizeD4) * scale;
            float doorY = heightOffset + (exitPosition.y * size - workY * size + sizeD4) * scale;
            batch.draw( doorSprite, doorX, doorY, sizeD2, sizeD2, sizeD2, sizeD2, scale, scale, 0 );

            // Secret
            if ( secretStuff != 0 ) {
                batch.setColor( Color.RED );
                float sX = widthOffset + (secretPosition.x * size - workX * size + sizeD4) * scale;
                float sY = heightOffset + (secretPosition.y * size - workY * size + sizeD4) * scale;
                batch.draw( secretSprite, sX, sY, sizeD2, sizeD2, sizeD2, sizeD2, scale, scale, 0 );
            }

            // Затемнение по краям
            float hD3 = Core.HEIGHT / 3;
            float hD5 = Core.HEIGHT / 5;
            batch.draw( gradientSprite, 0, hD3 * 2 + 5, 0, 0, w, hD3, 1, 1, 0 );
            batch.draw( gradientSprite, w, hD3 - 5, 0, 0, w, hD3, 1, 1, 180 );
            batch.draw( gradientSprite, hD5 - 5, 0, 0, 0, w, hD5, 1, 1, 90 );
            batch.draw( gradientSprite, w - hD5 + 5, Core.HEIGHT, 0, 0, w, hD5, 1, 1, -90 );

            // Показываем руку, при ЗУМЕ
            float alpha = 0.8f - (scalingScene.current - 0.2f);
            batch.setColor( Color.RED.r, Color.RED.g, Color.RED.b, alpha );
            if ( (scale != 1) && !inTransition() ) {
                float xo = widthOffset;
                float yo = heightOffset;
                batch.draw( handSprite, xo, yo, sizeD2, sizeD2, size, size, scale, scale, 0 );
            }

            // Все ключи - GUI
            for ( int i = 0; i < keys.size(); i++ ) {
                Sprite spr    = keys.get( i );
                float  sizeD7 = size / 7;
                float  sizeD8 = size / 8;
                batch.setColor( spr.getColor() );
                batch.draw( spr, w - sizeD7 - i * sizeD7, h - sizeD7, sizeD8, sizeD8 );
            }

            // Текущий уровень
            font2.draw( batch, textLevel, w * 0.05f, h * 0.9f );

            // Найдены все ключи
            if ( keys.isEmpty() ) {
                font2.draw( batch, Text.KEY_FOUND.get(), w * 0.05f, h * 0.8f );
            }

            // Время прохождения уровня
            font2.draw( batch, Text.TIME.get() + (int) time, w * 0.05f, h * 0.2f );

            // Вступительные слова
            font1.draw( batch, introWordsBeforeGameLayout,
                        Core.WIDTH_HALF - introWordsBeforeGameLayout.width / 2,
                        Core.HEIGHT_HALF + introWordsBeforeGameLayout.height / 2 -
                        textAnimator.current * Core.HEIGHT );
        }
        else {
            // Текст в конце игры
            font1.draw( batch, gameoverLayout,
                        Core.WIDTH_HALF - gameoverLayout.width / 2,
                        Core.HEIGHT_HALF + gameoverLayout.height / 2 -
                        textAnimator.current * Core.HEIGHT );
        }

        batch.end();
    }

    @Override
    public boolean zoom( float initDistance, float distance ) {

        if ( isScaling() ) { return false; }

        // Если initialDistance больше чем distance
        // то значит мы сдвигаем пальцы от краев к центру
        if ( initDistance > distance ) {
            //scale -= (distance / initialDistance) * 0.03f;
            if ( scalingScene.current != 0.2f ) {
                scalingScene.fromCurrent().setTo( 0.2f ).resetTime();
            }
        }
        // Если наоборот, то мы от центра разводим пальцы к краям
        else {
            //scale += (initialDistance / distance) * 0.03f;
            if ( scalingScene.current != 1.0f ) {
                scalingScene.fromCurrent().setTo( 1.0f ).resetTime();
            }
        }

        //if ( scale > 1.0f ) { scale = 1.0f; }
        //if ( scale < 0.2f ) { scale = 0.2f; }

        return false;
    }

    @Override
    public void dispose() {
        super.dispose();

        Core.saveSettings();
        Gdx.app.debug( className, "dispose" );
    }
}
