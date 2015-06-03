package cz.stepansanda.weather.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cz.stepansanda.weather.android.R;

/**
 * Created by a111 on 29.05.15.
 */
public class DrawerMenuListAdapter extends BaseAdapter {

    private static final int MENU_ITEMS_COUNT = 3;

    private Context mContext;
    private String[] mMenuItems;


    public DrawerMenuListAdapter(Context context) {
        mContext = context;
        mMenuItems = new String[]{
                mContext.getString(R.string.fragment_today),
                mContext.getString(R.string.fragment_forecast),
                mContext.getString(R.string.fragment_locations)
        };
    }


    @Override
    public int getCount() {
        return MENU_ITEMS_COUNT;
    }


    @Override
    public Object getItem(int position) {
        return mMenuItems[position];
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_item, null);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        // set menu text
        viewHolder.text.setText(mMenuItems[position]);
        // set drawable
        switch (position) {
            case 0:
                viewHolder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_sunny));
                break;
            case 1:
                viewHolder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_today));
                break;
            default:
                viewHolder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_location));
                break;
        }
        return convertView;
    }


    private class ViewHolder {
        public ImageView image;
        public TextView text;


        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.menu_img);
            text = (TextView) view.findViewById(R.id.menu_txt);
        }
    }
}
