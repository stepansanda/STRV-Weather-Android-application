package cz.stepansanda.weather.android.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.fragment.SettingsFragment;

/**
 * Created by a111 on 31.05.15.
 */
public class SettingsHelper implements OnSharedPreferenceChangeListener {

    public enum ImageToLoad {
        STATIC, DYNAMIC_WEATHER, DYNAMIC_WEATHER_LOCATION, DYNAMIC_LOCATION;
    }

    private static SettingsHelper sInstance;

    private Context mContext;
    private String mDistanceUnit;
    private String mTemperatureUnit;
    private String mImageToLoad;


    /**
     * Private constructor for singleton
     */
    private SettingsHelper(Context context) {
        mContext = context;
        loadSavedSettings();
        // register listener on preferences changed
        PreferenceManager.getDefaultSharedPreferences(mContext).registerOnSharedPreferenceChangeListener(this);
    }


    /**
     * Singleton getter
     *
     * @param context application context
     * @return singleton instance of settings helper
     */
    public static SettingsHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SettingsHelper(context);
        }
        return sInstance;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadSavedSettings();
    }


    /**
     * Get temperature in Celsius and convert it to the correct format
     *
     * @param temperature temperature in celsius
     * @return formatted temperature in correct format to show
     */
    public String getTemperatureString(int temperature) {
        if (mTemperatureUnit.equalsIgnoreCase("Fahrenheit")) {
            return Math.round(UnitConverterHelper.convertCelsiusToFahrenheit(temperature)) + mContext.getString(R.string.degree_unit);
        } else {
            return temperature + mContext.getString(R.string.degree_unit);
        }
    }


    /**
     * Get wind speed in km/h and convert it to the correc format
     *
     * @param windSpeed in km/h
     * @return correct format string
     */
    public String getWindSpeedString(int windSpeed) {
        if (mDistanceUnit.equalsIgnoreCase("Kilometer")) {
            return windSpeed + " " + mContext.getString(R.string.wind_km_h);
        } else {
            return Math.round(UnitConverterHelper.convertKilometersInHourToMetersInSecond(windSpeed)) + " " + mContext.getString(R.string.wind_m_s);
        }
    }


    /**
     * Get type of image to load
     *
     * @return type of image to load
     */
    public ImageToLoad getImageToLoad() {
        loadSavedSettings();
        if (mImageToLoad.equalsIgnoreCase("Static (saves data transfer)")) {
            return ImageToLoad.STATIC;
        } else if (mImageToLoad.equalsIgnoreCase("Dynamic by weather")) {
            return ImageToLoad.DYNAMIC_WEATHER;
        } else if (mImageToLoad.equalsIgnoreCase("Dynamic by weather and location")) {
            return ImageToLoad.DYNAMIC_WEATHER_LOCATION;
        } else {
            return ImageToLoad.DYNAMIC_LOCATION;
        }
    }


    /**
     * Load saved settings from shared preferences
     */
    private void loadSavedSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mDistanceUnit = sharedPref.getString(SettingsFragment.KEY_PREF_DISTANCE_UNIT, mContext.getString(R.string.pref_length_unit_default_value));
        mTemperatureUnit = sharedPref.getString(SettingsFragment.KEY_PREF_TEMPERATURE_UNIT, mContext.getString(R.string.pref_temperature_unit_default_value));
        mImageToLoad = sharedPref.getString(SettingsFragment.KEY_PREF_IMAGE_TO_LOAD, mContext.getString(R.string.pref_image_to_load_default_value));
    }
}
