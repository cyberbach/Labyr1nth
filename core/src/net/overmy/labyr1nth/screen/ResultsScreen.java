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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.MyGdxGame.SCREEN;
import net.overmy.labyr1nth.logic.MyLevel;
import net.overmy.labyr1nth.neatresources.Fonts;
import net.overmy.labyr1nth.neatresources.IMG;
import net.overmy.labyr1nth.neatresources.SoundTrack;
import net.overmy.labyr1nth.neatresources.Text;
import net.overmy.labyr1nth.utils.AtmoManager;
import net.overmy.labyr1nth.utils.GroupHelper;
import net.overmy.labyr1nth.utils.LabelHelper;
import net.overmy.labyr1nth.utils.TIMER;


public class ResultsScreen extends Base2DScreen {

    private Stage  stage          = null;
    private Group  okgroup        = null;
    private Group  levelgroup     = null;
    private Group  outrogroup     = null;
    private Sprite gradientSprite = null;

    public ResultsScreen( final MyGdxGame game ) {
        super( game );
    }

    @Override
    public void show() {
        stage = new Stage();

        // Создаем Номер уровня и пишем, что уровень закончен
        int    nLevel          = MyLevel.getCurrent() + 1;
        String levelNumberText = Text.LEVEL.get() + nLevel + Text.LEVEL_END.get();
        Label  levelLabel      = LabelHelper.create( levelNumberText, Fonts.GUI_TEXT2 );
        //levelLabel.setDebug( true );
        levelgroup = GroupHelper.create( levelLabel );
        levelgroup.setPosition( Core.WIDTH_HALF, Core.HEIGHT * 0.9f );
        if ( !MyLevel.isLast() ) {
            stage.addActor( levelgroup );
        }

        // Создаем Результаты уровня
        String levelResultsText = "";
        levelResultsText += Text.LEVEL_TIME.get() + TIMER.get() + "\n";
        levelResultsText += Text.MAX_LEVEL_TIME.get() + TIMER.getMax() + "\n";
        levelResultsText += Text.LEVEL_KEYS.get() + Core.levelKeys + "\n";
        levelResultsText += Text.TOTAL_KEYS.get() + Core.keys;
        if ( MyLevel.isLast() ) {
            levelResultsText = Text.GAMEOVER.get();
        }
        Label introLabel = LabelHelper.createWithWrap( levelResultsText, Fonts.GUI_TEXT2 );
        outrogroup = GroupHelper.create( introLabel );
        outrogroup.setPosition( Core.WIDTH_HALF, Core.HEIGHT * 0.5f );
        stage.addActor( outrogroup );

        // Создаем кнопку OK
        Label okLabel = LabelHelper.create( Text.OK, Fonts.TITLE_BUTTONS );
        okgroup = GroupHelper.create( okLabel );
        okgroup.setPosition( Core.WIDTH * 0.9f, Core.HEIGHT * 0.15f );
        okgroup.addListener( new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y ) {
                if ( MyLevel.isLast() ) {
                    MyLevel.set( 0 );

                    scaleGroupsOut();
                    Core.fullGameFinished++;
                    transitionTo( SCREEN.MENU );
                }
                else {
                    MyLevel.next();

                    scaleGroupsOut();
                    transitionTo( SCREEN.INTRO );
                }
                SoundTrack.CLICK.play();
            }
        } );
        stage.addActor( okgroup );

        scaleGroupsIn();

        gradientSprite = IMG.GRADIENT.createSprite();

        InputProcessor keys      = new MyKeysProcessor();
        InputProcessor gestures  = new GestureDetector( this );
        InputProcessor processor = new InputMultiplexer( stage, keys, gestures );
        Gdx.input.setInputProcessor( processor );
        Gdx.input.setCatchBackKey( true );
        Gdx.input.setCatchMenuKey( true );
    }

    private void scaleGroupsIn() {
        GroupHelper.scaleIn( outrogroup );
        GroupHelper.scaleIn( okgroup );
        GroupHelper.scaleIn( levelgroup );
    }

    private void scaleGroupsOut() {
        GroupHelper.scaleOut( outrogroup );
        GroupHelper.scaleOut( okgroup );
        GroupHelper.scaleOut( levelgroup );
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
        float w   = Core.WIDTH;
        float hD3 = Core.HEIGHT / 3;
        float hD5 = Core.HEIGHT / 5;
        batch.setColor( Color.WHITE );
        batch.draw( gradientSprite, 0, hD3 * 2 + 5, 0, 0, w, hD3, 1, 1, 0 );
        batch.draw( gradientSprite, w, hD3 - 5, 0, 0, w, hD3, 1, 1, 180 );
        batch.draw( gradientSprite, hD5 - 5, 0, 0, 0, w, hD5, 1, 1, 90 );
        batch.draw( gradientSprite, w - hD5 + 5, Core.HEIGHT, 0, 0, w, hD5, 1, 1, -90 );
        batch.end();

        stage.draw();
    }

    @Override
    public void backButton() {
        if ( inTransition() ) { return; }
        MyLevel.next();
        scaleGroupsOut();
        SoundTrack.BACK.play();
        transitionTo( SCREEN.MENU );
    }

    @Override
    public void dispose() {
        super.dispose();
        outrogroup.clear();
        okgroup.clear();
        levelgroup.clear();
        gradientSprite = null;
        stage.dispose();
    }
}
