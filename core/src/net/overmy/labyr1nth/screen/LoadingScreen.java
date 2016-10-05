/**
 * OVERMY.NET - Make your device live! *
 * <p/>
 * Games: http://play.google.com/store/apps/developer?id=OVERMY
 *
 * @author Andrey Mikheev (cb)
 */

package net.overmy.labyr1nth.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.MyGdxGame.SCREEN;
import net.overmy.labyr1nth.neatresources.IMG;
import net.overmy.labyr1nth.neatresources.NeatResources;
import net.overmy.labyr1nth.utils.AtmoManager;


public class LoadingScreen extends Base2DScreen {

    final private String className = LoadingScreen.class.getSimpleName();
    Image loadingBar;
    Stage stage;

    boolean loaded = false;

    public LoadingScreen( final MyGdxGame game ) {
        super( game );

        stage = new Stage();
        loadingBar = new Image( IMG.generateSquareSprite( Core.WIDTH, Core.HEIGHT / 14 ) );
        stage.addActor( loadingBar );
    }

    @Override
    public void update( float delta ) {
        super.update( delta );
        stage.act( delta );

        if ( !loaded ) {

            if ( !NeatResources.getManager().update() ) {
                loadingBar.setScaleX( NeatResources.getManager().getProgress() * 1.35f );
            }
            else {
                NeatResources.build();
                AtmoManager.init();
                transitionTo( SCREEN.MENU );
                loaded = true;
            }
        }
    }

    @Override
    public void draw() {
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();

        loadingBar.clear();
        stage.dispose();

        Gdx.app.debug( className, "dispose" );
    }
}
