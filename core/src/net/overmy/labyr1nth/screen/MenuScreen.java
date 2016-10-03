/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.MyGdxGame.SCREEN;
import net.overmy.labyr1nth.neatresources.Fonts;
import net.overmy.labyr1nth.neatresources.MusicTrack;
import net.overmy.labyr1nth.neatresources.Skins;
import net.overmy.labyr1nth.neatresources.SoundTrack;
import net.overmy.labyr1nth.neatresources.Text;
import net.overmy.labyr1nth.utils.AtmoManager;


public class MenuScreen extends Base2DScreen {

    final float FADE_TIME = 0.35f;
    Label label              = null;
    Label startNewGameButton = null;
    Label continueButton     = null;

    Table table = null;
    Stage stage = null;

    public MenuScreen( final MyGdxGame game ) {
        super( game );
    }

    @Override
    public void show() {

        Core.music = false;
        Core.sound = false;

        MusicTrack.TRACK.play();

        startNewGameButton = new Label( Text.START_GAME.get(), Fonts.GUI_TEXT1.getStyle() );
        continueButton = new Label( Text.CONTINUE_GAME.get(), Fonts.GUI_TEXT1.getStyle() );

        label = new Label( Text.TITLE.get(), Skins.TITLE.get() );
        label.setAlignment( Align.center );
        label.setWrap( true );

        table = new Table();
        table.setDebug( true );
        table.setFillParent( true );

        if ( Core.level > 0 ) {
            table.add( label ).width( Core.WIDTH ).colspan( 2 );
        }
        else {
            table.add( label ).width( Core.WIDTH );
        }

        table.row();
        table.add( startNewGameButton );

        if ( Core.level > 0 ) { table.add( continueButton ); }

        table.addAction(
                Actions.sequence( Actions.moveTo( Core.WIDTH, 0, 0 ),
                                  Actions.moveTo( 0, 0, FADE_TIME ) ) );
        stage = new Stage();
        stage.addActor( table );

        startNewGameButton.addListener( new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y ) {
                Core.level = 0;
                table.addAction( Actions.moveTo( -Core.WIDTH, 0, FADE_TIME ) );
                SoundTrack.START.play();
                switchTo( SCREEN.GAME );
            }
        } );

        if ( Core.level > 0 ) {
            continueButton.addListener( new ClickListener() {
                @Override
                public void clicked( InputEvent event, float x, float y ) {
                    table.addAction( Actions.moveTo( -Core.WIDTH, 0, FADE_TIME ) );
                    SoundTrack.START.play();
                    switchTo( SCREEN.GAME );
                }
            } );
        }

        InputProcessor keysProcessor     = new MyKeysProcessor();
        InputProcessor gesturesProcessor = new GestureDetector( this );
        InputProcessor processor = new InputMultiplexer( stage, keysProcessor,
                                                         gesturesProcessor );
        Gdx.input.setInputProcessor( processor );
        Gdx.input.setCatchBackKey( true );
        Gdx.input.setCatchMenuKey( true );
        Gdx.app.debug( "overrided", "show" );
    }

    @Override
    public boolean tap( float x, float y, int count, int button ) {
/*
        table.addAction( Actions.moveTo( -Core.WIDTH, 0, FADE_TIME ) );
        SoundTrack.START.play();
        switchTo( SCREEN.GAME );*/

        return false;
    }

    @Override
    public void update( float delta ) {
        super.update( delta );
        AtmoManager.update( delta );
        stage.act( delta );
    }

    @Override
    public void draw() {

        batch.begin();
        AtmoManager.render( batch, Color.WHITE );

        batch.end();

        stage.draw();
    }

    @Override
    public void backButton() {

        table.addAction( Actions.moveTo( -Core.WIDTH, 0, FADE_TIME ) );

        switchTo( SCREEN.EXIT );
    }
}
