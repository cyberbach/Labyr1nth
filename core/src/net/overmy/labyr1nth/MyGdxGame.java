package net.overmy.labyr1nth;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import net.overmy.labyr1nth.neatresources.NeatResources;
import net.overmy.labyr1nth.neatresources.Text;
import net.overmy.labyr1nth.screen.GameScreen;
import net.overmy.labyr1nth.screen.IntroScreen;
import net.overmy.labyr1nth.screen.LoadingScreen;
import net.overmy.labyr1nth.screen.MenuScreen;
import net.overmy.labyr1nth.screen.ResultsScreen;
import net.overmy.labyr1nth.utils.AtmoManager;


public class MyGdxGame extends Game {

    public boolean gpgsStateChange = false;

    public MyGdxGame() {
    }

    @Override
    public void create() {
        //Gdx.app.setLogLevel( Application.LOG_DEBUG );
        Gdx.app.setLogLevel( Application.LOG_NONE );
        Text.detect();
        Core.loadSettings();
        NeatResources.load();
        switchTo( SCREEN.LOADING );
    }

    public void switchTo( final SCREEN scr ) {
        if ( this.getScreen() != null ) {
            this.screen.dispose();
        }

        if ( scr == SCREEN.LOADING ) { setScreen( new LoadingScreen( this ) ); }
        else if ( scr == SCREEN.INTRO ) { setScreen( new IntroScreen( this ) ); }
        else if ( scr == SCREEN.GAME ) { setScreen( new GameScreen( this ) ); }
        else if ( scr == SCREEN.RESULTS ) { setScreen( new ResultsScreen( this ) ); }
        else if ( scr == SCREEN.MENU ) { setScreen( new MenuScreen( this ) ); }
        else if ( scr == SCREEN.EXIT ) { exit(); }
    }

    private void exit() {
        Core.saveSettings();
        NeatResources.unload();
        AtmoManager.dispose();

        Gdx.app.exit();
    }

    public enum SCREEN {
        LOADING, MENU, INTRO, GAME, RESULTS, EXIT
    }
}
