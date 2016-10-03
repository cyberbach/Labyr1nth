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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.MyGdxGame.SCREEN;
import net.overmy.labyr1nth.utils.AtmoManager;
import net.overmy.labyr1nth.logic.LabyrinthGen;
import net.overmy.labyr1nth.neatresources.Fonts;
import net.overmy.labyr1nth.neatresources.GameColor;
import net.overmy.labyr1nth.neatresources.IMG;
import net.overmy.labyr1nth.neatresources.Settings;
import net.overmy.labyr1nth.neatresources.SoundTrack;
import net.overmy.labyr1nth.neatresources.Text;
import net.overmy.labyr1nth.utils.DoubleFloatAnimator;
import net.overmy.labyr1nth.utils.FloatAnimator;

import java.util.ArrayList;


public class GameScreen extends Base2DScreen {
    
    //private final float               FADE_TIME          = 0.35f;
    final private String  className = GameScreen.class.getSimpleName();
    private       boolean swiped    = false;

    private int                     squareSize         = 0;
    private float                   heightOffset       = 0;
    private float                   widthOffset        = 0;
    private boolean                 key                = false;
    private boolean                 exit               = false;
    private boolean[][]             lab                = null;
    private Sprite                  sprite             = null;
    private Sprite                  gradientSprite     = null;
    private GridPoint2              workPosition       = null;
    private ArrayList< GridPoint2 > keyPosition        = null;
    private GridPoint2              exitPosition       = null;
    private Color                   currentLevelColor  = null;
    private DoubleFloatAnimator     workPositionOffset = null;
    private float                   time               = 0.0f;
    private FloatAnimator           textAnimator       = null;
    private float                   textAnimationTime  = 10.0f;
    private boolean                 textAnimationStop  = false;
    private boolean                 gameOverFlag       = false;
    private GlyphLayout textLayout;
    private GlyphLayout gameoverLayout;

    public GameScreen( final MyGdxGame game ) {
        super( game );
    }

    @Override
    public void show() {
        super.show();

        squareSize = (int) (Core.HEIGHT * 0.6f);
        heightOffset = Core.HEIGHT * 0.2f;
        widthOffset = (Core.WIDTH - squareSize) / 2;

        workPositionOffset = new DoubleFloatAnimator();
        workPositionOffset.setFrom( 0, 0 ).setTo( 0, 0 ).setAnimationTime( Core.FADE * 0.5f );

        gradientSprite = IMG.GRADIENT.createSprite();

        currentLevelColor = GameColor.getByNumber( MathUtils.random.nextInt( GameColor.values()
                                                                                     .length ) );

        int labyrinthWidth  = 0;
        int labyrinthHeight = 0;

        if ( Core.level > 47 ) {
            labyrinthWidth = 10;
            labyrinthHeight = 10;
        }
        else if ( Core.level > 45 ) {
            labyrinthWidth = 12;
            labyrinthHeight = 12;
        }
        else if ( Core.level > 40 ) {
            labyrinthWidth = 16;
            labyrinthHeight = 16;
        }
        else if ( Core.level > 35 ) {
            labyrinthWidth = 22;
            labyrinthHeight = 22;
        }
        else if ( Core.level > 30 ) {
            labyrinthWidth = 20;
            labyrinthHeight = 20;
        }
        else if ( Core.level > 20 ) {
            labyrinthWidth = 18;
            labyrinthHeight = 18;
        }
        else if ( Core.level > 10 ) {
            labyrinthWidth = 16;
            labyrinthHeight = 16;
        }
        else {
            labyrinthWidth = 14;
            labyrinthHeight = 14;
        }

        workPosition = new GridPoint2();
        keyPosition = new ArrayList< GridPoint2 >();
        exitPosition = new GridPoint2();

        lab = LabyrinthGen.gen( labyrinthWidth, labyrinthHeight, 5 + Core.level / 10 );

        ArrayList< GridPoint2 > points = new ArrayList< GridPoint2 >();

        // заполняем список, основаный на 2х-мерном массиве
        for ( int j = 0; j < labyrinthWidth; j++ )
            for ( int i = 0; i < labyrinthHeight; i++ )
                if ( lab[ i ][ j ] ) { points.add( new GridPoint2( i, j ) ); }

        // вытаскиваем из этого списка случайные точки
        int randomPosition1 = MathUtils.random.nextInt( points.size() );
        workPosition.set( points.get( randomPosition1 ).x, points.get( randomPosition1 ).y );
        points.remove( randomPosition1 );

        int keysCount = 1 + MathUtils.random( 4 );

        for ( int i = 0; i < keysCount; i++ ) {
            if ( points.size() < 2 ) { break; }
            int randomPosition2 = MathUtils.random.nextInt( points.size() );
            keyPosition.add( new GridPoint2( points.get( randomPosition2 ).x, points.get(
                    randomPosition2 ).y )
                           );
            points.remove( randomPosition2 );
        }

        int randomPosition3 = MathUtils.random.nextInt( points.size() );
        exitPosition.set( points.get( randomPosition3 ).x, points.get( randomPosition3 ).y );

        // больше список не нужен
        points.clear();

        String s = "\n";

        for ( int j = lab[ 0 ].length - 1; j >= 0; j-- ) {
            for ( int i = 0; i < lab.length; i++ ) {
                s += lab[ i ][ j ] ? "X" : " ";
            }
            s += "\n";
        }

        sprite = IMG.generateSquareSprite( squareSize, squareSize );

        textLayout = new GlyphLayout( Fonts.LEVEL_INTRO_TEXT.get(),
                                      Text.values()[ 1 + Core.level ].get(),
                                      GameColor.getByNumber(
                                              MathUtils.random.nextInt(
                                                      GameColor.values().length ) ),
                                      Core.WIDTH * 0.8f, Align.left, true );
        gameoverLayout = new GlyphLayout( Fonts.LEVEL_INTRO_TEXT.get(),
                                          Text.GAMEOVER.get(),
                                          Color.YELLOW,
                                          Core.WIDTH * 0.9f, Align.left, true );
        textAnimator = new FloatAnimator();
        textAnimator.setFrom( 1 ).setTo( 0 );

        Gdx.app.debug( className + " Core.level " + Core.level, "" + s );
    }

