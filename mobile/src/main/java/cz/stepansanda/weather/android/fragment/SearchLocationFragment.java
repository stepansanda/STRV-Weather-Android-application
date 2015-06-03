package cz.stepansanda.weather.android.fragment;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.adapter.LocationSearchListAdapter;
import cz.stepansanda.weather.android.database.dao.LocationDao;
import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.utility.Logcat;
import cz.stepansanda.weather.android.utility.NetworkManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchLocationFragment extends Fragment {

    private static final String TAG = SearchLocationFragment.class.getName();
    private static final int MAX_RESULTS_TO_SHOW = 20;

    private EditText mSearchLocationEditText;
    private ListView mListView;
    private LocationSearchListAdapter mAdapter;
    private List<Address> mLocations;
    private TextView mEmptyListTxt;

    private Geocoder mGeocoder;


    public SearchLocationFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocations = new ArrayList<>();
        mAdapter = new LocationSearchListAdapter(mLocations, getActivity());
        // if geocoder not implemented, show toast
        if (!Geocoder.isPresent()) {
            Toast.makeText(
                    getActivity(),
                    getActivity().getResources().getString(R.string.toast_geocoder_not_implemented),
                    Toast.LENGTH_SHORT
            ).show();
        }
        mGeocoder = new Geocoder(getActivity(), Locale.ENGLISH);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_location, container, false);

        getViews(rootView);
        renderView();
        bindViews();

        return rootView;
    }


    private void getViews(View rootView) {
        mSearchLocationEditText = (EditText) rootView.findViewById(R.id.search_location_edit_ext);
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mEmptyListTxt = (TextView) rootView.findViewById(android.R.id.empty);
    }


    private void renderView() {
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyListTxt);
    }


    private void bindViews() {
        if (NetworkManager.isOnline(getActivity())) {
            mSearchLocationEditText.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    searchForLocation(s.toString());
                }


                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }


                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // on city selected, save city to the db and finish activity
                saveSelectedLocation(position);
                getActivity().setResult(Activity.RESULT_OK, null);
                getActivity().finish();
            }
        });
    }


    /**
     * Search for locations that matches users input
     *
     * @param userInput name of location from user
     */
    private void searchForLocation(String userInput) {
        try {
            // clear location array
            mLocations.clear();
            // add new locations from geocoder
            mLocations.addAll(mGeocoder.getFromLocationName(userInput.toString(), MAX_RESULTS_TO_SHOW));
            mAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Save selected location to the db
     */
    private void saveSelectedLocation(int selectedPosition) {
        Address selectedLocation = mLocations.get(selectedPosition);
        LocationModel locationModel = new LocationModel();
        locationModel.setLocationName(selectedLocation.getFeatureName());
        locationModel.setLatitude(selectedLocation.getLatitude());
        locationModel.setLongitude(selectedLocation.getLongitude());
        try {
            LocationDao.create(locationModel);
        } catch (SQLException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
