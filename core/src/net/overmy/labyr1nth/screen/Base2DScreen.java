package net.overmy.labyr1nth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

    private final String className = Base2DScreen.class.getSimpleName();

    protected MyGdxGame game;
    protected MyGdxGame.SCREEN   nextScr         = null;
    protected SpriteBatch        batch           = null;
    protected OrthographicCamera camera          = null;
    protected   FloatAnimator      transition      = null;
    private   Sprite             blackFullScreen = null;
    private   FPSLogger          fpsLogger       = null;
    private   boolean            skipRender      = false;

    public Base2DScreen( MyGdxGame game ) {
        this.game = game;
        Core.init( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho( false, Core.WIDTH, Core.HEIGHT );
        batch.setProjectionMatrix( camera.combined );
        transition = new FloatAnimator( 0, 1, Core.FADE );
        blackFullScreen = IMG.generateSquareSprite( Core.WIDTH, Core.HEIGHT );

        fpsLogger = new FPSLogger();

        Gdx.app.debug( className, "new" );
    }

    @Override
    public void show() {
        InputProcessor keysProcessor     = new MyKeysProcessor();
        InputProcessor gesturesProcessor = new GestureDetector( this );
        Gdx.input.setInputProcessor( new InputMultiplexer( keysProcessor, gesturesProcessor ) );
        Gdx.input.setCatchBackKey( true );
        Gdx.input.setCatchMenuKey( true );
        Gdx.app.debug( className, "show" );
    }

    @Override
    public void render( float delta ) {
        if ( skipRender ) { return; }

        update( delta );

        Gdx.gl.glClearColor( GameColor.BG.get().r, GameColor.BG.get().g, GameColor.BG.get().b, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        draw();
        drawBlackScreen( delta );
    }

    public void draw() { }

    private void drawBlackScreen( float delta ) {

        if(transition==null){
            return;
        }

        if ( transition.isNeedToUpdate() ) {
            batch.begin();
            batch.setColor( 0, 0, 0, 1 - transition.current );
            batch.draw( blackFullScreen, 0, 0, Core.WIDTH, Core.HEIGHT );
            batch.end();
        }
        // Этот апдейт должен быть здесь, чтобы избежать ситуации
        // когда в момент перехода экранов на секунду появляется предыдущая сцена
        transition.update( delta );
        if ( transition.current != 1 && !transition.isNeedToUpdate() && nextScr!= null ) {
            skipRender = true;
            switchGameScreen();
        }
    }

    public void update( float delta ) {
        fpsLogger.log();
    }

    public void switchTo( MyGdxGame.SCREEN scr ) {
        transition.fromCurrent().setTo( 0 ).resetTime();
        nextScr = scr;
    }

    private void switchGameScreen() {
        skipRender = true;
        Gdx.app.debug( className,"switchGameScreen to "+nextScr.toString() );
        game.switchTo( nextScr );
    }

    @Override
    public void resize( int width, int height ) {
        Core.init( width, height );
    }

    @Override
    public void hide() {
        skipRender = true;
        Gdx.app.debug( className, "hide" );
    }

    @Override
    public void pause() {
        skipRender = true;
        Gdx.app.debug( className, "pause" );
    }

    @Override
    public void resume() {
        skipRender = false;
        Texture.setAssetManager( NeatResources.getManager() );
        Gdx.app.debug( className, "resume" );
    }

    @Override
    public void dispose() {
        skipRender = true;

        camera = null;
        transition = null;
        blackFullScreen = null;

        batch.dispose();
        batch = null;

        fpsLogger = null;

        Gdx.app.debug( className, "dispose" );
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
    public boolean zoom( float initialDistance, float distance ) {
        return false;
    }

    @Override
    public boolean pinch( Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2 ) {
        return false;
    }

    @Override
    public void pinchStop() { }

    public boolean touchUp( float x, float y, int pointer, int button ) { return false; }

    public boolean touchDragged( int x, int y, int pointer ) { return false; }

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
            return false;
        }
    }
}