    @Override
    public void update( float delta ) {
        super.update( delta );

        if ( !textAnimationStop ) {
            textAnimationTime -= delta;
            if ( textAnimationTime < 0 ) {
                if ( gameOverFlag ) {
                    switchTo( SCREEN.MENU );
                }
                textAnimationStop = true;
                textAnimator.fromCurrent().setTo( 1 ).resetTime();
            }
        }

        textAnimator.update( delta );

        AtmoManager.update( delta );

        time += delta;

        workPositionOffset.update( delta );
        if ( !workPositionOffset.isNeedToUpdate() ) {
            workPosition.x += workPositionOffset.currentX;
            workPosition.y += workPositionOffset.currentY;
            workPositionOffset.setFrom( 0, 0 ).setTo( 0, 0 ).resetTime();
            swiped = false;
        }
    }

    @Override
    public void backButton() {

        SoundTrack.FINISH.play();

        switchTo( SCREEN.MENU );
    }

    @Override
    public boolean fling( float velocityX, float velocityY, int button ) {
        super.fling( velocityX, velocityY, button );

        if ( swiped ) { return false; }

        AtmoManager.setFling( velocityX, velocityY );

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

        if ( gameOverFlag ) {
            switchTo( SCREEN.MENU );
        }

        if ( !textAnimationStop ) {
            textAnimationStop = true;
            textAnimator.fromCurrent().setTo( 1 ).resetTime();
        }

        if ( !key && workPosition.equals( keyPosition ) ) {
            if ( MathUtils.random.nextBoolean() ) { SoundTrack.KEY1.play(); }
            else { SoundTrack.KEY2.play(); }
            key = true;
        }
        if ( !exit && key && workPosition.equals( exitPosition ) ) { exit = true; }

        if ( exit ) {
            switch ( MathUtils.random.nextInt( 2 ) ) {
                case 1:
                    SoundTrack.EXIT1.play();
                    break;
                default:
                    SoundTrack.EXIT2.play();
                    break;
            }

            Core.level++;
            if ( Core.level > 49 ) {
                Core.level = 0;
                gameOverFlag = true;
                textAnimationStop = false;
                textAnimationTime = 10.0f;
                textAnimator.setFrom( 1 ).setTo( 0 ).resetTime();
            }
            if ( !gameOverFlag ) {
                Settings.Level.setInteger( Core.level );
                switchTo( SCREEN.GAME );
            }
        }

        Gdx.app.debug( className, "tap " + x + " " + y );
        return false;
    }

