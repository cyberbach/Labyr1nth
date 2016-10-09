package net.overmy.labyr1nth.utils;

/**
 * Created by Andrey (cb) Mikheev
 * Grow248
 * 10.08.2016
 */
public class TIMER {

    public static  float current;
    public static  int   totalSec;
    public static  int   totalMin;
    public static  int   totalHR;
    public static  int   totalDays;
    public static  int   maxSec;
    public static  int   maxMin;
    public static  int   maxHR;
    public static  int   maxDays;
    private static int   sec;
    private static int   min;
    private static int   hr;
    private static int   d;
    private static String timeString = "";

    private static TIMER ourInstance = new TIMER();

    private TIMER() { }

    public static void reset() {
        sec = 0;
        min = 0;
        hr = 0;
        d = 0;
        current = 1.0f;
    }

    private static void save() {
        totalSec++;
        if ( totalSec > 59 ) {
            totalSec = 0;
            totalMin++;
        }
        if ( totalMin > 59 ) {
            totalMin = 0;
            totalHR++;
        }
        if ( totalHR > 23 ) {
            totalHR = 0;
            totalDays++;
        }
    }

    private static void saveMax() {
        if ( sec + min * 60 + hr * 60 * 60 + d * 24 * 60 * 60 >
             maxSec + maxMin * 60 + maxHR * 60 * 60 + maxDays * 24 * 60 * 60 ) {
            maxDays = d;
            maxHR = hr;
            maxMin = min;
            maxSec = sec;
        }
    }

    public static void tick( float delta ) {
        current -= delta;
        if ( current < 0 ) {
            current = 1.0f;
            sec++;
            if ( sec > 59 ) {
                sec = 0;
                min++;
            }
            if ( min > 59 ) {
                min = 0;
                hr++;
            }
            if ( hr > 23 ) {
                hr = 0;
                d++;
            }
            save();
            saveMax();
        }
    }

    public static long toLong() {
        return d * 1000000 + hr * 10000 + min * 100 + sec;
    }

    public static boolean isWait() {
        return current == 1.0f;
    }

    public static String get() {
        return getTime( d, hr, min, sec );
    }

    public static String getRusTime() {
        timeString = "";
        if ( d > 0 ) {
            timeString += " " + d;

            int tmp = d;
            while ( tmp > 10 ) { tmp -= 10; }

            if ( tmp == 1 ) { timeString += " день "; }
            else if ( tmp == 2 || tmp == 3 || tmp == 4 ) { timeString += " дня "; }
            else { timeString += " дней "; }
        }
        if ( hr > 0 ) {
            timeString += " " + hr;

            int tmp = hr;
            while ( tmp > 10 ) { tmp -= 10; }

            if ( tmp == 1 ) { timeString += " час "; }
            else if ( tmp == 2 || tmp == 3 || tmp == 4 ) { timeString += " часа "; }
            else { timeString += " часов "; }
        }
        if ( min > 0 ) {
            if ( min < 10 ) { timeString += " 0" + min; }
            else { timeString += " " + min; }

            int tmp = min;
            while ( tmp > 10 ) { tmp -= 10; }

            if ( tmp == 1 ) { timeString += " минуту и "; }
            else if ( tmp == 2 || tmp == 3 || tmp == 4 ) { timeString += " минуты и "; }
            else { timeString += " минут и "; }
        }
        if ( sec < 10 ) { timeString += " 0" + sec; }
        else { timeString += " " + sec; }

        int tmp = min;
        while ( tmp > 10 ) { tmp -= 10; }

        if ( tmp == 1 ) { timeString += " секунду"; }
        else if ( tmp == 2 || tmp == 3 || tmp == 4 ) { timeString += " секунды"; }
        else { timeString += " секунд"; }

        return timeString;
    }

    public static String getTotal() {
        return getTime( totalDays, totalHR, totalMin, totalSec );
    }

    public static String getMax() {
        return getTime( maxDays, maxHR, maxMin, maxSec );
    }

    private static String getTime( int q, int w, int e, int r ) {
        timeString = "";
        if ( q > 0 ) { timeString += " " + q + ","; }
        if ( w > 0 ) { timeString += " " + w + " :"; }
        if ( e > 0 ) {
            if ( e < 10 ) { timeString += " 0" + e + " :"; }
            else { timeString += " " + e + " :"; }
        }
        if ( r < 10 ) { timeString += " 0" + r; }
        else { timeString += " " + r; }
        return timeString;
    }
}
