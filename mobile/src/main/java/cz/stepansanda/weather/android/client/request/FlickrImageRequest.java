package cz.stepansanda.weather.android.client.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.text.Normalizer;

import cz.stepansanda.weather.android.WeatherConfig;
import cz.stepansanda.weather.android.client.VolleySingleton;
import cz.stepansanda.weather.android.client.response.FlickrImageErrorResponse;
import cz.stepansanda.weather.android.client.response.FlickrImageResponse;
import cz.stepansanda.weather.android.entity.FlickrImageEntity;
import cz.stepansanda.weather.android.utility.SettingsHelper.ImageToLoad;

/**
 * Created by a111 on 29.05.15.
 */
public class FlickrImageRequest {

    /**
     * Listener for callback when async loading finished
     */
    public interface OnLocationImageLoadedListener {
        void onFlickrImageLoaded(FlickrImageEntity flickrImageEntity);
    }

    private static final String TAG = FlickrImageRequest.class.getName();

    private Context mContext;
    private double mLongitude;
    private double mLatitude;
    private String mLocationName;
    private String mWeather;
    private ImageToLoad mImageToLoad;
    private OnLocationImageLoadedListener mListener;


    public FlickrImageRequest(Context context, double longitude, double latitude,
                              String locationName, String weather, ImageToLoad imageToLoad,
                              OnLocationImageLoadedListener listener) {
        mContext = context;
        mLongitude = longitude;
        mLatitude = latitude;
        mLocationName = locationName;
        mWeather = weather;
        mImageToLoad = imageToLoad;
        mListener = listener;
    }


    public void loadLocationImage() {
        // create new request
        JsonObjectRequest locationImageRequest = new JsonObjectRequest(
                Request.Method.GET,
                getURL(),
                new FlickrImageResponse(mContext, mListener),
                new FlickrImageErrorResponse(mContext));
        // add request to the global queue
        VolleySingleton.getInstance(mContext).addToRequestQueue(locationImageRequest, TAG);
    }


    /**
     * Prepare url address for current location
     *
     * @return Uri with current location
     */
    private String getURL() {
        switch (mImageToLoad) {
            case DYNAMIC_LOCATION:
                return String.format(WeatherConfig.FLICKR_SEARCH_LOCATION_IMAGE_URL,
                        WeatherConfig.FLICKR_API_KEY,
                        stripAccents(mLocationName).replace(" ", "+"),
                        mLatitude,
                        mLongitude);
            case DYNAMIC_WEATHER_LOCATION:
                return String.format(WeatherConfig.FLICKR_SEARCH_LOCATION_IMAGE_URL,
                        WeatherConfig.FLICKR_API_KEY,
                        getWeatherCondition().replace(" ", "+"),
                        mLatitude,
                        mLongitude);
            case DYNAMIC_WEATHER:
                return String.format(WeatherConfig.FLICKR_SEARCH_WEATHER_IMAGE_URL,
                        WeatherConfig.FLICKR_API_KEY,
                        getWeatherCondition().replace(" ", "+"));
            default:
                return String.format(WeatherConfig.FLICKR_SEARCH_LOCATION_IMAGE_URL,
                        WeatherConfig.FLICKR_API_KEY,
                        stripAccents(mLocationName).replace(" ", "+"),
                        mLatitude,
                        mLongitude);
        }
    }


    /**
     * Convert weather condition to more simple tags
     * From light rain to rain etc..
     *
     * @return converted weather condition
     */
    private String getWeatherCondition() {
        if (mWeather.equalsIgnoreCase("clear")) {
            return "sunny";
        } else if (mWeather.equalsIgnoreCase("sun")) {
            return "sunny";
        } else if (mWeather.equalsIgnoreCase("storm")) {
            return "storm";
        } else if (mWeather.equalsIgnoreCase("rain")) {
            return "rain";
        } else if (mWeather.equalsIgnoreCase("fog")) {
            return "foggy";
        } else if (mWeather.equalsIgnoreCase("mist")) {
            return "foggy";
        } else if (mWeather.equalsIgnoreCase("snow")) {
            return "snow";
        } else if (mWeather.equalsIgnoreCase("wind")) {
            return "wind";
        } else if (mWeather.equalsIgnoreCase("cloud")) {
            return "clouds";
        } else if (mWeather.equalsIgnoreCase("blizzard")) {
            return "storm";
        } else {
            return mWeather;
        }
    }


    /**
     * Remove UTF-8 diacritic from string
     *
     * @param s string to remove UFF-8
     * @return string with removed UTF-8
     */
    private String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}