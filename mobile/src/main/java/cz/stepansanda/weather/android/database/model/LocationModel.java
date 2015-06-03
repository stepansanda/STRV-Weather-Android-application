package cz.stepansanda.weather.android.database.model;

import com.j256.ormlite.field.DatabaseField;

import cz.stepansanda.weather.android.entity.LocationEntity;

/**
 * Created by a111 on 01.06.15.
 */
public class LocationModel {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOCATION_NAME = "location_name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long mId;
    @DatabaseField(columnName = COLUMN_LOCATION_NAME)
    private String mLocationName;
    @DatabaseField(columnName = COLUMN_LATITUDE)
    private double mLatitude;
    @DatabaseField(columnName = COLUMN_LONGITUDE)
    private double mLongitude;


    /**
     * Empty constructor
     */
    public LocationModel() {
    }


    public LocationModel(long id, String locationName, double latitude, double longitude) {
        mId = id;
        mLocationName = locationName;
        mLatitude = latitude;
        mLongitude = longitude;
    }


    public LocationEntity toEntity() {
        return new LocationEntity(mId, mLocationName, mLatitude, mLongitude);
    }


    public void set(LocationEntity l) {
        mLocationName = l.getLocationName();
        mLatitude = l.getLatitude();
        mLongitude = l.getLongitude();
    }


    public double getLongitude() {
        return mLongitude;
    }


    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }


    public double getLatitude() {
        return mLatitude;
    }


    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }


    public String getLocationName() {
        return mLocationName;
    }


    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }


    public long getId() {
        return mId;
    }


    public void setId(long id) {
        mId = id;
    }


    @Override
    public String toString() {
        return mLocationName;
    }
}
