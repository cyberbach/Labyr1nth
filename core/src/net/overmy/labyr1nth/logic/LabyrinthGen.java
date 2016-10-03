package net.overmy.labyr1nth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Andrey (cb) Mikheev
 * Labyrinth3DGame
 * 16.02.2016
 */
public class LabyrinthGen {
    
    final private static String className = LabyrinthGen.class.getSimpleName();

    private static LabyrinthGen ourInstance = new LabyrinthGen();

    private LabyrinthGen() {}

    public static LabyrinthGen getInstance() {
        return ourInstance;
    }

    private static boolean inBounds( int a, int b, int c, int d ) {
        return a + 1 < c && b + 1 < d && a - 1 > 0 && b - 1 > 0;
    }

    private static float randomAfterPercent( final float percent, final float r ) {
        return r * percent + MathUtils.random.nextFloat() * r * (1 - percent);
    }

    public static boolean[][] gen( int width, int height, int density ) {
        //Gdx.app.debug( className, "begin" );

        boolean[][] grid = new boolean[ width ][ height ];

        for ( int j = 0; j < height; j++ )
            for ( int i = 0; i < width; i++ ) {
                grid[ i ][ j ] = false;
            }

        final int max_iterations = (int) (randomAfterPercent( 0.5f, width * height ));

        final int x = (int) (width * 0.25f + MathUtils.random.nextInt( (int) (width * 0.5f) ));
        final int y = (int) (height * 0.25f + MathUtils.random.nextInt( (int) (height * 0.5f) ));

        grid[ x ][ y ] = true;

        ArrayList< GridPoint2 > points = new ArrayList< GridPoint2 >();

        for ( int d = 0; d < density; d++ ) {

            //Gdx.app.debug( className, "density " + d );

            for ( int j = 0; j < height; j++ )
                for ( int i = 0; i < width; i++ ) {
                    if ( grid[ i ][ j ] ) points.add( new GridPoint2( i, j ) );
                }

            //Gdx.app.debug( className, "Grid filled " + points.size() );

            final int generatedStartPoint = MathUtils.random.nextInt( points.size() );
            int startX = points.get( generatedStartPoint ).x;
            int startY = points.get( generatedStartPoint ).y;

            for ( int step = 0; step < max_iterations; step++ ) {
                int workX = startX;
                int workY = startY;

                final int curveLength = (int) (randomAfterPercent( 0.3f, width * height * 0.1f ));

                for ( int curve = 0; curve < curveLength; curve++ ) {
                    int resetX = workX;
                    int resetY = workY;
                    switch ( MathUtils.random.nextInt( 4 ) ) {
                        case 1:
                            workX++;
                            if ( inBounds( workX, workY, width, height ) )
                                if ( (!grid[ workX ][ workY + 1 ] && !grid[ workX ][ workY - 1 ])
                                     && grid[ workX - 1 ][ workY ] )
                                    grid[ workX ][ workY ] = true;
                            break;
                        case 2:
                            workX--;
                            if ( inBounds( workX, workY, width, height ) )
                                if ( (!grid[ workX ][ workY + 1 ] && !grid[ workX ][ workY - 1 ])
                                     && grid[ workX + 1 ][ workY ] )
                                    grid[ workX ][ workY ] = true;
                            break;
                        case 3:
                            workY++;
                            if ( inBounds( workX, workY, width, height ) )
                                if ( (!grid[ workX + 1 ][ workY ] && !grid[ workX - 1 ][ workY ])
                                     && grid[ workX ][ workY - 1 ] )
                                    grid[ workX ][ workY ] = true;
                            break;
                        default:
                            workY--;
                            if ( inBounds( workX, workY, width, height ) )
                                if ( (!grid[ workX + 1 ][ workY ] && !grid[ workX - 1 ][ workY ])
                                     && grid[ workX ][ workY + 1 ] )
                                    grid[ workX ][ workY ] = true;
                            break;
                    }
                    if ( inBounds( workX, workY, width, height ) && !grid[ workX ][ workY ] ) {
                        workX = resetX;
                        workY = resetY;
                        continue;
                    } else if ( MathUtils.random.nextInt( 10 ) == 0 ) {
                        // change start cell
                        startX = workX;
                        startY = workY;
                    }
                }
                points.clear();
            }
        }

        //Gdx.app.debug( className, "end" );

        return grid;
    }
}
