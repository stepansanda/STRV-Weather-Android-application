package cz.stepansanda.weather.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.dialog.AboutDialog;
import cz.stepansanda.weather.android.dialog.EnableGpsDialog;
import cz.stepansanda.weather.android.dialog.EnableGpsDialog.OnGpsDialogResultListener;
import cz.stepansanda.weather.android.fragment.CurrentWeatherPagerFragment;
import cz.stepansanda.weather.android.fragment.ForecastPagerFragment;
import cz.stepansanda.weather.android.fragment.LocationListFragment;
import cz.stepansanda.weather.android.fragment.NavigationDrawerFragment;
import cz.stepansanda.weather.android.fragment.NavigationDrawerFragment.NavigationDrawerCallbacks;
import cz.stepansanda.weather.android.geolocation.GeolocationHelper;
import cz.stepansanda.weather.android.utility.Logcat;
import cz.stepansanda.weather.android.utility.NetworkManager;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks, OnGpsDialogResultListener {

    public static final int TODAY_FRAGMENT_POSITION = 0;
    public static final int FORECAST_FRAGMENT_POSITION = 1;
    public static final int LOCATION_FRAGMENT_POSITION = 2;

    private static final String TAG = MainActivity.class.getName();
    private static final int ENABLE_GPS_REQUEST_CODE = 1;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GeolocationHelper mGeolocationHelper;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGeolocationHelper = GeolocationHelper.getInstance(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        if (!GeolocationHelper.isGpsEnabled(getApplicationContext())) {
            Logcat.e(TAG, "GPS disabled");
            showEnableGpsDialog();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGeolocationHelper.startTracking();
        if (!NetworkManager.isOnline(getApplicationContext())) {
            Toast.makeText(this, getString(R.string.toast_no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the menu_main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        switch (position) {
            case TODAY_FRAGMENT_POSITION:
                fragment = CurrentWeatherPagerFragment.newInstance();
                break;
            case FORECAST_FRAGMENT_POSITION:
                fragment = ForecastPagerFragment.newInstance();
                break;
            case LOCATION_FRAGMENT_POSITION:
                fragment = LocationListFragment.newInstance();
                break;
            default:
                fragment = CurrentWeatherPagerFragment.newInstance();
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_about:
                showAboutDialog();
                return true;
            case R.id.action_settings:
                startSettingsActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mNavigationDrawerFragment.getDrawerToggle().syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mNavigationDrawerFragment.getDrawerToggle().onConfigurationChanged(newConfig);
    }


    @Override
    public void onGpsDialogResult(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            startEnableGpsSettings();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ENABLE_GPS_REQUEST_CODE:
                // if gps enabled
                if (GeolocationHelper.isGpsEnabled(getApplicationContext())) {
                    // get new current location
                    mGeolocationHelper.startTracking();
                }
                break;
        }
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case TODAY_FRAGMENT_POSITION:
                mTitle = getString(R.string.fragment_today);
                break;
            case FORECAST_FRAGMENT_POSITION:
                mTitle = getString(R.string.fragment_forecast);
                break;
            case LOCATION_FRAGMENT_POSITION:
                mTitle = getString(R.string.fragment_locations);
                break;
        }
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    private void showAboutDialog() {
        new AboutDialog().show(getSupportFragmentManager(), TAG);
    }


    private void showEnableGpsDialog() {
        EnableGpsDialog gpsDialog = new EnableGpsDialog();
        gpsDialog.show(getSupportFragmentManager(), TAG);
    }


    /**
     * Go to settings to enable GPS sensor
     */
    private void startEnableGpsSettings() {
        // Launch settings, allowing user to make a change
        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(enableGpsIntent, ENABLE_GPS_REQUEST_CODE);
    }


    /**
     * Start settings activity
     */
    private void startSettingsActivity() {
        Intent settingsIntent = SettingsActivity.newIntent(this);
        startActivity(settingsIntent);
    }
}
