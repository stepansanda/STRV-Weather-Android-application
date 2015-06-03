package cz.stepansanda.weather.android.geolocation;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by a111 on 24.05.15.
 */
public class GeolocationHelper implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private static final String TAG = GeolocationHelper.class.getName();
    // radius of the earth
    private static final long EARTH_RADIUS = 6371000l;
    private static final long DISTANCE_IN_METERS_BETWEEN_NEW_LOCATION = 1000l;
    private static final int REFRESH_LOCATION_MINUTES = 15;
    private static GeolocationHelper sInstance;

    private LocationRequest mLocationRequest;
    private GoogleApiClient googleApiClient;
    private Context mContext;
    private Location mLocation;
    private boolean isTracking;
    private Date mDate;
    private List<OnLocationChangedListener> mListeners;


    /**
     * Private constructor for singleton
     *
     * @param context application context
     */
    private GeolocationHelper(Context context) {
        mContext = context;
        mListeners = new LinkedList<>();
        mLocation = getLastKnownLocation(context);
        googleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        startTracking();
    }


    /**
     * Singleton getter
     *
     * @param context application context
     * @return singleton instance
     */
    public static GeolocationHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GeolocationHelper(context);
        }
        return sInstance;
    }


    /**
     * Check if GPS sensor is enabled
     *
     * @param context application context
     * @return result if GPS sensor is enabled
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    /**
     * Count distance between two locations in meters
     *
     * @param location1 first location
     * @param location2 second location
     * @return distance between locations in meters
     */
    public static float getDistanceBetweenLocations(Location location1, Location location2) {
        double fromLatitude, toLatitude, fromLongitude, toLongitude;
        fromLatitude = location1.getLatitude();
        toLatitude = location2.getLatitude();
        fromLongitude = location1.getLongitude();
        toLongitude = location2.getLongitude();

        if (fromLatitude > toLatitude) {
            double helper = fromLatitude;
            fromLatitude = toLatitude;
            toLatitude = helper;
        }
        if (fromLongitude > toLongitude) {
            double helper = fromLongitude;
            fromLongitude = toLongitude;
            toLongitude = helper;
        }

        // meters
        double dLat = Math.toRadians(toLatitude - fromLatitude);
        double dLng = Math.toRadians(toLongitude - fromLongitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(fromLatitude))
                * Math.cos(Math.toRadians(toLatitude))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (float) (EARTH_RADIUS * c);
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        endTracking();
        Location oldLocation = mLocation;
        mLocation = location;
        if (oldLocation == null
                || getDistanceBetweenLocations(oldLocation, mLocation) > DISTANCE_IN_METERS_BETWEEN_NEW_LOCATION) {
            callListeners();
        }
    }


    /**
     * Add new on location changed listener
     *
     * @param listener on location changed listener
     */
    public void addLocationChangedListener(OnLocationChangedListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }


    /**
     * Unregister on location changed listener
     *
     * @param listener on location changed listener
     */
    public void unregisterOnLocationChangedListener(OnLocationChangedListener listener) {
        mListeners.remove(listener);
    }


    /**
     * Get current location
     *
     * @return current location
     */
    public Location getLocation() {
        return mLocation;
    }


    /**
     * Start GPS tracking to find current location
     */
    public void startTracking() {
        // if is not already tracking
        if (!isTracking) {
            // refresh only if last known location is older than constant
            if (mDate == null || getDifferenceBetweenDatesInMinutes(mDate, new Date()) > REFRESH_LOCATION_MINUTES) {
                isTracking = true;
                googleApiClient.connect();
            }
        }
    }


    /**
     * End GPS tracking
     */
    private void endTracking() {
        try {
            googleApiClient.unregisterConnectionCallbacks(this);
            googleApiClient.unregisterConnectionFailedListener(this);
            googleApiClient.disconnect();
            mDate = new Date();
            isTracking = false;
        } catch (Exception e) {
            Log.e(TAG, "endTracking()" + e.toString());
        }
    }


    /**
     * Call all listeners when location changed
     */
    private void callListeners() {
        for (OnLocationChangedListener l : mListeners) {
            if (l != null) {
                l.onLocationChanged(mLocation);
            }
        }
    }


    /**
     * At start set current location on last known location
     *
     * @param context application context
     * @return last known location position
     */
    private Location getLastKnownLocation(Context context) {
        // Get the location manager
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    /**
     * Get minutes between two dates
     *
     * @param date1 first date
     * @param date2 second date
     * @return difference in minutes
     */
    private long getDifferenceBetweenDatesInMinutes(Date date1, Date date2) {
        long differenceInMilliseconds = date2.getTime() - date1.getTime();
        return differenceInMilliseconds / 1000 / 60;
    }


    /**
     * Called when new location is found
     */
    public interface OnLocationChangedListener {
        void onLocationChanged(Location location);
    }
}
