package cz.stepansanda.weather.android.client.response;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import cz.stepansanda.weather.android.utility.Logcat;

/**
 * Created by a111 on 24.05.15.
 */
public class CurrentWeatherErrorResponse implements Response.ErrorListener {

    public static final String TAG = CurrentWeatherErrorResponse.class.getName();

    private Context mContext;


    public CurrentWeatherErrorResponse(Context context) {
        mContext = context;
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if (volleyError.getMessage() != null) {
            Logcat.e(TAG, volleyError.getMessage());
        } else {
            Logcat.e(TAG, "onErrorResponse");
        }
    }
}
