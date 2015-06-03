package cz.stepansanda.weather.android.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.sql.SQLException;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.WeatherConfig;
import cz.stepansanda.weather.android.activity.MainActivity;
import cz.stepansanda.weather.android.client.VolleySingleton;
import cz.stepansanda.weather.android.client.request.CurrentWeatherRequest;
import cz.stepansanda.weather.android.client.request.CurrentWeatherRequest.OnCurrentWeatherLoadedListener;
import cz.stepansanda.weather.android.client.request.FlickrImageRequest;
import cz.stepansanda.weather.android.database.dao.CurrentWeatherDao;
import cz.stepansanda.weather.android.database.dao.LocationDao;
import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.database.model.WeatherModel;
import cz.stepansanda.weather.android.entity.CurrentWeatherEntity;
import cz.stepansanda.weather.android.entity.FlickrImageEntity;
import cz.stepansanda.weather.android.geolocation.GeolocationHelper;
import cz.stepansanda.weather.android.geolocation.GeolocationHelper.OnLocationChangedListener;
import cz.stepansanda.weather.android.utility.Logcat;
import cz.stepansanda.weather.android.utility.NetworkManager;
import cz.stepansanda.weather.android.utility.SettingsHelper;
import cz.stepansanda.weather.android.utility.SettingsHelper.ImageToLoad;
import cz.stepansanda.weather.android.utility.UnitConverterHelper;

