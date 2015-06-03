package cz.stepansanda.weather.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by a111 on 02.06.15.
 */
public class LocationSearchListAdapter extends BaseAdapter {

    private List<Address> mLocations;
    private Context mContext;


    public LocationSearchListAdapter(List<Address> locations, Context context) {
        mLocations = locations;
        mContext = context;
    }


    @Override
    public int getCount() {
        if (mLocations != null) {
            return mLocations.size();
        } else {
            return 0;
        }
    }


    @Override
    public Object getItem(int position) {
        return mLocations.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        // get current address
        Address location = mLocations.get(position);
        ViewHolder viewHolder = new ViewHolder(convertView);
        // set location name
        viewHolder.locationName.setText(location.getFeatureName());

        return convertView;
    }


    private class ViewHolder {
        public TextView locationName;


        public ViewHolder(View view) {
            locationName = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
