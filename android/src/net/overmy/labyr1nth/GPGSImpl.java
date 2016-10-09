package net.overmy.labyr1nth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
            "CgkI7Kb_-4YXEAIQBQ",
            "CgkI7Kb_-4YXEAIQFQ",
            "CgkI7Kb_-4YXEAIQFg",
            "CgkI7Kb_-4YXEAIQFw",
            "CgkI7Kb_-4YXEAIQGA",
            "CgkI7Kb_-4YXEAIQGQ",
            "CgkI7Kb_-4YXEAIQGg",
            "CgkI7Kb_-4YXEAIQGw",
            "CgkI7Kb_-4YXEAIQHA",
            "CgkI7Kb_-4YXEAIQHQ",
            "CgkI7Kb_-4YXEAIQHg",
            "CgkI7Kb_-4YXEAIQHw",
            "CgkI7Kb_-4YXEAIQIA",
            "CgkI7Kb_-4YXEAIQIQ",
            "CgkI7Kb_-4YXEAIQIg",
            "CgkI7Kb_-4YXEAIQIw",
            "CgkI7Kb_-4YXEAIQJA",
            "CgkI7Kb_-4YXEAIQJQ",
            "CgkI7Kb_-4YXEAIQJg",
            "CgkI7Kb_-4YXEAIQJw",
            "CgkI7Kb_-4YXEAIQKA",
            "CgkI7Kb_-4YXEAIQKQ",
            "CgkI7Kb_-4YXEAIQKg",
            "CgkI7Kb_-4YXEAIQKw",
            "CgkI7Kb_-4YXEAIQLA",
            "CgkI7Kb_-4YXEAIQLQ",
            "CgkI7Kb_-4YXEAIQLg",
            "CgkI7Kb_-4YXEAIQLw",
            "CgkI7Kb_-4YXEAIQMA",
            "CgkI7Kb_-4YXEAIQMQ",
            "CgkI7Kb_-4YXEAIQMg",
            "CgkI7Kb_-4YXEAIQMw",
            "CgkI7Kb_-4YXEAIQNA",
            "CgkI7Kb_-4YXEAIQNQ",
            "CgkI7Kb_-4YXEAIQNg",
            "CgkI7Kb_-4YXEAIQNw",
            "CgkI7Kb_-4YXEAIQOA",
    };
    private final String   LEADERBOARD = "CgkI7Kb_-4YXEAIQFA";
    public    GoogleApiClient client;
    protected AndroidLauncher context;

    public void init( AndroidLauncher context ) {
        this.context = context;

        Games.GamesOptions gamesOptions;
        gamesOptions = Games.GamesOptions.builder().setRequireGooglePlus( true ).build();

        // http://android-developers.blogspot.ru/2016/01/play-games-permissions-are-changing-in.html
        // В обновлениях написано, что нужно использовать ТОЛЬКО те АПИ, которые действительно
        // нужны.
        client = new GoogleApiClient.Builder( context )
                .addApi( Games.API, gamesOptions )
                .build();
        // .addApi( Plus.API ).addScope( Plus.SCOPE_PLUS_LOGIN ) - не нужно
        // .addScope( Games.SCOPE_GAMES ) - тоже не нужно
    }

    @Override
    public void connect() {
        if ( isConnected() ) { return; }

        client.registerConnectionCallbacks( this );
        client.registerConnectionFailedListener( this );
        client.connect();

        Log.d( className, "Client: log in" );
    }

    @Override
    public void disconnect() {
        if ( !isConnected() ) { return; }

        client.unregisterConnectionCallbacks( this );
        client.unregisterConnectionFailedListener( this );
        client.disconnect();

        Log.d( className, "Client: log out" );
    }

    @Override
    public void signOut() {
        if ( !isConnected() ) { return; }

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

    @Override
    public void unlockAchievement( int n ) {
        if ( !isConnected() ) { return; }

        if ( n > ACHEIVEMENT.length ) { return; }
        else { Games.Achievements.unlock( client, ACHEIVEMENT[ n ] ); }
    }

    @Override
    public void unlockIncrementAchievement( int n, int count ) {
        if ( !isConnected() ) { return; }

        if ( n > ACHEIVEMENT.length ) { return; }
        else { Games.Achievements.increment( client, ACHEIVEMENT[ n ], count ); }
    }

    @Override
    public void showAchievements() {
        if ( !isConnected() ) { return; }

        Intent intent = Games.Achievements.getAchievementsIntent( client );
        context.startActivityForResult( intent, REQUEST_ACHIEVEMENTS );
    }

    @Override
    public void submitScore( long score ) {
        if ( !isConnected() ) { return; }

        Games.Leaderboards.submitScore( client, LEADERBOARD, score );
    }

    @Override
    public void showLeaderboard() {
        if ( !isConnected() ) { return; }

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
