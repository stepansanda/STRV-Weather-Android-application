package cz.stepansanda.weather.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by a111 on 24.05.15.
 */
public class WeatherApplication extends Application {

    private static WeatherApplication sInstance;


    public WeatherApplication() {
        sInstance = this;
    }


    public static Context getContext() {
        return sInstance;
    }

}
