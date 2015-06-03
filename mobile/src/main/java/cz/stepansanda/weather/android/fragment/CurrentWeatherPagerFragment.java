package cz.stepansanda.weather.android.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.activity.MainActivity;
import cz.stepansanda.weather.android.adapter.CurrentWeatherPagerAdapter;
import cz.stepansanda.weather.android.database.dao.LocationDao;
import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.utility.Logcat;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherPagerFragment extends Fragment {

    private final static String TAG = CurrentWeatherFragment.class.getName();

    private ViewPager mViewPager;
    private CurrentWeatherPagerAdapter mAdapter;
    private ArrayList<LocationModel> mLocations;
    private CircleIndicator mProgressPagerIndicator;


    public static CurrentWeatherPagerFragment newInstance() {
        CurrentWeatherPagerFragment fragment = new CurrentWeatherPagerFragment();
        return fragment;
    }


    public CurrentWeatherPagerFragment() {
        // Required empty public constructor
        mLocations = new ArrayList<>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLocations();
        mAdapter = new CurrentWeatherPagerAdapter(getFragmentManager(), mLocations);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_current_weather_pager, container, false);

        getViews(rootView);
        renderView();

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
        loadLocations();
        mAdapter.notifyDataSetChanged();
    }


    private void getViews(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mProgressPagerIndicator = (CircleIndicator) rootView.findViewById(R.id.pager_indicator);
    }


    private void renderView() {
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(false, new ParallaxPagerTransformer(R.id.location_img));
        mProgressPagerIndicator.setViewPager(mViewPager);
    }


    private void loadLocations() {
        try {
            mLocations.clear();
            mLocations.addAll(LocationDao.readAll());
        } catch (SQLException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


}