    @Override
    public void draw() {

        batch.begin();
        batch.setColor( currentLevelColor );

        if ( !gameOverFlag ) {
            for ( int j = lab[ 0 ].length - 1; j >= 0; j-- ) {
                for ( int i = 0; i < lab.length; i++ ) {
                    if ( lab[ i ][ j ] ) {
                        batch.draw(
                                sprite,
                                widthOffset + i * squareSize -
                                (workPosition.x + workPositionOffset.currentX) * squareSize,
                                heightOffset + j * squareSize -
                                (workPosition.y + workPositionOffset.currentY) * squareSize,
                                0, 0,
                                squareSize, squareSize,
                                1, 1, 0 );
                    }
                }
            }
        }

        if ( !gameOverFlag ) {
            if ( !key ) {
                for(GridPoint2 kp : keyPosition) {
                    batch.setColor( Color.CORAL );
                    Fonts.GUI_TEXT.get().draw(
                            batch,
                            Text.KEY.get(),
                            widthOffset + kp.x * squareSize -
                            (workPosition.x + workPositionOffset.currentX) * squareSize
                            + squareSize / 2,
                            heightOffset + kp.y * squareSize -
                            (workPosition.y + workPositionOffset.currentY) * squareSize
                            + squareSize / 2 );
                }
            }
        }

        if ( !gameOverFlag ) {
            if ( !exit ) {
                Fonts.GUI_TEXT.get().draw(
                        batch,
                        Text.EXIT.get(),
                        widthOffset + exitPosition.x * squareSize -
                        (workPosition.x + workPositionOffset.currentX) * squareSize
                        + squareSize / 2,
                        heightOffset + exitPosition.y * squareSize -
                        (workPosition.y + workPositionOffset.currentY) * squareSize
                        + squareSize / 2 );
            }
        }

        AtmoManager.render( batch, currentLevelColor );

        // затемнение по краям
        batch.setColor( currentLevelColor );
        batch.draw( gradientSprite, 0, (Core.HEIGHT / 3) * 2 + 5, 0, 0,
                    Core.WIDTH,
                    Core.HEIGHT / 3, 1, 1, 0 );
        batch.draw( gradientSprite, Core.WIDTH, Core.HEIGHT / 3 - 5, 0,
                    0, Core.WIDTH,
                    Core.HEIGHT / 3, 1, 1, 180 );
        batch.draw( gradientSprite, Core.HEIGHT / 5 - 5, 0, 0, 0, Core.WIDTH,
                    Core.HEIGHT / 5, 1, 1, 90 );
        batch.draw( gradientSprite, Core.WIDTH - Core.HEIGHT / 5 + 5,
                    Core.HEIGHT, 0, 0,
                    Core.WIDTH, Core.HEIGHT / 5, 1, 1, -90 );

        if ( !gameOverFlag ) {
            Fonts.GUI_TEXT.get().draw(
                    batch,
                    Text.LEVEL.get() + (Core.level + 1),
                    Core.WIDTH * 0.05f + Core.WIDTH * (1 - transition.current),
                    Core.HEIGHT * 0.9f
                                     );
        }

        if ( !gameOverFlag ) {
            if ( key ) {
                Fonts.GUI_TEXT.get().draw(
                        batch,
                        Text.KEY_FOUND.get(),
                        Core.WIDTH * 0.05f + Core.WIDTH * (1 - transition.current),
                        Core.HEIGHT * 0.8f );
            }
        }

        if ( !gameOverFlag ) {
            Fonts.GUI_TEXT.get().draw(
                    batch,
                    Text.TIME.get() + (int) time,
                    Core.WIDTH * 0.05f + Core.WIDTH * (1 - transition.current),
                    Core.HEIGHT * 0.2f );
        }

        if ( !gameOverFlag ) {
            Fonts.LEVEL_INTRO_TEXT.get().draw( batch, textLayout,
                                               Core.WIDTH_HALF - textLayout.width / 2,
                                               Core.HEIGHT_HALF + textLayout.height / 2 -
                                               textAnimator.current * Core.HEIGHT );
        }
        if ( gameOverFlag ) {
            Fonts.LEVEL_INTRO_TEXT.get().draw( batch, gameoverLayout,
                                               Core.WIDTH_HALF - gameoverLayout.width / 2,
                                               Core.HEIGHT_HALF + gameoverLayout.height / 2 -
                                               textAnimator.current * Core.HEIGHT );
        }

        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();

        Settings.Level.setInteger( Core.level );
        Gdx.app.debug( className, "dispose" );
    }
}
