package cz.stepansanda.weather.android.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.adapter.ForecastRecycleViewAdapter;
import cz.stepansanda.weather.android.client.request.ForecastWeatherRequest;
import cz.stepansanda.weather.android.client.request.ForecastWeatherRequest.OnForecastLoadedListener;
import cz.stepansanda.weather.android.database.dao.ForecastDao;
import cz.stepansanda.weather.android.database.dao.LocationDao;
import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.database.model.WeatherModel;
import cz.stepansanda.weather.android.geolocation.GeolocationHelper;
import cz.stepansanda.weather.android.geolocation.GeolocationHelper.OnLocationChangedListener;
import cz.stepansanda.weather.android.utility.DividerItemDecoration;
import cz.stepansanda.weather.android.utility.Logcat;
import cz.stepansanda.weather.android.utility.NetworkManager;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class ForecastListFragment extends Fragment implements OnForecastLoadedListener,
        OnRefreshListener, OnLocationChangedListener, OnSharedPreferenceChangeListener {

    private static final String TAG = ForecastListFragment.class.getName();
    private static final String LOCATION_ID_ARG = "location_id";


    private RecyclerView mRecyclerView;
    private ForecastRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeToRefreshLayout;
    private AlphaInAnimationAdapter mAlphaAdapter;
    private ScaleInAnimationAdapter mScaleInAnimationAdapter;

    private List<WeatherModel> mForecastList;
    private Context mContext;
    private Location mLocation;
    private LocationModel mLocationModelFromDb;
    private GeolocationHelper mGeolocationHelper;

    private boolean mIsCurrentLocation;
    private long mLocationId;


    public static ForecastListFragment newInstance(long locationName) {
        ForecastListFragment fragment = new ForecastListFragment();
        Bundle args = new Bundle();
        args.putLong(LOCATION_ID_ARG, locationName);
        fragment.setArguments(args);
        return fragment;
    }


    public ForecastListFragment() {
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
        mGeolocationHelper = GeolocationHelper.getInstance(mContext);
        mForecastList = new ArrayList<>();
        loadForecastFromDb();
        mAdapter = new ForecastRecycleViewAdapter(mForecastList, mContext);
        mAlphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        mScaleInAnimationAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast_list, container, false);

        getViews(rootView);
        renderView();

        mGeolocationHelper.addLocationChangedListener(this);

        if (mIsCurrentLocation) {
            // register on location changed listener
            mGeolocationHelper.addLocationChangedListener(this);
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        // register listener on preferences changed
        PreferenceManager.getDefaultSharedPreferences(mContext).registerOnSharedPreferenceChangeListener(this);
        if (mIsCurrentLocation) {
            mLocation = mGeolocationHelper.getLocation();
        }
        loadForecastDataFromApi();
    }


    @Override
    public void forecastWeatherLoaded() {
        loadForecastFromDb();
        mAdapter.notifyDataSetChanged();
        // play animation again when loaded new data
        mAlphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        mScaleInAnimationAdapter = new ScaleInAnimationAdapter(mAlphaAdapter);
        mRecyclerView.setAdapter(mScaleInAnimationAdapter);
        swipeToRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        loadForecastDataFromApi();
    }


    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (getActivity() != null) {
            loadForecastDataFromApi();
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getActivity() != null && mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    private void getViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.forecast_recycle_view);
        swipeToRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.container_content);
    }


    private void renderView() {
        // set recycle view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // set divider
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));
        // set animated adapter
        mRecyclerView.setAdapter(mScaleInAnimationAdapter);
        // set swipe to refresh color
        swipeToRefreshLayout.setColorSchemeResources(R.color.global_color_primary, R.color.global_color_accent);
        swipeToRefreshLayout.setOnRefreshListener(this);
    }


    /**
     * Loads stored forecast data from database
     */
    private void loadForecastFromDb() {
        try {
            mForecastList.clear();
            if (mIsCurrentLocation) {
                mForecastList.addAll(ForecastDao.readAllCurrentLocation());
            } else {
                mForecastList.addAll(ForecastDao.readAllSelectedLocation(mLocationModelFromDb.getLocationName()));
            }
        } catch (SQLException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Loads newest forecast data from api
     */
    private void loadForecastDataFromApi() {
        if (NetworkManager.isOnline(mContext)) {
            if (mLocation != null) {
                if (mIsCurrentLocation) {
                    new ForecastWeatherRequest(mContext, mLocation, mIsCurrentLocation, "", this).loadForecast();
                } else {
                    new ForecastWeatherRequest(mContext, mLocation, mIsCurrentLocation, mLocationModelFromDb.getLocationName(), this).loadForecast();
                }
            }
        } else {
            Logcat.d(TAG, "No internet");
        }
    }


    /**
     * Load location from db
     */
    private void loadSelectedLocation() {
        try {
            mLocationModelFromDb = LocationDao.read(mLocationId);
            mLocation = new Location("");
            mLocation.setLongitude(mLocationModelFromDb.getLongitude());
            mLocation.setLatitude(mLocationModelFromDb.getLatitude());
        } catch (SQLException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
