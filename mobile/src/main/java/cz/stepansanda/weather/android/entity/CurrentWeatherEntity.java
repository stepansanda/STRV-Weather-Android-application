package cz.stepansanda.weather.android.entity;

import java.util.Date;

/**
 * Created by a111 on 24.05.15.
 */
public class CurrentWeatherEntity {

    private long mId;
    private float mTemperature;
    private String mIcon;
    private float mWindSpeed;
    private float mWindDegrees;
    private String mCityName;
    private String mCountry;
    private Date mDate;
    private String mWeatherCondition;
    private String mWeatherConditionDescription;
    private int mHumidity;
    private float mPressure;
    private float mRain;
    private boolean mIsForecast;
    private boolean mIsCurrentPosition;


    /**
     * Empty constructor
     */
    public CurrentWeatherEntity() {

    }


    public CurrentWeatherEntity(float temperature, String icon, float windSpeed, float windDegrees,
                                String cityName, String country, Date date, String weatherCondition,
                                String weatherConditionDescription, int humidity, float pressure,
                                float rain, boolean isForecast, boolean isCurrentPositoin) {
        mTemperature = temperature;
        mIcon = icon;
        mWindSpeed = windSpeed;
        mWindDegrees = windDegrees;
        mCityName = cityName;
        mCountry = country;
        mDate = date;
        mWeatherCondition = weatherCondition;
        mWeatherConditionDescription = weatherConditionDescription;
        mHumidity = humidity;
        mPressure = pressure;
        mRain = rain;
        mIsForecast = isForecast;
        mIsCurrentPosition = isCurrentPositoin;
    }


    public long getId() {
        return mId;
    }


    public void setId(long id) {
        mId = id;
    }


    public float getTemperature() {
        return mTemperature;
    }


    public void setTemperature(float temperature) {
        mTemperature = temperature;
    }


    public String getIcon() {
        return mIcon;
    }


    public void setIcon(String icon) {
        mIcon = icon;
    }


    public float getWindSpeed() {
        return mWindSpeed;
    }


    public void setWindSpeed(float windSpeed) {
        mWindSpeed = windSpeed;
    }


    public float getWindDegrees() {
        return mWindDegrees;
    }


    public void setWindDegrees(float windDegrees) {
        mWindDegrees = windDegrees;
    }


    public String getCityName() {
        return mCityName;
    }


    public void setCityName(String cityName) {
        mCityName = cityName;
    }


    public String getCountry() {
        return mCountry;
    }


    public void setCountry(String country) {
        mCountry = country;
    }


    public Date getDate() {
        return mDate;
    }


    public void setDate(Date date) {
        mDate = date;
    }


    public String getWeatherCondition() {
        return mWeatherCondition;
    }


    public void setWeatherCondition(String weatherCondition) {
        mWeatherCondition = weatherCondition;
    }


    public String getWeatherConditionDescription() {
        return mWeatherConditionDescription;
    }


    public void setWeatherConditionDescription(String weatherConditionDescription) {
        mWeatherConditionDescription = weatherConditionDescription;
    }


    public int getHumidity() {
        return mHumidity;
    }


    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }


    public float getPressure() {
        return mPressure;
    }


    public void setPressure(float pressure) {
        mPressure = pressure;
    }


    public float getRain() {
        return mRain;
    }


    public void setRain(float rain) {
        mRain = rain;
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


    public void setIsCurrentPosition(boolean isCurrentPosition) {
        mIsCurrentPosition = isCurrentPosition;
    }


    @Override
    public String toString() {
        return "CurrentWeatherEntity{" +
                "mId=" + mId +
                ", mTemperature=" + mTemperature +
                ", mIcon='" + mIcon + '\'' +
                ", mWindSpeed=" + mWindSpeed +
                ", mWindDegrees=" + mWindDegrees +
                ", mCityName='" + mCityName + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mDate=" + mDate +
                ", mWeatherCondition='" + mWeatherCondition + '\'' +
                ", mWeatherConditionDescription='" + mWeatherConditionDescription + '\'' +
                ", mHumidity=" + mHumidity +
                ", mPressure=" + mPressure +
                ", mRain=" + mRain +
                ", mIsForecast=" + mIsForecast +
                ", mIsCurrentPosition=" + mIsCurrentPosition +
                '}';
    }
}
