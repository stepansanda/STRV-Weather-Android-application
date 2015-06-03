package cz.stepansanda.weather.android.client.request;

import android.content.Context;
import android.location.Location;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import cz.stepansanda.weather.android.WeatherConfig;
import cz.stepansanda.weather.android.client.VolleySingleton;
import cz.stepansanda.weather.android.client.response.CurrentWeatherErrorResponse;
import cz.stepansanda.weather.android.client.response.CurrentWeatherResponse;

/**
 * Created by a111 on 24.05.15.
 */
public class CurrentWeatherRequest {
    /**
     * Listener for callback when async loading finished
     */
    public interface OnCurrentWeatherLoadedListener {
        void currentWeatherLoaded();
    }

    private static final String TAG = CurrentWeatherRequest.class.getName();

    private Context mContext;
    private Location mLocation;
    private OnCurrentWeatherLoadedListener mListener;
    private boolean mIsCurrentLocation;
    private String mLocationName;


    public CurrentWeatherRequest(Context context, Location location, boolean isCurrentLocation,
                                 String locationName, OnCurrentWeatherLoadedListener listener) {
        mContext = context;
        mLocation = location;
        mListener = listener;
        mIsCurrentLocation = isCurrentLocation;
        mLocationName = locationName;
    }


    public void loadCurrentWeather() {
        // create new request
        JsonObjectRequest currentWeatherRequest = new JsonObjectRequest(
                Request.Method.GET,
                getUri().toString(),
                new CurrentWeatherResponse(mContext, mListener, mLocationName, mIsCurrentLocation),
                new CurrentWeatherErrorResponse(mContext));
        // add request to the global queue
        VolleySingleton.getInstance(mContext).addToRequestQueue(currentWeatherRequest, TAG);
    }


    /**
     * Prepare url address for current location
     *
     * @return Uri with current location
     */
    private Uri getUri() {
        return Uri.parse(WeatherConfig.WEATHER_URL).buildUpon()
                .appendPath("weather")
                .appendQueryParameter("lat", mLocation.getLatitude() + "")
                .appendQueryParameter("lon", mLocation.getLongitude() + "")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("key", WeatherConfig.WEATHER_API_KEY)
                .build();
    }
}
