/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.MyGdxGame.SCREEN;
import net.overmy.labyr1nth.neatresources.SoundTrack;
import net.overmy.labyr1nth.utils.AtmoManager;
import net.overmy.labyr1nth.neatresources.MusicTrack;
import net.overmy.labyr1nth.neatresources.Skins;
import net.overmy.labyr1nth.neatresources.Text;


public class MenuScreen extends Base2DScreen {

    final float FADE_TIME = 0.35f;
    Label label = null;
    Label startButton = null;
    Label continueButton = null;

    Table table = null;
    Stage stage = null;

    public MenuScreen( final MyGdxGame game ) {
        super( game );
    }

    @Override
    public void show() {
        super.show();

        Core.music = false;
        Core.sound = false;

        MusicTrack.TRACK.play();

        startButton = new Label( Text.START_GAME.get(), Skins.TITLE.get() );
        continueButton = new Label( Text.START_GAME.get(), Skins.TITLE.get() );

        label = new Label( Text.TITLE.get(), Skins.TITLE.get() );
        label.setAlignment( Align.center );
        label.setWrap( true );

        table = new Table();
        table.setFillParent( true );

        table.add( label ).width( Core.WIDTH * 0.9f );
        table.add( startButton ).width( Core.WIDTH * 0.9f );
        table.add( continueButton ).width( Core.WIDTH * 0.9f );

        table.addAction(
                Actions.sequence( Actions.moveTo( Core.WIDTH, 0, 0 ),
                                  Actions.moveTo( 0, 0, FADE_TIME ) ) );
        stage = new Stage();
        stage.addActor( table );
    }

    @Override
    public boolean tap( float x, float y, int count, int button ) {

        table.addAction( Actions.moveTo( -Core.WIDTH, 0, FADE_TIME ) );
        SoundTrack.START.play();
        switchTo( SCREEN.GAME );

        return false;
    }

    @Override
    public void update( float delta ) {
        super.update( delta );
        AtmoManager.update( delta );
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
