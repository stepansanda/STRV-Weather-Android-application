package cz.stepansanda.weather.android.client.response;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.client.parser.ForecastParser;
import cz.stepansanda.weather.android.client.request.ForecastWeatherRequest;
import cz.stepansanda.weather.android.database.dao.ForecastDao;
import cz.stepansanda.weather.android.database.model.WeatherModel;
import cz.stepansanda.weather.android.utility.Logcat;

/**
 * Created by a111 on 24.05.15.
 */
public class ForecastResponse implements Response.Listener<JSONObject> {

    public static final String TAG = ForecastResponse.class.getName();

    private ForecastWeatherRequest.OnForecastLoadedListener mListener;
    private Context mContext;
    private boolean mIsCurrentLocation;
    private String mLocationName;


    public ForecastResponse(Context context, ForecastWeatherRequest.OnForecastLoadedListener listener,
                            String locationName, boolean isCurrentLocation) {
        mContext = context;
        mListener = listener;
        mIsCurrentLocation = isCurrentLocation;
        mLocationName = locationName;
    }


    @Override
    public void onResponse(JSONObject jsonObject) {
        Logcat.d(TAG, jsonObject.toString());
        // parse response data
        try {
            List<WeatherModel> forecast = new ForecastParser().parse(jsonObject);
            Logcat.d(TAG, forecast.toString());
            // save data to the db
            saveForecast(forecast);
        } catch (JSONException e) {
            // handle parsing error
            Logcat.e(TAG + " Parsing forecast weather error,", e.getMessage());
            e.printStackTrace();
            // let user know, that something went wrong
            Toast.makeText(mContext, mContext.getString(R.string.global_parsing_error_toast), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Logcat.e(TAG + " SQL forecast weather error,", e.getMessage());
            e.printStackTrace();
            // let user know, that something went wrong
            Toast.makeText(mContext, mContext.getString(R.string.global_parsing_error_toast), Toast.LENGTH_SHORT).show();
        }
        // call listener when everything is done
        if (mListener != null) {
            mListener.forecastWeatherLoaded();
        }
    }


    /**
     * Save forecast data to the db
     *
     * @param forecast parsed forecast collection
     * @throws SQLException exception during saving to the db
     */
    private void saveForecast(List<WeatherModel> forecast) throws SQLException {
        if (forecast.size() > 0) {
            if (mIsCurrentLocation) {
                ForecastDao.replace(forecast, forecast.get(0).getCityName(), true);
            } else {
                ForecastDao.replace(forecast, mLocationName, false);
            }
        }
    }
}
