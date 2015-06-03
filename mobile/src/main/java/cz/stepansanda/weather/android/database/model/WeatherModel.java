package cz.stepansanda.weather.android.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import cz.stepansanda.weather.android.entity.CurrentWeatherEntity;

/**
 * Created by a111 on 24.05.15.
 */
@DatabaseTable
public class WeatherModel {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_WIND_SPEED = "wind_speed";
    public static final String COLUMN_WIND_DEGREES = "wind_degrees";
    public static final String COLUMN_CITY_NAME = "city_name";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_WEATHER_CONDITION = "weather_condition";
    public static final String COLUMN_WEATHER_CONDITION_DESCRIPTION = "weather_condition_description";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_RAIN = "mRain";
    public static final String COLUMN_IS_FORECAST = "is_forecast";
    public static final String COLUMN_IS_CURRENT_POSITION = "is_current_position";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long mId;
    @DatabaseField(columnName = COLUMN_TEMPERATURE)
    private float mTemperature;
    @DatabaseField(columnName = COLUMN_ICON)
    private String mIcon;
    @DatabaseField(columnName = COLUMN_WIND_SPEED)
    private float mWindSpeed;
    @DatabaseField(columnName = COLUMN_WIND_DEGREES)
    private float mWindDegrees;
    @DatabaseField(columnName = COLUMN_CITY_NAME)
    private String mCityName;
    @DatabaseField(columnName = COLUMN_COUNTRY)
    private String mCountry;
    @DatabaseField(columnName = COLUMN_TIMESTAMP)
    private long mTimestamp;
    @DatabaseField(columnName = COLUMN_WEATHER_CONDITION)
    private String mWeatherCondition;
    @DatabaseField(columnName = COLUMN_WEATHER_CONDITION_DESCRIPTION)
    private String mWeatherConditionDescription;
    @DatabaseField(columnName = COLUMN_HUMIDITY)
    private int mHumidity;
    @DatabaseField(columnName = COLUMN_PRESSURE)
    private float mPressure;
    @DatabaseField(columnName = COLUMN_RAIN)
    private float mRain;
    @DatabaseField(columnName = COLUMN_IS_FORECAST)
    private boolean mIsForecast;
    @DatabaseField(columnName = COLUMN_IS_CURRENT_POSITION)
    private boolean mIsCurrentPosition;


    /**
     * Empty constructor
     */
    public WeatherModel() {
    }


    public WeatherModel(float temperature, String icon, float windSpeed,
                        float windDegrees, String cityName, String country, long timestamp,
                        String weatherCondition, String weatherConditionDescription,
                        int humidity, float pressure, float rain, boolean isForecast, boolean isCurrentPosition) {
        mTemperature = temperature;
        mIcon = icon;
        mWindSpeed = windSpeed;
        mWindDegrees = windDegrees;
        mCityName = cityName;
        mCountry = country;
        mTimestamp = timestamp;
        mWeatherCondition = weatherCondition;
        mWeatherConditionDescription = weatherConditionDescription;
        mHumidity = humidity;
        mPressure = pressure;
        mRain = rain;
        mIsForecast = isForecast;
        mIsCurrentPosition = isCurrentPosition;
    }


    public CurrentWeatherEntity toEntity() {
        CurrentWeatherEntity e = new CurrentWeatherEntity();
        e.setId(mId);
        e.setTemperature(mTemperature);
        e.setIcon(mIcon);
        e.setWindSpeed(mWindSpeed);
        e.setWindDegrees(mWindDegrees);
        e.setCityName(mCityName);
        e.setCountry(mCountry);
        e.setDate(new Date(mTimestamp));
        e.setWeatherCondition(mWeatherCondition);
        e.setWeatherConditionDescription(mWeatherConditionDescription);
        e.setHumidity(mHumidity);
        e.setPressure(mPressure);
        e.setRain(mRain);
        e.setIsForecast(mIsForecast);
        e.setIsCurrentPosition(mIsCurrentPosition);
        return e;
    }


    public void set(CurrentWeatherEntity e) {
        mTemperature = e.getTemperature();
        mIcon = e.getIcon();
        mWindSpeed = e.getWindSpeed();
        mWindDegrees = e.getWindDegrees();
        mCityName = e.getCityName();
        mCountry = e.getCountry();
        mTimestamp = e.getDate().getTime();
        mWeatherCondition = e.getWeatherCondition();
        mWeatherConditionDescription = e.getWeatherConditionDescription();
        mHumidity = e.getHumidity();
        mPressure = e.getPressure();
        mRain = e.getRain();
        mIsForecast = e.isForecast();
        mIsCurrentPosition = e.isCurrentPosition();
    }


    public long getId() {
        return mId;
    }


    public void setId(long id) {
        this.mId = id;
    }


    public float getTemperature() {
        return mTemperature;
    }


    public void setTemperature(float temperature) {
        this.mTemperature = temperature;
    }


    public String getIcon() {
        return mIcon;
    }


    public void setIcon(String icon) {
        this.mIcon = icon;
    }


    public float getWindSpeed() {
        return mWindSpeed;
    }


    public void setWindSpeed(float windSpeed) {
        this.mWindSpeed = windSpeed;
    }


    public float getWindDegrees() {
        return mWindDegrees;
    }


    public void setWindDegrees(float windDegrees) {
        this.mWindDegrees = windDegrees;
    }


    public String getCityName() {
        return mCityName;
    }


    public void setCityName(String cityName) {
        this.mCityName = cityName;
    }


    public String getCountry() {
        return mCountry;
    }


    public void setCountry(String country) {
        this.mCountry = country;
    }


    public long getTimestamp() {
        return mTimestamp;
    }


    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }


    public String getWeatherCondition() {
        return mWeatherCondition;
    }


    public void setWeatherCondition(String weatherCondition) {
        this.mWeatherCondition = weatherCondition;
    }


    public String getWeatherConditionDescription() {
        return mWeatherConditionDescription;
    }


    public void setWeatherConditionDescription(String weatherConditionDescription) {
        this.mWeatherConditionDescription = weatherConditionDescription;
    }


    public int getHumidity() {
        return mHumidity;
    }


    public void setHumidity(int humidity) {
        this.mHumidity = humidity;
    }


    public float getPressure() {
        return mPressure;
    }


    public void setPressure(float pressure) {
        this.mPressure = pressure;
    }


    public float getRain() {
        return mRain;
    }


    public void setRain(float rain) {
        this.mRain = rain;
    }


    public boolean isForecast() {
        return mIsForecast;
    }


    public void setIsForecast(boolean isForecast) {
        mIsForecast = isForecast;
    }


    public boolean isCurrentPosition() {
        return mIsCurrentPosition;
    }


    public void setIsCurrentPositon(boolean isCurrentPositon) {
        mIsCurrentPosition = isCurrentPositon;
    }
}
