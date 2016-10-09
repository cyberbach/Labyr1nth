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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.overmy.labyr1nth.Core;
import net.overmy.labyr1nth.MyGdxGame;
import net.overmy.labyr1nth.MyGdxGame.SCREEN;
import net.overmy.labyr1nth.logic.LabyrinthGen;
import net.overmy.labyr1nth.logic.MyLevel;
import net.overmy.labyr1nth.neatresources.Fonts;
import net.overmy.labyr1nth.neatresources.GameColor;
import net.overmy.labyr1nth.neatresources.IMG;
import net.overmy.labyr1nth.neatresources.MusicTrack;
import net.overmy.labyr1nth.neatresources.Settings;
import net.overmy.labyr1nth.neatresources.SoundTrack;
import net.overmy.labyr1nth.neatresources.Text;
import net.overmy.labyr1nth.utils.AtmoManager;
import net.overmy.labyr1nth.utils.DoubleFloatAnimator;
import net.overmy.labyr1nth.utils.GroupHelper;
import net.overmy.labyr1nth.utils.LabelHelper;


public class MenuScreen extends Base2DScreen {

    private final int         labWidth             = Core.WIDTH / 3;
    private final int         labHeight            = Core.HEIGHT / 3;
    private final int         fullMaxGenerateTimes = 300;
    private       float       maxTime              = 0.6f;
    private       float       time                 = maxTime;
    private       int         generates            = 0;
    private       int         maxGenerateTimes     = 0;
    private       Stage       stage                = null;
    private       Group       titlegroup           = null;
    private       Group       startgroup           = null;
    private       Group       continuegroup        = null;
    private       Group       soundsgroup          = null;
    private       Group       connectToGPGSgroup   = null;
    private       Texture     labyrinthTexture     = null;
    private       boolean[][] labyrinth            = null;
    private       boolean[][] previousLabyrinth    = null;
    private       Pixmap      pixmap               = null;
    private       Sprite      gradientSprite       = null;

    private Image soundsON  = null;
    private Image soundsOFF = null;
    private Image acheivesImage     = null;
    private Image leaderboardsImage = null;
    private Image controllerON      = null;
    private Image controllerOFF     = null;

    private DoubleFloatAnimator dfa = null;

    public MenuScreen( final MyGdxGame game ) {
        super( game );
    }

