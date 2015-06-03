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
public class CurrentWeatherDao {

    private static Dao<WeatherModel, Long> getDao() throws SQLException {
        WeatherDatabaseHelper databaseHelper = WeatherDatabaseHelper.getInstance();
        return databaseHelper.getWeatherDao();
    }


    public static int refresh(WeatherModel weather) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.refresh(weather);
    }


    public static int replace(WeatherModel weather, String cityName, boolean isCurrentPosition) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        // get all current weather with the same city name or current location
        QueryBuilder<WeatherModel, Long> qb = dao.queryBuilder();
        Where where = qb.where();
        if (isCurrentPosition) {
            // only current position
            where.eq(WeatherModel.COLUMN_IS_CURRENT_POSITION, Boolean.TRUE);
        } else {
            // city name equals
            where.eq(WeatherModel.COLUMN_CITY_NAME, cityName);
        }
        // and
        where.and();
        // only current weather not forecast
        where.eq(WeatherModel.COLUMN_IS_FORECAST, Boolean.FALSE);
        PreparedQuery<WeatherModel> preparedQuery = qb.prepare();
        List<WeatherModel> modelsToRemove = dao.query(preparedQuery);
        // remove current day weather models
        dao.delete(modelsToRemove);
        weather.setCityName(cityName);
        weather.setIsCurrentPositon(isCurrentPosition);
        // add newest version
        return dao.create(weather);
    }


    public static int create(WeatherModel weather) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.create(weather);
    }


    public static WeatherModel read(long id) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.queryForId(id);
    }


    public static WeatherModel readCurrentPositionWeather() throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        QueryBuilder<WeatherModel, Long> qb = dao.queryBuilder();
        Where where = qb.where();
        // only current position
        where.eq(WeatherModel.COLUMN_IS_CURRENT_POSITION, Boolean.TRUE);
        // and
        where.and();
        // only current weather not forecast
        where.eq(WeatherModel.COLUMN_IS_FORECAST, Boolean.FALSE);
        PreparedQuery<WeatherModel> preparedQuery = qb.prepare();
        List<WeatherModel> models = dao.query(preparedQuery);
        if (models.size() == 0) {
            return null;
        } else {
            return models.get(0);
        }
    }


    public static WeatherModel readLocationWeather(String locationName) throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        QueryBuilder<WeatherModel, Long> qb = dao.queryBuilder();
        Where where = qb.where();
        // only current position
        where.eq(WeatherModel.COLUMN_IS_CURRENT_POSITION, Boolean.FALSE);
        // and
        where.and();
        // only current weather not forecast
        where.eq(WeatherModel.COLUMN_IS_FORECAST, Boolean.FALSE);
        where.and();
        where.eq(WeatherModel.COLUMN_CITY_NAME, locationName);
        PreparedQuery<WeatherModel> preparedQuery = qb.prepare();
        List<WeatherModel> models = dao.query(preparedQuery);
        if (models.size() == 0) {
            return null;
        } else {
            return models.get(0);
        }
    }


    public static List<WeatherModel> readAll() throws SQLException {
        Dao<WeatherModel, Long> dao = getDao();
        return dao.queryForEq(WeatherModel.COLUMN_IS_FORECAST, Boolean.FALSE);
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
        List<WeatherModel> modelsToRemove = dao.queryForEq(WeatherModel.COLUMN_IS_FORECAST, Boolean.FALSE);
        // remove all current day weather models
        return dao.delete(modelsToRemove);
    }
}
