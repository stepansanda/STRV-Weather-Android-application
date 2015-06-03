package cz.stepansanda.weather.android.client.response;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.client.parser.CurrentWeatherParser;
import cz.stepansanda.weather.android.client.request.CurrentWeatherRequest.OnCurrentWeatherLoadedListener;
import cz.stepansanda.weather.android.database.dao.CurrentWeatherDao;
import cz.stepansanda.weather.android.database.model.WeatherModel;
import cz.stepansanda.weather.android.utility.Logcat;

/**
 * Created by a111 on 24.05.15.
 */
public class CurrentWeatherResponse implements Response.Listener<JSONObject> {

    public static final String TAG = CurrentWeatherResponse.class.getName();

    private OnCurrentWeatherLoadedListener mListener;
    private Context mContext;
    private boolean mIsCurrentLocation;
    private String mLocationName;


    public CurrentWeatherResponse(Context context, OnCurrentWeatherLoadedListener listener,
                                  String locationName, boolean isCurrentLocation) {
        mContext = context;
        mListener = listener;
        mIsCurrentLocation = isCurrentLocation;
        mLocationName = locationName;
    }


    @Override
    public void onResponse(JSONObject jsonObject) {
        Logcat.d(TAG, jsonObject.toString());
        try {
            // parse response data
            WeatherModel weatherModel = new CurrentWeatherParser().parse(jsonObject);
            Logcat.d(TAG, weatherModel.toString());
            // save
            saveWeather(weatherModel);
        } catch (JSONException e) {
            // handle parsing error
            Logcat.e(TAG + " Parsing current weather error,", e.getMessage());
            e.printStackTrace();
            // let user know, that something went wrong
            Toast.makeText(mContext, mContext.getString(R.string.global_parsing_error_toast), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Logcat.e(TAG + " SQL current weather error,", e.getMessage());
            e.printStackTrace();
            // let user know, that something went wrong
            Toast.makeText(mContext, mContext.getString(R.string.global_parsing_error_toast), Toast.LENGTH_SHORT).show();
        }
        // call listener when everything is done
        if (mListener != null) {
            mListener.currentWeatherLoaded();
        }
    }


    /**
     * Save weather to the database
     *
     * @param weatherModel model to save
     * @throws SQLException exception during saving to the db
     */
    private void saveWeather(WeatherModel weatherModel) throws SQLException {
        if (mIsCurrentLocation) {
            // save data to the db
            CurrentWeatherDao.replace(weatherModel, weatherModel.getCityName(), mIsCurrentLocation);
        } else {
            CurrentWeatherDao.replace(weatherModel, mLocationName, mIsCurrentLocation);
        }
    }
}