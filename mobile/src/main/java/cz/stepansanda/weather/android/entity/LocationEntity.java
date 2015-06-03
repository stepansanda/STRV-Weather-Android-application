package cz.stepansanda.weather.android.entity;

/**
 * Created by a111 on 01.06.15.
 */
public class LocationEntity {

    private long mId;
    private String mLocationName;
    private double mLatitude;
    private double mLongitude;


    /**
     * Empty constructor
     */
    public LocationEntity() {
    }


    public LocationEntity(long id, String locationName, double latitude, double longitude) {
        mId = id;
        mLocationName = locationName;
        mLatitude = latitude;
        mLongitude = longitude;
    }


    public long getId() {
        return mId;
    }


    public void setId(int id) {
        mId = id;
    }


    public String getLocationName() {
        return mLocationName;
    }


    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }


    public double getLatitude() {
        return mLatitude;
    }


    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }


    public double getLongitude() {
        return mLongitude;
    }


    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
