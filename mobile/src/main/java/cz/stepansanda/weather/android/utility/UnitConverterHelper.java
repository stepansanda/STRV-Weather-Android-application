package cz.stepansanda.weather.android.utility;

import android.content.Context;

import java.util.Date;

import cz.stepansanda.weather.android.R;

/**
 * Created by a111 on 24.05.15.
 */
public class UnitConverterHelper {

    public static final long UNIX_CONSTANT = 1000l;


    /**
     * Convert Unix seconds to Date
     *
     * @param unixTime time
     * @return date from time
     */
    public static Date getDateFromUnixTime(long unixTime) {
        return new Date(unixTime * UNIX_CONSTANT);
    }


    /**
     * Get wind direction formatted string
     *
     * @param context       application context
     * @param windDirection wind direction in degrees
     * @return formatted string wind direcion
     */
    public static String getWindDirectionString(Context context, double windDirection) {
        String[] directions = context.getResources().getStringArray(R.array.wind_directions);
        return directions[(int) Math.round(((windDirection % 360) / 45)) % 8];
    }


    /**
     * Converts km/h to m/s
     *
     * @param kilometerInHour speed in km/h
     * @return speed in m/s
     */
    public static float convertKilometersInHourToMetersInSecond(float kilometerInHour) {
        return kilometerInHour / 3.6f;
    }


    /**
     * Convert Celsius to Fahrenheit
     *
     * @param temperature in Celsius
     * @return temperature in Fahrenheit
     */
    public static float convertCelsiusToFahrenheit(float temperature) {
        return temperature * (9f / 5f) + 32;
    }


    /**
     * Rounds the number to specified decimal places
     *
     * @param number number to round
     * @param places decimal places to round
     * @return rounded number
     */
    public static double roundTwoDecimals(double number, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        number = number * factor;
        long tmp = Math.round(number);
        return (double) tmp / factor;
    }
}
