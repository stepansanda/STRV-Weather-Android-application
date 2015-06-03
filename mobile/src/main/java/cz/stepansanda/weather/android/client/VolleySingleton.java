package cz.stepansanda.weather.android.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cz.stepansanda.weather.android.utility.Logcat;

/**
 * Created by a111 on 15.04.15.
 */
public class VolleySingleton {
    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleySTRVWeather";

    /**
     * Singleton instance
     */
    private static VolleySingleton sInstance;

    /**
     * Global request que for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * Image loader instance for loading images
     */
    private ImageLoader mImageLoader;

    /**
     * Image cache size
     */
    private int mImageCacheSize;

    /**
     * Image cache
     */
    private LruCache<String, Bitmap> mImageCache;


    /**
     * Static method for get static instance of the singleton
     *
     * @param context application context
     * @return instance of VolleySingleton
     */
    public static VolleySingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolleySingleton(context);
        }
        return sInstance;
    }


    /**
     * Private constructor for singleton use
     *
     * @param context application context
     */
    private VolleySingleton(Context context) {

        mRequestQueue = Volley.newRequestQueue(context);

        initializeImageLoader();
    }


    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req request to add to the queue
     * @param tag request tag for cancellation
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        Logcat.d("Adding request to queue: ", req.getUrl());
        req.setRetryPolicy(new DefaultRetryPolicy(25 * 1000, 1, 1.0f));
        req.setShouldCache(true);
        mRequestQueue.add(req);
    }


    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag request tag for cancel request
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    /**
     * Getter for global Image Loader with global image cache memory
     *
     * @return instance of Image Loader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * Initialize Image Loader and set caching images dynamically based on free RAM size
     */
    private void initializeImageLoader() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        mImageCacheSize = maxMemory / 6;
        // initialize cache
        mImageCache = new LruCache<>(mImageCacheSize);
        // initialize image loader
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String url) {
                return mImageCache.get(url);
            }


            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mImageCache.put(url, bitmap);
            }
        });
    }

}