import static cz.stepansanda.weather.android.client.request.FlickrImageRequest.OnLocationImageLoadedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment implements OnCurrentWeatherLoadedListener,
        OnLocationImageLoadedListener, OnLocationChangedListener, OnSharedPreferenceChangeListener {

    private static final String TAG = CurrentWeatherFragment.class.getName();
    private static final String LOCATION_ID_ARG = "location_id";

    private ImageView mLocationImg;
    private TextView mLocationNameTxt;
    private TextView mWeatherDescriptionTxt;
    private ImageView mWeatherImg;
    private TextView mTemperatureTxt;
    private TextView mHumidityTxt;
    private TextView mPrecipitationTxt;
    private TextView mPressureTxt;
    private TextView mWindSpeedTxt;
    private TextView mWindDirectionTxt;
    private View mCurrentLocationLayout;

    private CurrentWeatherEntity mCurrentWeatherEntity;
    private Location mLocation;
    private LocationModel mLocationModelFromDb;
    private VolleySingleton mVolleySingleton;
    private Context mContext;
    private Bitmap mLocationImageBitmap;
    private GeolocationHelper mGeolocationHelper;
    private Animation mFadeInAnimation;
    private SettingsHelper mSettingsHelper;
    private ImageToLoad mImageToLoad;
    private String mImageWeatherCondition;
    private long mLocationId;
    private boolean mIsCurrentLocation;


    public static CurrentWeatherFragment newInstance(long locationId) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putLong(LOCATION_ID_ARG, locationId);
        fragment.setArguments(args);
        return fragment;
    }


    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLocationId = getArguments().getLong(LOCATION_ID_ARG, -1);
            mIsCurrentLocation = mLocationId < 0;
        }

        // if not current location, load selected location from db
        if (!mIsCurrentLocation) {
            loadSelectedLocation();
        }

        mContext = getActivity().getApplicationContext();
        mFadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        mVolleySingleton = VolleySingleton.getInstance(mContext);
        mGeolocationHelper = GeolocationHelper.getInstance(mContext);
        mSettingsHelper = SettingsHelper.getInstance(mContext);
        mImageToLoad = mSettingsHelper.getImageToLoad();
        mImageWeatherCondition = "";
        // get current weather entity
        loadCurrentWeatherFromDb();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_current_weather, container, false);

        getViews(rootView);

        // if already have older data in db
        if (mCurrentWeatherEntity != null) {
            renderView();
        }

        if (mIsCurrentLocation) {
            // register on location changed listener
            mGeolocationHelper.addLocationChangedListener(this);
        }
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(MainActivity.TODAY_FRAGMENT_POSITION);
    }


    @Override
    public void onResume() {
        super.onResume();
        // register listener on preferences changed
        PreferenceManager.getDefaultSharedPreferences(mContext).registerOnSharedPreferenceChangeListener(this);
        if (mIsCurrentLocation) {
            mLocation = mGeolocationHelper.getLocation();
        }
        loadWeatherDataFromApi();
    }


    @Override
    public void currentWeatherLoaded() {
        // load newest data from db
        loadCurrentWeatherFromDb();
        if (mCurrentWeatherEntity != null) {
            renderView();
            // if image is based on weather condition and weather condition changed
            if (mImageToLoad == ImageToLoad.DYNAMIC_WEATHER_LOCATION
                    && !mImageWeatherCondition.equalsIgnoreCase(mCurrentWeatherEntity.getWeatherCondition())) {
                // restart image to load new one
                mLocationImageBitmap = null;
            }
            // load new image from flickr for current location
            loadLocationImage();
        }
    }


    @Override
    public void onFlickrImageLoaded(FlickrImageEntity flickrImageEntity) {
        if (flickrImageEntity != null) {
            setFlickrDynamicImage(flickrImageEntity);
        } else {
            // if no image found on Flickr, set static image instead
            setStaticLocationImage();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (getActivity() != null) {
            // restart image bitmap to get new one
            mLocationImageBitmap = null;
            // load weather data for new location
            loadWeatherDataFromApi();
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getActivity() != null && mCurrentWeatherEntity != null) {
            renderView();
            mImageToLoad = mSettingsHelper.getImageToLoad();
            mLocationImageBitmap = null;
            loadLocationImage();
        }
    }


    private void getViews(View rootView) {
        mLocationImg = (ImageView) rootView.findViewById(R.id.location_img);
        mLocationNameTxt = (TextView) rootView.findViewById(R.id.location_name_txt);
        mWeatherDescriptionTxt = (TextView) rootView.findViewById(R.id.weather_detail_txt);
        mWeatherImg = (ImageView) rootView.findViewById(R.id.weather_img);
        mTemperatureTxt = (TextView) rootView.findViewById(R.id.temperature_txt);
        mHumidityTxt = (TextView) rootView.findViewById(R.id.humidity_txt);
        mPrecipitationTxt = (TextView) rootView.findViewById(R.id.precipitation_txt);
        mPressureTxt = (TextView) rootView.findViewById(R.id.pressure_txt);
        mWindSpeedTxt = (TextView) rootView.findViewById(R.id.wind_speed_txt);
        mWindDirectionTxt = (TextView) rootView.findViewById(R.id.wind_direction_txt);
        mCurrentLocationLayout = rootView.findViewById(R.id.current_location_layout);
    }


    private void renderView() {
        if (mIsCurrentLocation) {
            mLocationNameTxt.setText(mCurrentWeatherEntity.getCityName());
            mCurrentLocationLayout.setVisibility(View.VISIBLE);
        } else {
            mLocationNameTxt.setText(mLocationModelFromDb.getLocationName());
            mCurrentLocationLayout.setVisibility(View.GONE);
        }
        mWeatherDescriptionTxt.setText(mCurrentWeatherEntity.getWeatherCondition());
        mTemperatureTxt.setText(mSettingsHelper.getTemperatureString(Math.round(mCurrentWeatherEntity.getTemperature())));
        mHumidityTxt.setText(mCurrentWeatherEntity.getHumidity() + "%");
        mPrecipitationTxt.setText(UnitConverterHelper.roundTwoDecimals(mCurrentWeatherEntity.getRain(), 2) + " mm");
        mPressureTxt.setText(Math.round(mCurrentWeatherEntity.getPressure()) + " hPa");
        mWindSpeedTxt.setText(mSettingsHelper.getWindSpeedString(Math.round(mCurrentWeatherEntity.getWindSpeed())));
        mWindDirectionTxt.setText(UnitConverterHelper.getWindDirectionString(mContext, mCurrentWeatherEntity.getWindDegrees()));

        String imageUrl = String.format(WeatherConfig.WEATHER_ICON_URL, mCurrentWeatherEntity.getIcon());
        mVolleySingleton.getImageLoader().get(imageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer.getBitmap() != null) {
                    mWeatherImg.setImageBitmap(imageContainer.getBitmap());
                }
            }


            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }


    private void setFlickrDynamicImage(FlickrImageEntity flickrImageEntity) {
        String imageUrl = String.format(WeatherConfig.FLICKR_LOCATION_IMAGE_URL,
                flickrImageEntity.getServer(),
                flickrImageEntity.getId(),
                flickrImageEntity.getSecret()
        );
        mVolleySingleton.getImageLoader().get(imageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer.getBitmap() != null) {
                    mLocationImageBitmap = imageContainer.getBitmap();
                    mLocationImg.setImageBitmap(mLocationImageBitmap);
                    mLocationImg.startAnimation(mFadeInAnimation);
                }
            }


            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // when error occurs, use static image
                setStaticLocationImage();
            }
        });
    }


    /**
     * Set static image or loads dynamic from flickr api
     */
    private void loadLocationImage() {
        if (mImageToLoad == ImageToLoad.STATIC) {
            setStaticLocationImage();
        } else {
            loadFlickrImage();
        }
    }


    /**
     * Load current weather data from db
     */
    private void loadCurrentWeatherFromDb() {
        // get current weather model from db
        try {
            WeatherModel weatherModel;
            if (mIsCurrentLocation) {
                weatherModel = CurrentWeatherDao.readCurrentPositionWeather();
            } else {
                weatherModel = CurrentWeatherDao.readLocationWeather(mLocationModelFromDb.getLocationName());
            }
            if (weatherModel != null) {
                // get current weather entity
                mCurrentWeatherEntity = weatherModel.toEntity();
            }
        } catch (SQLException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Load location from db
     */
    private void loadSelectedLocation() {
        try {
            mLocationModelFromDb = LocationDao.read(mLocationId);
            if (mLocationModelFromDb == null) {
                Logcat.e("", "");
            }
            mLocation = new Location("");
            mLocation.setLongitude(mLocationModelFromDb.getLongitude());
            mLocation.setLatitude(mLocationModelFromDb.getLatitude());
        } catch (SQLException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Load current weather data from api
     */
    private void loadWeatherDataFromApi() {
        if (NetworkManager.isOnline(mContext)) {
            if (mLocation != null) {
                if (mIsCurrentLocation) {
                    new CurrentWeatherRequest(mContext, mLocation, mIsCurrentLocation, "", this).loadCurrentWeather();
                } else {
                    new CurrentWeatherRequest(mContext, mLocation, mIsCurrentLocation, mLocationModelFromDb.getLocationName(), this).loadCurrentWeather();
                }
            }
        } else {
            Logcat.d(TAG, "No internet");
        }
    }


    /**
     * Load current location image from flickr
     */
    private void loadFlickrImage() {
        if (mLocationImageBitmap == null && NetworkManager.isOnline(mContext)) {
            if (mLocation != null) {
                mImageWeatherCondition = mCurrentWeatherEntity.getWeatherCondition();
                // set location name from current position or from saved location in db
                String locationName = mIsCurrentLocation ? mCurrentWeatherEntity.getCityName() : mLocationModelFromDb.getLocationName();
                new FlickrImageRequest(mContext,
                        mLocation.getLongitude(),
                        mLocation.getLatitude(),
                        locationName,
                        mCurrentWeatherEntity.getWeatherCondition(),
                        mImageToLoad,
                        this).loadLocationImage();
            }
        }
    }


    /**
     * Set static image according to weather condition
     */
    private void setStaticLocationImage() {
        if (mCurrentWeatherEntity == null) return;
        String weather = mCurrentWeatherEntity.getWeatherCondition();
        if (weather.toLowerCase().contains("rain")) {
            mLocationImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bcg_rain));
        } else if (weather.toLowerCase().contains("snow")) {
            mLocationImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bcg_snow));
        } else if (weather.toLowerCase().contains("fog") || weather.toLowerCase().contains("misty")) {
            mLocationImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bcg_foggy));
        } else if (weather.toLowerCase().contains("storm") || weather.toLowerCase().contains("thunder")) {
            mLocationImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bcg_storm));
        } else if (weather.toLowerCase().contains("cloud")) {
            mLocationImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bcg_clouds));
        } else {
            mLocationImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bcg_sunny));
        }
        mLocationImg.startAnimation(mFadeInAnimation);

    }
}
