package cz.stepansanda.weather.android.database.dao;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cz.stepansanda.weather.android.database.WeatherDatabaseHelper;
import cz.stepansanda.weather.android.database.model.LocationModel;

/**
 * Created by a111 on 01.06.15.
 */
public class LocationDao {

    private static Dao<LocationModel, Long> getDao() throws SQLException {
        WeatherDatabaseHelper databaseHelper = WeatherDatabaseHelper.getInstance();
        return databaseHelper.getLocationDao();
    }


    public static int refresh(LocationModel location) throws SQLException {
        Dao<LocationModel, Long> dao = getDao();
        return dao.refresh(location);
    }


    public static int create(LocationModel location) throws SQLException {
        Dao<LocationModel, Long> dao = getDao();
        return dao.create(location);
    }


    public static LocationModel read(long id) throws SQLException {
        Dao<LocationModel, Long> dao = getDao();
        return dao.queryForId(id);
    }


    public static List<LocationModel> readAll() throws SQLException {
        Dao<LocationModel, Long> dao = getDao();
        return dao.queryForAll();
    }


    public static int update(LocationModel location) throws SQLException {
        Dao<LocationModel, Long> dao = getDao();
        return dao.update(location);
    }


    public static int delete(long id) throws SQLException {
        Dao<LocationModel, Long> dao = getDao();
        return dao.deleteById(id);
    }


    public static int deleteAll() throws SQLException {
        Dao<LocationModel, Long> dao = getDao();
        // remove all current day weather models
        return dao.delete(dao.queryForAll());
    }
}
