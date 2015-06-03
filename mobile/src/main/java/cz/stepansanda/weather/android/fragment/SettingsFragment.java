package cz.stepansanda.weather.android.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import cz.stepansanda.weather.android.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_PREF_DISTANCE_UNIT = "pref_length_units";
    public static final String KEY_PREF_TEMPERATURE_UNIT = "pref_temperature_unit";
    public static final String KEY_PREF_IMAGE_TO_LOAD = "pref_image_to_load";


    public SettingsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}

