package cz.stepansanda.weather.android.database;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cz.stepansanda.weather.android.WeatherApplication;
import cz.stepansanda.weather.android.database.model.LocationModel;
import cz.stepansanda.weather.android.database.model.WeatherModel;
import cz.stepansanda.weather.android.utility.Logcat;

/**
 * Created by a111 on 24.05.15.
 */
public class WeatherDatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String TAG = WeatherDatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "weather_app.db";
    private static final int DATABASE_VERSION = 6;

    private Dao<WeatherModel, Long> mWeatherDao = null;
    private Dao<LocationModel, Long> mLocationDao = null;


    public static synchronized WeatherDatabaseHelper getInstance() {
        if (sInstance == null) {
            sInstance = new WeatherDatabaseHelper();
        }
        return sInstance;
    }


    /**
     * Singleton instance
     */
    private static WeatherDatabaseHelper sInstance;


    /**
     * Private singleton constructor
     */
    private WeatherDatabaseHelper() {
        super(WeatherApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Logcat.d("DatabaseHelper.onCreate()");
            TableUtils.createTable(connectionSource, WeatherModel.class);
            TableUtils.createTable(connectionSource, LocationModel.class);
        } catch (android.database.SQLException e) {
            Logcat.e(TAG + ".onCreate(): can't create database", e);
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            Logcat.e(TAG + ".onCreate(): can't create database", e);
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Logcat.d(TAG + ".onUpgrade()");

            // clear database
            TableUtils.dropTable(getConnectionSource(), WeatherModel.class, true);
            TableUtils.dropTable(getConnectionSource(), LocationModel.class, true);

            TableUtils.createTable(getConnectionSource(), WeatherModel.class);
            TableUtils.createTable(getConnectionSource(), LocationModel.class);
        } catch (android.database.SQLException e) {
            Logcat.e(TAG + ".onUpgrade(): can't upgrade database", e);
            e.printStackTrace();
        } catch (SQLException e) {
            Logcat.e(TAG + ".onUpgrade(): can't upgrade database", e);
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        super.close();
        mWeatherDao = null;
    }


    public synchronized void clearDatabase() {
        try {
            Logcat.d("DatabaseHelper.clearDatabase()");

            TableUtils.dropTable(getConnectionSource(), WeatherModel.class, true);
            TableUtils.dropTable(getConnectionSource(), LocationModel.class, true);

            TableUtils.createTable(getConnectionSource(), WeatherModel.class);
            TableUtils.createTable(getConnectionSource(), LocationModel.class);
        } catch (android.database.SQLException e) {
            Logcat.e(TAG + ".clearDatabase(): can't clear database", e);
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            Logcat.e(TAG + ".clearDatabase(): can't clear database", e);
            e.printStackTrace();
        }
    }


    public synchronized Dao<WeatherModel, Long> getWeatherDao() throws java.sql.SQLException {
        if (mWeatherDao == null) {
            mWeatherDao = getDao(WeatherModel.class);
        }
        return mWeatherDao;
    }


    public synchronized Dao<LocationModel, Long> getLocationDao() throws java.sql.SQLException {
        if (mLocationDao == null) {
            mLocationDao = getDao(LocationModel.class);
        }
        return mLocationDao;
    }
}
