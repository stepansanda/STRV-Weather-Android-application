package cz.stepansanda.weather.android.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import cz.stepansanda.weather.android.database.WeatherDatabaseHelper;
import cz.stepansanda.weather.android.database.model.WeatherModel;

/**
 * Created by a111 on 24.05.15.
 */
public class ForecastDao {

    private static Dao<WeatherModel, Long> getDao() throws SQLException {
        WeatherDatabaseHelper databaseHelper = WeatherDatabaseHelper.getInstance();
        return databaseHelper.getWeatherDao();
    }


    public static int refresh(WeatherModel weather) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.refresh(weather);
    }


    public static int replace(List<WeatherModel> forecast, String cityName, boolean isCurrentPosition) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        int returnState = 99;
        // get all current weather with the same city name or current location
        QueryBuilder<WeatherModel, Long> qb = dao.queryBuilder();
        Where where = qb.where();
        if (isCurrentPosition) {
            // only current position
            where.eq(WeatherModel.COLUMN_IS_CURRENT_POSITION, Boolean.TRUE);
        } else {
            // only current position
            where.eq(WeatherModel.COLUMN_IS_CURRENT_POSITION, Boolean.FALSE);
            where.and();
            // city name equals
            where.eq(WeatherModel.COLUMN_CITY_NAME, cityName);
        }
        // and
        where.and();
        // only forecast data
        where.eq(WeatherModel.COLUMN_IS_FORECAST, Boolean.TRUE);
        PreparedQuery<WeatherModel> preparedQuery = qb.prepare();
        List<WeatherModel> modelsToRemove = dao.query(preparedQuery);
        // remove current day weather models
        dao.delete(modelsToRemove);
        // add newest version
        for (WeatherModel w : forecast) {
            w.setIsCurrentPositon(isCurrentPosition);
            w.setCityName(cityName);
            returnState = Math.min(dao.create(w), returnState);
        }
        return returnState;
    }


    public static int create(WeatherModel weather) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.create(weather);
    }


    public static WeatherModel read(long id) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.queryForId(id);
    }


    public static List<WeatherModel> readAllCurrentLocation() throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        // get all current weather with the same city name or current location
        QueryBuilder<WeatherModel, Long> qb = dao.queryBuilder();
        Where where = qb.where();
        // only current position
        where.eq(WeatherModel.COLUMN_IS_CURRENT_POSITION, Boolean.TRUE);
        // and
        where.and();
        // only forecast data
        where.eq(WeatherModel.COLUMN_IS_FORECAST, Boolean.TRUE);
        PreparedQuery<WeatherModel> preparedQuery = qb.prepare();
        return dao.query(preparedQuery);
    }


    public static List<WeatherModel> readAllSelectedLocation(String locationName) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        // get all current weather with the same city name or current location
        QueryBuilder<WeatherModel, Long> qb = dao.queryBuilder();
        Where where = qb.where();
        // only selected location
        where.eq(WeatherModel.COLUMN_CITY_NAME, locationName);
        // and
        where.and();
        // only forecast data
        where.eq(WeatherModel.COLUMN_IS_FORECAST, Boolean.TRUE);
        PreparedQuery<WeatherModel> preparedQuery = qb.prepare();
        return dao.query(preparedQuery);
    }


    public static List<WeatherModel> readAll(String cityName) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        // get all current weather with the same city name or current location
        QueryBuilder<WeatherModel, Long> qb = dao.queryBuilder();
        Where where = qb.where();
        // only current position
        where.eq(WeatherModel.COLUMN_CITY_NAME, cityName);
        // and
        where.and();
        // only forecast data
        where.eq(WeatherModel.COLUMN_IS_FORECAST, Boolean.TRUE);
        PreparedQuery<WeatherModel> preparedQuery = qb.prepare();
        return dao.query(preparedQuery);
    }


    public static int update(WeatherModel weather) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.update(weather);
    }


    public static int delete(long id) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.deleteById(id);
    }


    public static int deleteAll() throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        List<WeatherModel> modelsToRemove = dao.queryForEq(WeatherModel.COLUMN_IS_FORECAST, Boolean.TRUE);
        // remove all current day weather models
        return dao.delete(modelsToRemove);
    }
}
