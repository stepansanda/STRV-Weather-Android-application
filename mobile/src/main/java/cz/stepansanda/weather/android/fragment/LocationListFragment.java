package cz.stepansanda.weather.android.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.activity.MainActivity;
import cz.stepansanda.weather.android.activity.SearchLocationActivity;
import cz.stepansanda.weather.android.database.dao.LocationDao;
import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.dialog.RemoveLocationDialog;
import cz.stepansanda.weather.android.dialog.RemoveLocationDialog.OnRemoveLocationDialogResult;
import cz.stepansanda.weather.android.utility.Logcat;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationListFragment extends Fragment implements OnRemoveLocationDialogResult {
    private static final String TAG = LocationListFragment.class.getName();
    private static final int SEARCH_LOCATION_REQUEST_CODE = 1;
    private static final int REMOVE_LOCATION_REQUEST_CODE = 2;

    private ListView mListView;
    private ArrayAdapter<LocationModel> mAdapter;
    private List<LocationModel> mLocations;
    private FloatingActionButton mAddLocationBtn;
    private TextView mEmptyListTxt;


    public LocationListFragment() {
        // Required empty public constructor
    }


    public static LocationListFragment newInstance() {
        LocationListFragment fragment = new LocationListFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocations = new ArrayList<>();
        loadLocationsFromDB();
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mLocations);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_location_select, container, false);

        getViews(rootView);
        renderView();
        bindViews();

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(MainActivity.LOCATION_FRAGMENT_POSITION);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_LOCATION_REQUEST_CODE) {
            loadLocationsFromDB();
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRemoveLocationDialogResult(int resultCode, int position) {
        if (resultCode == Activity.RESULT_OK) {
            removeLocationFromDb(position);
            loadLocationsFromDB();
            mAdapter.notifyDataSetChanged();
        }
    }


    private void getViews(View rootView) {
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mAddLocationBtn = (FloatingActionButton) rootView.findViewById(R.id.add_location_btn);
        mEmptyListTxt = (TextView) rootView.findViewById(android.R.id.empty);
    }


    private void renderView() {
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyListTxt);
    }


    private void bindViews() {
        mAddLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationSearchActivity();
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }


    /**
     * Removes locatoin from db
     *
     * @param position of the selected location to delete
     */
    private void removeLocationFromDb(int position) {
        LocationModel locationToRemove = mLocations.get(position);
        try {
            LocationDao.delete(locationToRemove.getId());
        } catch (SQLException e) {
            Logcat.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Loads saved locations from db
     */
    private void loadLocationsFromDB() {
        try {
            mLocations.clear();
            mLocations.addAll(LocationDao.readAll());
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }


    private void startLocationSearchActivity() {
        Intent searchLocationIntent = SearchLocationActivity.newIntent(getActivity());
        startActivityForResult(searchLocationIntent, SEARCH_LOCATION_REQUEST_CODE);
    }


    private void showDeleteDialog(int locationPosition) {
        RemoveLocationDialog removeLocationDialog = RemoveLocationDialog.newInstance(locationPosition);
        removeLocationDialog.setTargetFragment(this, REMOVE_LOCATION_REQUEST_CODE);
        removeLocationDialog.show(getActivity().getSupportFragmentManager(), TAG);
    }
}
