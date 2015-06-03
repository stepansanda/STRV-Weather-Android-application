package cz.stepansanda.weather.android.client.response;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.client.parser.FlickrImageParser;
import cz.stepansanda.weather.android.client.request.FlickrImageRequest.OnLocationImageLoadedListener;
import cz.stepansanda.weather.android.entity.FlickrImageEntity;
import cz.stepansanda.weather.android.utility.Logcat;

/**
 * Created by a111 on 29.05.15.
 */
public class FlickrImageResponse implements Response.Listener<JSONObject> {

    public static final String TAG = FlickrImageResponse.class.getName();

    private OnLocationImageLoadedListener mListener;
    private Context mContext;


    public FlickrImageResponse(Context context, OnLocationImageLoadedListener listener) {
        mContext = context;
        mListener = listener;
    }


    @Override
    public void onResponse(JSONObject jsonObject) {
        Logcat.d(TAG, jsonObject.toString());
        // parse response data
        FlickrImageEntity flickrImageEntity = null;
        try {
            flickrImageEntity = new FlickrImageParser().parse(jsonObject);
        } catch (JSONException e) {
            // handle parsing error
            Logcat.e(TAG + " Parsing Flickr location image error,", e.getMessage());
            e.printStackTrace();
            // let user know, that something went wrong
            Toast.makeText(mContext, mContext.getString(R.string.global_parsing_error_toast), Toast.LENGTH_SHORT).show();
        }
        // call listener when everything is done
        if (mListener != null) {
            mListener.onFlickrImageLoaded(flickrImageEntity);
        }
    }
}
