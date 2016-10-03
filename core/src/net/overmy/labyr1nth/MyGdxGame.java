package net.overmy.labyr1nth;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import net.overmy.labyr1nth.utils.AtmoManager;
import net.overmy.labyr1nth.neatresources.NeatResources;
import net.overmy.labyr1nth.neatresources.Settings;
import net.overmy.labyr1nth.screen.GameScreen;
import net.overmy.labyr1nth.screen.LoadingScreen;
import net.overmy.labyr1nth.screen.MenuScreen;


public class MyGdxGame extends Game {

    private final AdMob ad;
    private final GPGS  gpgs;

    public MyGdxGame( AdMob ad, GPGS gpgs ) {
        this.ad = ad;
        this.gpgs = gpgs;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel( Application.LOG_DEBUG );
        //Gdx.app.setLogLevel( Application.LOG_NONE );
        Core.loadSettings();
        NeatResources.load();
        switchTo( SCREEN.LOADING );
    }

    public void switchTo( final SCREEN scr ) {
        if ( this.getScreen() != null ) {
            this.screen.dispose();
            ad.hide();
        }

        switch ( scr ) {
            case LOADING:
                setScreen( new LoadingScreen( this ) );
                break;

            case GAME:
                setScreen( new GameScreen( this ) );
                break;

            case MENU:
                ad.show();
                setScreen( new MenuScreen( this ) );
                break;

            case EXIT:
                Core.saveSettings();
                NeatResources.unload();
                AtmoManager.dispose();
                Gdx.app.exit();
                break;

            default:
                break;
        }
    }

    public enum SCREEN {
        LOADING, MENU, GAME, EXIT
    }
}
