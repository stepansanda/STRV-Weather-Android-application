package cz.stepansanda.weather.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.stepansanda.weather.android.R;
import cz.stepansanda.weather.android.WeatherConfig;
import cz.stepansanda.weather.android.client.VolleySingleton;
import cz.stepansanda.weather.android.database.model.WeatherModel;
import cz.stepansanda.weather.android.utility.SettingsHelper;

/**
 * Created by a111 on 29.05.15.
 */
public class ForecastRecycleViewAdapter extends RecyclerView.Adapter<ForecastRecycleViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView weatherImg;
        public TextView weatherConditionOnDayTxt;
        public TextView temperatureTxt;
        public TextView dateTxt;


        public ViewHolder(View view) {
            super(view);
            rootView = view;
            weatherImg = (ImageView) view.findViewById(R.id.weather_img);
            weatherConditionOnDayTxt = (TextView) view.findViewById(R.id.weather_condition_day_txt);
            temperatureTxt = (TextView) view.findViewById(R.id.temperature_txt);
            dateTxt = (TextView) view.findViewById(R.id.date_txt);
        }
    }

    private List<WeatherModel> mForecastList;
    private Context mContext;
    private VolleySingleton mVolleySingleton;
    private SettingsHelper mSettingsHelper;


    public ForecastRecycleViewAdapter(List<WeatherModel> forecastList, Context context) {
        mForecastList = forecastList;
        mContext = context;
        mSettingsHelper = SettingsHelper.getInstance(context);
        mVolleySingleton = VolleySingleton.getInstance(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_forecast_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        WeatherModel weatherModel = mForecastList.get(i);
        String weatherCondition = weatherModel.getWeatherConditionDescription();
        // first letter uppercase
        weatherCondition = getFirstLetterUppercase(weatherCondition);
        // get day name from date
        String dayName = getDayName(new Date(weatherModel.getTimestamp()));
        loadWeatherImage(weatherModel.getIcon(), viewHolder.weatherImg);
        viewHolder.weatherConditionOnDayTxt.setText(weatherCondition
                + " "
                + mContext.getResources().getString(R.string.on_day)
                + " "
                + dayName);
        viewHolder.temperatureTxt.setText(mSettingsHelper.getTemperatureString(Math.round(weatherModel.getTemperature())));
        viewHolder.dateTxt.setText(getFullDate(new Date(weatherModel.getTimestamp())));
    }


    @Override
    public int getItemCount() {
        return mForecastList.size();
    }


    private void loadWeatherImage(String iconUrl, final ImageView weatherImageView) {
        mVolleySingleton.getImageLoader().get(String.format(WeatherConfig.WEATHER_ICON_URL,
                iconUrl), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer.getBitmap() != null && weatherImageView != null) {
                    weatherImageView.setImageBitmap(imageContainer.getBitmap());
                }
            }


            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

    }


    private String getDayName(Date date) {
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return outFormat.format(date);
    }


    private String getFullDate(Date date) {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(date);
    }


    private String getFirstLetterUppercase(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
