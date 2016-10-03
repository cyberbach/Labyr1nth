package net.overmy.labyr1nth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;


/**
 * Created by Andrey (cb) Mikheev
 * TutorialGPGS
 * 26.09.2016
 */
public class GPGSImpl implements GPGS, GoogleApiClient.ConnectionCallbacks,
                                 GoogleApiClient.OnConnectionFailedListener {

    final static String className = GPGSImpl.class.getSimpleName();

    private static final int RC_SIGN_IN           = 9001;
    private static final int REQUEST_ACHIEVEMENTS = 9002;
    private static final int REQUEST_LEADERBOARD  = 9003;

    private final String[] ACHEIVEMENT = {
            "CgkI7Kb_-4YXEAIQAQ",
            "CgkI7Kb_-4YXEAIQAg",
            "CgkI7Kb_-4YXEAIQAw",
            "CgkI7Kb_-4YXEAIQBA",
            "CgkI7Kb_-4YXEAIQBQ",
            "CgkI7Kb_-4YXEAIQBg",
            "CgkI7Kb_-4YXEAIQBw",
            "CgkI7Kb_-4YXEAIQCA",
            "CgkI7Kb_-4YXEAIQCQ",
            "CgkI7Kb_-4YXEAIQCg",
            "CgkI7Kb_-4YXEAIQCw",
            "CgkI7Kb_-4YXEAIQDA",
            "CgkI7Kb_-4YXEAIQDQ",
            "CgkI7Kb_-4YXEAIQDg",
            "CgkI7Kb_-4YXEAIQDw",
            "CgkI7Kb_-4YXEAIQEA",
            "CgkI7Kb_-4YXEAIQEQ",
            "CgkI7Kb_-4YXEAIQEg",
            "CgkI7Kb_-4YXEAIQEw",
    };
    private final String   LEADERBOARD = ">>> replace me <<<";
    public    GoogleApiClient client;
    protected AndroidLauncher context;

    public void init( AndroidLauncher context ) {
        this.context = context;

        Games.GamesOptions gamesOptions;
        gamesOptions = Games.GamesOptions.builder().setRequireGooglePlus( true ).build();

        // http://android-developers.blogspot.ru/2016/01/play-games-permissions-are-changing-in.html
        // В обновлениях написано, что нужно использовать ТОЛЬКО те АПИ, которые действительно
        // нужны.
        // .addApi( Plus.API ).addScope( Plus.SCOPE_PLUS_LOGIN ) - не нужно
        // .addScope( Games.SCOPE_GAMES ) - тоже не нужно
        client = new GoogleApiClient.Builder( context )
                .addApi( Games.API, gamesOptions )
                .build();
    }

    @Override
    public void connect() {
        // эта строчка проверяла правильность идентификатора приложения
        //BaseGameUtils.verifySampleSetup( context, R.string.app_id );
        client.registerConnectionCallbacks( this );
        client.registerConnectionFailedListener( this );
        client.connect();

        Log.d( className, "Client: log in" );
    }

    @Override
    public void disconnect() {
        client.unregisterConnectionCallbacks( this );
        client.unregisterConnectionFailedListener( this );
        client.disconnect();

        Log.d( className, "Client: log out" );
    }

    @Override
    public void signOut() {
        Games.signOut( client );

        Log.d( className, "Client: sign out" );
    }

    @Override
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public void onActivityResult( int request, int response, Intent intent ) {
        Log.d( className, "Activity requestCode: " + request );

        if ( request == RC_SIGN_IN ) {
            Log.d( className, "RC_SIGN_IN, responseCode=" + response + ", intent=" + intent );
            if ( response == context.RESULT_OK ) {
                client.connect();
            }
            else {
                int error = R.string.connect_error;
                BaseGameUtils.showActivityResultError( context, request, response, error );
            }
        }
    }

    public void onStop() {
        if ( isConnected() ) {
            disconnect();
        }
    }

    public void onStart() {
        if ( !isConnected() ) {
            connect();
        }
    }

    @Override
    public void unlockAchievement( int n ) {
        Games.Achievements.unlock( client, ACHEIVEMENT[ n ] );
    }

    @Override
    public void unlockIncrementAchievement( int n, int count ) {
        Games.Achievements.increment( client, ACHEIVEMENT[ n ], count );
    }

    @Override
    public void showAchievements() {
        Intent intent = Games.Achievements.getAchievementsIntent( client );
        context.startActivityForResult( intent, REQUEST_ACHIEVEMENTS );
    }

    @Override
    public void submitScore( long score ) {
        Games.Leaderboards.submitScore( client, LEADERBOARD, score );
    }

    @Override
    public void showLeaderboard() {
        Intent intent = Games.Leaderboards.getLeaderboardIntent( client, LEADERBOARD );
        context.startActivityForResult( intent, REQUEST_LEADERBOARD );
    }

    @Override
    public void onConnected( Bundle connectionHint ) {
        Log.d( className, "Client: Success connected xD" );
    }

    @Override
    public void onConnectionSuspended( int i ) {
        client.connect();
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult ) {
        Log.d( className, "Connection Failed, result: " + connectionResult );

        String error = connectionResult.getErrorMessage();
        BaseGameUtils.resolveConnectionFailure( context, client, connectionResult,
                                                RC_SIGN_IN, error );
    }
}
