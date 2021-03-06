package net.overmy.labyr1nth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.neatresources.GameColor;
import net.overmy.labyr1nth.neatresources.IMG;
import net.overmy.labyr1nth.neatresources.NeatResources;
import net.overmy.labyr1nth.utils.FloatAnimator;


/**
 * Created by Andrey (cb) Mikheev
 * Fluffy-Vegans-3D
 * 23.09.2016
 */
public class Base2DScreen implements Screen, GestureDetector.GestureListener {

    protected MyGdxGame game;
    protected SpriteBatch        batch           = null;
    private   FloatAnimator      transition      = null;
    private   MyGdxGame.SCREEN   nextScr         = null;
    private   OrthographicCamera camera          = null;
    private   Sprite             blackFullScreen = null;
    private   boolean            skipRender      = false;
    private   Color              bg              = GameColor.BG.get();

    public Base2DScreen( MyGdxGame game ) {
        this.game = game;
        Core.init( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho( false, Core.WIDTH, Core.HEIGHT );
        batch.setProjectionMatrix( camera.combined );

        transition = new FloatAnimator( 0, 1, Core.FADE );
        blackFullScreen = IMG.generateSquareSprite( Core.WIDTH, Core.HEIGHT );
    }

    @Override
    public void show() {
        InputProcessor keys      = new MyKeysProcessor();
        InputProcessor gestures  = new GestureDetector( this );
        InputProcessor processor = new InputMultiplexer( keys, gestures );
        Gdx.input.setInputProcessor( processor );
        Gdx.input.setCatchBackKey( true );
        Gdx.input.setCatchMenuKey( true );
    }

    @Override
    public void render( float delta ) {
        if ( skipRender ) { return; }

        update( delta );

        Gdx.gl.glClearColor( bg.r, bg.g, bg.b, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        draw();
        drawBlackScreen( delta );
    }

    public void draw() { }

    private void drawBlackScreen( float delta ) {

        if ( inTransition() ) {
            batch.begin();
            batch.setColor( 0, 0, 0, 1 - transition.current );
            batch.draw( blackFullScreen, 0, 0, Core.WIDTH, Core.HEIGHT );
            batch.end();
        }
        // Этот апдейт должен быть здесь, чтобы избежать ситуации
        // когда в момент перехода экранов на секунду появляется предыдущая сцена
        transition.update( delta );
        if ( transition.current != 1 && !inTransition() && nextScr != null ) {
            skipRender = true;
            switchGameScreen();
        }
    }

    /**
     * true - экраны переключаются
     * false - экраны не переключаются
     */
    public boolean inTransition() {
        return transition.isNeedToUpdate();
    }

    public void update( float delta ) {

    }

    /**
     * Включаем переключение экрана. Время перехода (Core.FADE)
     */
    public void transitionTo( MyGdxGame.SCREEN scr ) {
        transition.fromCurrent().setTo( 0 ).resetTime();
        nextScr = scr;
    }

    private void switchGameScreen() {
        skipRender = true;
        game.switchTo( nextScr );
    }

    @Override
    public void resize( int width, int height ) {
        Core.init( width, height );
    }

    @Override
    public void hide() {
        skipRender = true;
    }

    @Override
    public void pause() {
        skipRender = true;
    }

    @Override
    public void resume() {
        skipRender = false;
        NeatResources.getManager().update();
        NeatResources.getManager().finishLoading();
    }

    @Override
    public void dispose() {
        skipRender = true;

        camera = null;
        transition = null;
        blackFullScreen = null;

        batch.dispose();
        batch = null;
    }

    @Override
    public boolean touchDown( float x, float y, int pointer, int button ) {
        return false;
    }

    @Override
    public boolean tap( float x, float y, int count, int button ) {
        return false;
    }

    @Override
    public boolean longPress( float x, float y ) {
        return false;
    }

    @Override
    public boolean fling( float velocityX, float velocityY, int button ) {
        return false;
    }

    @Override
    public boolean pan( float x, float y, float deltaX, float deltaY ) {
        return true;
    }

    @Override
    public boolean panStop( float x, float y, int pointer, int button ) {
        return false;
    }

    @Override
    public boolean zoom( float initDistance, float distance ) { return false; }

    @Override
    public boolean pinch( Vector2 initPointer1, Vector2 initPointer2, Vector2 pointer1, Vector2 pointer2 ) {
        return false;
    }

    @Override
    public void pinchStop() { }

    public boolean touchUp( float x, float y, int pointer, int button ) { return false; }

    public boolean touchDragged( int x, int y, int pointer ) { return false; }

    public boolean scrolled( int amount ) {
        return false;
    }

    public void backButton() { }

    protected class MyKeysProcessor implements InputProcessor {

        @Override
        public boolean keyDown( int keycode ) {
            if ( (keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ) {
                backButton();
            }
            return false;
        }

        @Override
        public boolean keyUp( int keycode ) {
            return false;
        }

        @Override
        public boolean keyTyped( char character ) {
            return false;
        }

        @Override
        public boolean touchDown( int screenX, int screenY, int pointer, int button ) {
            return false;
        }

        @Override
        public boolean touchUp( int screenX, int screenY, int pointer, int button ) {
            return Base2DScreen.this.touchUp( screenX, Core.HEIGHT - screenY, pointer, button );
        }

        @Override
        public boolean touchDragged( int screenX, int screenY, int pointer ) {
            return Base2DScreen.this.touchDragged( screenX, Core.HEIGHT - screenY, pointer );
        }

        @Override
        public boolean mouseMoved( int screenX, int screenY ) {
            return false;
        }

        @Override
        public boolean scrolled( int amount ) {
            return Base2DScreen.this.scrolled( amount );
        }
    }
}
