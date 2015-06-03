package cz.stepansanda.weather.android.client.response;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import cz.stepansanda.weather.android.utility.Logcat;

/**
 * Created by a111 on 29.05.15.
 */
public class FlickrImageErrorResponse implements Response.ErrorListener {

    public static final String TAG = FlickrImageErrorResponse.class.getName();

    private Context mContext;


    public FlickrImageErrorResponse(Context context) {
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
