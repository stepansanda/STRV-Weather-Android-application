package cz.stepansanda.weather.android.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.activity.MainActivity;
import cz.stepansanda.weather.android.adapter.ForecastPagerAdapter;
import cz.stepansanda.weather.android.database.dao.LocationDao;
import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.utility.Logcat;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastPagerFragment extends Fragment {

    private final static String TAG = CurrentWeatherFragment.class.getName();

    private ViewPager mViewPager;
    private ForecastPagerAdapter mAdapter;
    private ArrayList<LocationModel> mLocations;
    private TextView mLocationNameTxt;
    private ImageButton mNextBt;
    private ImageButton mPrevBtn;
    private Animation mSlideRight;
    private Animation mSlideLeft;
    private int mPreviousPosition;
    private CircleIndicator mProgressPagerIndicator;


    public static ForecastPagerFragment newInstance() {
        ForecastPagerFragment fragment = new ForecastPagerFragment();
        return fragment;
    }


    public ForecastPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocations = new ArrayList<>();
        loadLocations();
        mAdapter = new ForecastPagerAdapter(getFragmentManager(), mLocations);
        mSlideRight = AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_enter);
        mSlideLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_enter);
        mPreviousPosition = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_forecast_pager, container, false);

        getViews(rootView);
        renderView();
        bindViews();

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(MainActivity.FORECAST_FRAGMENT_POSITION);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadLocations();
        mAdapter.notifyDataSetChanged();
    }


    private void getViews(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mLocationNameTxt = (TextView) rootView.findViewById(R.id.location_name_txt);
        renderLocationName(mViewPager.getCurrentItem());
        mNextBt = (ImageButton) rootView.findViewById(R.id.next_btn);
        mPrevBtn = (ImageButton) rootView.findViewById(R.id.prev_btn);
        mProgressPagerIndicator = (CircleIndicator) rootView.findViewById(R.id.pager_indicator);
    }


    private void renderView() {
        mViewPager.setAdapter(mAdapter);
        mProgressPagerIndicator.setViewPager(mViewPager);
        // at first hide prev btn
        mPrevBtn.setVisibility(View.INVISIBLE);
        // if only one item, hide next btn
        if (mAdapter.getCount() == 1) {
            mNextBt.setVisibility(View.INVISIBLE);
        }
    }


    private void bindViews() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }


            @Override
            public void onPageSelected(int i) {
                // change location name
                renderLocationName(i);
                // show or hide next and prev btn
                if (i == 0) {
                    mPrevBtn.setVisibility(View.INVISIBLE);
                } else {
                    mPrevBtn.setVisibility(View.VISIBLE);
                }
                if (i == mAdapter.getCount() - 1) {
                    mNextBt.setVisibility(View.INVISIBLE);
                } else {
                    mNextBt.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mNextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(Math.min(mViewPager.getCurrentItem() + 1, mAdapter.getCount()));
            }
        });

        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(Math.max(mViewPager.getCurrentItem() - 1, 0));
            }
        });
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


    private void renderLocationName(int pagerPosition) {
        if (pagerPosition == 0) {
            mLocationNameTxt.setText(getActivity().getString(R.string.current_position));
        } else {
            mLocationNameTxt.setText(mAdapter.getLocation(pagerPosition).getLocationName());
        }
        if (pagerPosition > mPreviousPosition) {
            mLocationNameTxt.startAnimation(mSlideLeft);
        } else {
            mLocationNameTxt.startAnimation(mSlideRight);
        }
        mPreviousPosition = pagerPosition;
    }

}
