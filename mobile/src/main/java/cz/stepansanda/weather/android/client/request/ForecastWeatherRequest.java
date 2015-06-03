package cz.stepansanda.weather.android.client.request;

import android.content.Context;
import android.location.Location;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import cz.stepansanda.weather.android.WeatherConfig;
import cz.stepansanda.weather.android.client.VolleySingleton;
import cz.stepansanda.weather.android.client.response.ForecastErrorResponse;
import cz.stepansanda.weather.android.client.response.ForecastResponse;

/**
 * Created by a111 on 24.05.15.
 */
public class ForecastWeatherRequest {

    /**
     * Listener for callback when async loading finished
     */
    public interface OnForecastLoadedListener {
        void forecastWeatherLoaded();
    }

    private static final String TAG = ForecastWeatherRequest.class.getName();

    private Context mContext;
    private Location mLocation;
    private OnForecastLoadedListener mListener;
    private boolean mIsCurrentLocation;
    private String mLocationName;


    public ForecastWeatherRequest(Context context, Location location, boolean isCurrentLocation,
                                  String locationName, OnForecastLoadedListener listener) {
        mContext = context;
        mLocation = location;
        mListener = listener;
        mIsCurrentLocation = isCurrentLocation;
        mLocationName = locationName;
    }


    public void loadForecast() {
        // create new request
        JsonObjectRequest currentWeatherRequest = new JsonObjectRequest(
                Request.Method.GET,
                getUri().toString(),
                new ForecastResponse(mContext, mListener, mLocationName, mIsCurrentLocation),
                new ForecastErrorResponse(mContext));
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
                .appendPath("forecast")
                .appendPath("daily")
                .appendQueryParameter("lat", mLocation.getLatitude() + "")
                .appendQueryParameter("lon", mLocation.getLongitude() + "")
                .appendQueryParameter("cnt", 16 + "")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("units", "metric")
                .appendQueryParameter("key", WeatherConfig.WEATHER_API_KEY)
                .build();
    }


}
