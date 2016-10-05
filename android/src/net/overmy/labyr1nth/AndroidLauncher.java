package net.overmy.labyr1nth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {

    final AndroidLauncher context = this;

    final AdMobImpl adMob;
    final GPGSImpl  gpgs;
    final MyGdxGame game;

    public AndroidLauncher() {
        adMob = new AdMobImpl( "ca-app-pub-3433473599086980/5967988151" );
        gpgs = new GPGSImpl(){
            @Override
            public void onConnected( Bundle connectionHint ) {
                game.gpgsStateChange = true;
            }
        };
        game = new MyGdxGame( adMob, gpgs );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        adMob.init( context );
        gpgs.init( context );

        AndroidApplicationConfiguration config;
        config = new AndroidApplicationConfiguration();
        View gameView = initializeForView( game, config );

        RelativeLayout layout = new RelativeLayout( this );
        layout.addView( gameView );
        layout.addView( adMob.adView, adMob.adParams );
        setContentView( layout );
    }

    @Override
    public void onStart() {
        super.onStart();
        // Во время старта приложения, подключаемся к GPGS
        // Так рекомендует делать GOOGLE
        //gpgs.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        gpgs.disconnect();
    }

    @Override
    public void onActivityResult( int request, int response, Intent intent ) {
        super.onActivityResult( request, response, intent );
        gpgs.onActivityResult( request, response, intent );
    }
}
