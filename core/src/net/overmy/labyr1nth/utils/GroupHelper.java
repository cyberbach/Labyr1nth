package net.overmy.labyr1nth.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import net.overmy.labyr1nth.Core;


/**
 * Created by Andrey (cb) Mikheev
 * Labyr1nth
 * 04.10.2016
 */

public final class GroupHelper {

    private static GroupHelper ourInstance = new GroupHelper();

    private GroupHelper() { }

    /**
     * Создаем ГРУППУ и добавляем к ней АКТЁРА
     */
    public static Group create( final Actor actor ) {
        Group returnGroup = new Group();
        returnGroup.addActor( actor );
        return returnGroup;
    }

    /**
     * Группа появляется из 0 в 1 за время (FADE * 2)
     */
    public static void scaleIn( Actor actor ) {
        float time = MathUtils.random( Core.FADE * 2 * 0.6f, Core.FADE * 2 );
        actor.addAction( Actions.sequence( Actions.scaleTo( 0, 0, 0 ),
                                           Actions.scaleTo( 1, 1, time ) ) );
    }

    /**
     * Группа исчезает из 1 в 0 за время (FADE * 2)
     */
    public static void scaleOut( Actor actor ) {
        float time = MathUtils.random( Core.FADE * 2 * 0.6f, Core.FADE * 2 );
        actor.addAction( Actions.sequence( Actions.scaleTo( 1, 1, 0 ),
                                           Actions.scaleTo( 0, 0, time ) ) );
    }
}