    @Override
    public void show() {

        game.ad.show();
        if ( Settings.NotFirstRun.getBoolean() ) {
            game.gpgs.unlockAchievement( 0 );
        }

        stage = new Stage();

        // Создаем ЗАГОЛОВОК игры
        Label titleLabel = LabelHelper.create( Text.TITLE, Fonts.TITLE );
        //titleLabel.setDebug( true );
        titlegroup = GroupHelper.create( titleLabel );
        titlegroup.setPosition( Core.WIDTH_HALF, Core.HEIGHT * 0.75f );
        stage.addActor( titlegroup );

        // Создаем кнопку НАЧАТЬ НОВУЮ ИГРУ
        Label startLabel = LabelHelper.create( Text.START_GAME, Fonts.TITLE_BUTTONS );
        //startLabel.setDebug( true );
        startgroup = GroupHelper.create( startLabel );
        startgroup.setPosition( Core.WIDTH_HALF, Core.HEIGHT * 0.35f );
        startgroup.addListener( new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y ) {
                MyLevel.set( 0 );
                SoundTrack.CLICK.play();
                scaleGroupsOut();
                transitionTo( SCREEN.INTRO );
            }
        } );
        if ( MyLevel.getCurrent() > 0 ) {
            startgroup.setPosition( Core.WIDTH * 0.3f, Core.HEIGHT * 0.35f );
        }
        stage.addActor( startgroup );

        // Создаем кнопку ПРОДОЛЖИТЬ ИГРУ
        if ( MyLevel.getCurrent() > 0 ) {
            Label continueLabel = LabelHelper.create( Text.CONTINUE_GAME, Fonts.TITLE_BUTTONS );
            //continueLabel.setDebug( true );
            continuegroup = GroupHelper.create( continueLabel );
            continuegroup.setPosition( Core.WIDTH * 0.7f, Core.HEIGHT * 0.35f );
            continuegroup.addListener( new ClickListener() {
                @Override
                public void clicked( InputEvent event, float x, float y ) {
                    scaleGroupsOut();
                    transitionTo( SCREEN.INTRO );
                    SoundTrack.CLICK.play();
                }
            } );
            stage.addActor( continuegroup );
        }

        float size = Core.HEIGHT * 0.1f;

        // Кнопка АЧИВОК
        acheivesImage = IMG.ACHEIVE_ICON.getImageActor( size, size );
        acheivesImage.setPosition( size * 2.55f - size / 2, Core.HEIGHT );
        acheivesImage.addListener( new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y ) {
                clickAnimation( acheivesImage );
                game.gpgs.showAchievements();
                SoundTrack.CLICK.play();
            }
        } );
        stage.addActor( acheivesImage );

        // Кнопка Таблицы РЕКОРДОВ
        leaderboardsImage = IMG.LEADERBOARD_ICON.getImageActor( size, size );
        leaderboardsImage.setPosition( size * 4.05f - size / 2, Core.HEIGHT );
        leaderboardsImage.addListener( new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y ) {
                clickAnimation( leaderboardsImage );
                game.gpgs.showLeaderboard();
                SoundTrack.CLICK.play();
            }
        } );
        stage.addActor( leaderboardsImage );

        // Кнопка GPGS
        controllerON = IMG.CONTROLLER.getImageActor( size, size );
        controllerOFF = IMG.CONTROLLER_GRAY.getImageActor( size, size );
        connectToGPGSgroup = new Group();
        connectToGPGSgroup.setPosition( size * 1.05f, Core.HEIGHT - size * 1.05f );
        stage.addActor( connectToGPGSgroup );
        updateConnectToGPGSGroup();

        // Кнопка МУЗЫКА и ЗВУК
        soundsON = IMG.SOUND_ON.getImageActor( size, size );
        soundsOFF = IMG.SOUND_OFF.getImageActor( size, size );
        soundsgroup = new Group();
        soundsgroup.setPosition( Core.WIDTH - size * 1.05f, Core.HEIGHT - size * 1.05f );
        stage.addActor( soundsgroup );
        updateSoundsGroup();

        // Анимация повяления всего GUI
        scaleGroupsIn();

        gradientSprite = IMG.GRADIENT.createSprite();

        // Эти переменные для фонового лабиринта
        // dfa - для плавных сдвигов в разные стороны
        dfa = new DoubleFloatAnimator( 0.0f, 0.0f, 0.0f, 0.0f, Core.FADE * 16 );

        // На этой пиксельной карте я рисую лабиринт, а потом конвертиную его в текстуру
        pixmap = new Pixmap( labWidth, labHeight, Pixmap.Format.RGBA4444 );
        // Сдесь начальная генерация (и если нужно удаление предыдущей копии лабиринта)
        createLabyrinth();

        // Количество этапов генерации от 30 до 330
        // Если times больше этого числа, то текущий лабиринт удаляем и создаем новый
        int rndTimes = (int) Core.randomAfterPercent( 0.1f, 300 );
        maxGenerateTimes = fullMaxGenerateTimes / 10 + rndTimes;

        // К процессорам добавляем stage, чтобы не париться с ClickEvents
        InputProcessor keys      = new MyKeysProcessor();
        InputProcessor gestures  = new GestureDetector( this );
        InputProcessor processor = new InputMultiplexer( stage, keys, gestures );
        Gdx.input.setInputProcessor( processor );
        Gdx.input.setCatchBackKey( true );
        Gdx.input.setCatchMenuKey( true );
    }

    private void clickAnimation( Actor actor ) {
        actor.addAction( Actions.sequence(
                Actions.scaleTo( 0.5f, 0.5f, Core.FADE * 0.2f, Interpolation.circleIn ),
                Actions.scaleTo( 1.0f, 1.0f, Core.FADE * 0.8f, Interpolation.circleOut )
                                         ) );
    }

    private void updateConnectToGPGSGroup() {
        connectToGPGSgroup.clear();
        connectToGPGSgroup.addListener( new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y ) {
                if ( game.gpgs.isConnected() ) {
                    game.gpgs.signOut();
                    game.gpgs.disconnect();
                }
                else {
                    game.gpgs.connect();
                }
                GroupHelper.scaleOut( connectToGPGSgroup );
                updateConnectToGPGSGroup();
                SoundTrack.CLICK.play();
            }
        } );

        if ( game.gpgs.isConnected() ) {
            connectToGPGSgroup.addActor( controllerON );
            showGPGSstuff();
        }
        else {
            connectToGPGSgroup.addActor( controllerOFF );
            hideGPGSstuff();
        }

        GroupHelper.scaleIn( connectToGPGSgroup );
    }

    private void showGPGSstuff() {
        acheivesImage.clearActions();
        leaderboardsImage.clearActions();
        float pos = Core.HEIGHT * 0.1f;
        float x1  = pos * 2.55f - pos / 2;
        float x2  = pos * 4.05f - pos / 2;
        float y   = Core.HEIGHT - pos * 1.05f - pos / 2;
        float time;
        time = Core.randomAfterPercent( 0.4f, Core.FADE );
        acheivesImage.addAction( Actions.moveTo( x1, y, time, Interpolation.circleOut ) );
        time = Core.randomAfterPercent( 0.4f, Core.FADE );
        leaderboardsImage.addAction( Actions.moveTo( x2, y, time, Interpolation.circleOut ) );
    }

    private void hideGPGSstuff() {
        acheivesImage.clearActions();
        leaderboardsImage.clearActions();
        float pos = Core.HEIGHT * 0.1f;
        float x1  = pos * 2.55f - pos / 2;
        float x2  = pos * 4.05f - pos / 2;
        float y   = Core.HEIGHT;
        float time;
        time = Core.randomAfterPercent( 0.4f, Core.FADE );
        acheivesImage.addAction( Actions.moveTo( x1, y, time, Interpolation.circleIn ) );
        time = Core.randomAfterPercent( 0.4f, Core.FADE );
        leaderboardsImage.addAction( Actions.moveTo( x2, y, time, Interpolation.circleIn ) );
    }

    private void updateSoundsGroup() {
        soundsgroup.clear();
        // В рекурсии группы нужно снова добавлять Лиснер,
        // т.к. clear() - очищает вообще всё: Лиснеры, Экшены и удаляет Актёров
        soundsgroup.addListener( new ClickListener() {
            @Override
            public void clicked( InputEvent event, float x, float y ) {
                Core.sound = !Core.sound;
                Core.music = Core.sound;
                GroupHelper.scaleOut( soundsgroup );
                updateSoundsGroup();
                Gdx.app.debug( "Sounds and music", "" + Core.sound );
                SoundTrack.CLICK.play();
                MusicTrack.stopAll();
            }
        } );

        if ( Core.sound ) { soundsgroup.addActor( soundsON ); }
        else { soundsgroup.addActor( soundsOFF ); }

        GroupHelper.scaleIn( soundsgroup );
        MusicTrack.playRandomIfFinished();
    }

    private void scaleGroupsIn() {
        GroupHelper.scaleIn( titlegroup );
        GroupHelper.scaleIn( startgroup );
        if ( MyLevel.getCurrent() > 0 ) { GroupHelper.scaleIn( continuegroup ); }
    }

    private void scaleGroupsOut() {
        GroupHelper.scaleOut( titlegroup );
        GroupHelper.scaleOut( startgroup );
        if ( MyLevel.getCurrent() > 0 ) { GroupHelper.scaleOut( continuegroup ); }
        GroupHelper.scaleOut( soundsgroup );
        if ( game.gpgs.isConnected() ) { hideGPGSstuff(); }
    }

    @Override
    public void update( float delta ) {
        super.update( delta );

        time -= delta;
        if ( time < 0 ) {
            // Время догенерации лабиринта всегда разное: от 0.06 до 0.6 секунды
            time = Core.randomAfterPercent( 0.1f, maxTime );

            generates++;
            if ( generates > maxGenerateTimes ) {
                generates = 0;
                int rndTimes = (int) Core.randomAfterPercent( 0.1f, 300 );
                maxGenerateTimes = fullMaxGenerateTimes / 10 + rndTimes;
                createLabyrinth();
            }
            else {
                expandLabyrinth();
            }
        }

        AtmoManager.update( delta );
        stage.act( delta );

        if ( dfa.isNeedToUpdate() ) {
            dfa.update( delta );
        }
        else {
            float maxOffset = Core.HEIGHT / 16;
            float newX      = MathUtils.randomBoolean() ? -maxOffset : maxOffset;
            float newY      = MathUtils.randomBoolean() ? -maxOffset : maxOffset;
            dfa.fromCurrent().setTo( newX, newY ).resetTime();
        }

        if ( game.gpgsStateChange ) {
            updateConnectToGPGSGroup();
            game.gpgsStateChange = false;
        }

        MusicTrack.playRandomIfFinished();
    }

    // Это очень медленный метод, его нельзя вызывать постоянно
    private void createLabyrinth() {
        labyrinth = null;
        previousLabyrinth = null;
        float rndLength        = Core.randomAfterPercent( 0.1f, Core.WIDTH * 0.7f );
        int   maxLengthOfCurve = (int) (Core.WIDTH * 0.3f + rndLength);
        // Сама генерация очень медленна
        labyrinth = LabyrinthGen.gen( labWidth, labHeight, 1, maxLengthOfCurve );
        // Копия лабиринта, чтобы новый кусочек вывести другим цветом
        previousLabyrinth = new boolean[ labWidth ][ labHeight ];
        for ( int j = 0; j < labHeight; j++ )
            for ( int i = 0; i < labWidth; i++ ) {
                previousLabyrinth[ i ][ j ] = labyrinth[ i ][ j ];
            }

        // Затирание пиксельной карты тоже очень медленно
        pixmap.setColor( new Color( 1, 1, 1, 0 ) );
        pixmap.fill();

        int   nOfColor  = 1 + MathUtils.random( GameColor.values().length - 2 );
        Color dotsColor = GameColor.getByNumber( nOfColor );
        pixmap.setColor( dotsColor );
        for ( int j = 0; j < labHeight; j++ )
            for ( int i = 0; i < labWidth; i++ )
                if ( labyrinth[ i ][ j ] ) { pixmap.drawPixel( i, j ); }

        if ( labyrinthTexture != null ) { labyrinthTexture.dispose(); }
        labyrinthTexture = new Texture( pixmap );
    }

    private void expandLabyrinth() {
        // Копия лабиринта, чтобы новый кусочек вывести другим цветом
        for ( int j = 0; j < labHeight; j++ )
            for ( int i = 0; i < labWidth; i++ ) {
                previousLabyrinth[ i ][ j ] = labyrinth[ i ][ j ];
            }

        float rndLength        = Core.randomAfterPercent( 0.1f, Core.WIDTH * 0.7f );
        int   maxLengthOfCurve = (int) (Core.WIDTH * 0.3f + rndLength);
        LabyrinthGen.add( labyrinth, 1, maxLengthOfCurve );

        int   nOfColor  = 1 + MathUtils.random( GameColor.values().length - 2 );
        Color dotsColor = GameColor.getByNumber( nOfColor );
        pixmap.setColor( dotsColor );
        for ( int j = 0; j < labHeight; j++ )
            for ( int i = 0; i < labWidth; i++ )
                if ( labyrinth[ i ][ j ] && (previousLabyrinth[ i ][ j ] != labyrinth[ i ][ j ]) ) {
                    pixmap.drawPixel( i, j );
                }

        labyrinthTexture.dispose();
        labyrinthTexture = new Texture( pixmap );
    }

    @Override
    public void draw() {

        batch.begin();
        // Фоновый лабиринт
        batch.draw( labyrinthTexture, dfa.currentX, dfa.currentY, Core.WIDTH, Core.HEIGHT );
        // Фоновые частицы
        AtmoManager.render( batch, Color.WHITE );
        float w     = Core.WIDTH;
        float h     = Core.HEIGHT;
        float hD3   = Core.HEIGHT / 3;
        float hD5   = Core.HEIGHT / 5;
        float wM012 = Core.WIDTH * 0.12f;
        float wM076 = Core.WIDTH * 0.76f;
        float hM01  = Core.HEIGHT * 0.1f;
        // Изменение цвета в 1,1,1,1 так как Атмосферные частицы изменяют цвет рендера
        batch.setColor( Color.WHITE );
        // Затемнение по краям экрана
        batch.draw( gradientSprite, 0, hD3 * 2 + 5, 0, 0, w, hD3, 1, 1, 0 );
        batch.draw( gradientSprite, w, hD3 - 5, 0, 0, w, hD3, 1, 1, 180 );
        batch.draw( gradientSprite, hD5 - 5, 0, 0, 0, w, hD5, 1, 1, 90 );
        batch.draw( gradientSprite, w - hD5 + 5, Core.HEIGHT, 0, 0, w, hD5, 1, 1, -90 );

        // Затемнение под кнопками Новая игра и Продолжить
        batch.draw( gradientSprite, wM012, hD5, 0, 0, wM076, hM01, 1, 1, 0 );
        batch.draw( gradientSprite, wM076 + wM012, h * 0.6f, 0, 0, wM076, hD3, 1, 1, 180 );
        batch.end();

        // Рисуем текст - Логотип и кнопки
        stage.draw();
    }

    @Override
    public void backButton() {
        if ( inTransition() ) { return; }
        scaleGroupsOut();
        // Ни в коем случае не нужно делать прямого переключения экрана game.switchTo()
        // За правильное переключение отвечает Base2DScreen
        transitionTo( SCREEN.EXIT );
    }

    @Override
    public void dispose() {
        super.dispose();
        titlegroup.clear();
        startgroup.clear();
        if ( MyLevel.getCurrent() > 0 ) { continuegroup.clear(); }
        labyrinthTexture.dispose();
        labyrinth = null;
        previousLabyrinth = null;
        pixmap.dispose();
        gradientSprite = null;
        dfa = null;
        soundsgroup.clear();
        soundsON.clear();
        soundsOFF.clear();
        stage.dispose();
    }
}
