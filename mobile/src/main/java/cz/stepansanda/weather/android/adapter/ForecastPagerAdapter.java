package cz.stepansanda.weather.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.fragment.ForecastListFragment;

/**
 * Created by a111 on 02.06.15.
 */
public class ForecastPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<LocationModel> mLocations;


    public ForecastPagerAdapter(FragmentManager fm, ArrayList<LocationModel> locations) {
        super(fm);
        mLocations = locations;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ForecastListFragment.newInstance(-1);
        } else {
            return ForecastListFragment.newInstance(mLocations.get(position - 1).getId());
        }
    }


    @Override
    public int getCount() {
        return mLocations.size() + 1;
    }


    public LocationModel getLocation(int position) {
        if (position == 0) {
            return mLocations.get(0);
        } else {
            return mLocations.get(position - 1);
        }
    }
}
