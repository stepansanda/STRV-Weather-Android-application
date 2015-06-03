package cz.stepansanda.weather.android.client.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cz.stepansanda.weather.android.database.model.WeatherModel;
import cz.stepansanda.weather.android.utility.UnitConverterHelper;

/**
 * Created by a111 on 24.05.15.
 */
public class ForecastParser {

    /**
     * Parse forecast weather from json
     *
     * @param jsonObject forecast weather json response
     * @return parsed forecast weather object instance
     * @throws JSONException exception during json response parsing
     */
    public List<WeatherModel> parse(JSONObject jsonObject) throws JSONException {
        List<WeatherModel> forecast = new LinkedList<>();

        JSONObject cityJson = jsonObject.getJSONObject("city");
        JSONArray forecastJsonArray = jsonObject.getJSONArray("list");

        String cityName = cityJson.getString("name");
        String country = cityJson.getString("country");

        // TODO parse coordinates

        // parse weather forecast
        for (int i = 0; i < forecastJsonArray.length(); i++) {
            forecast.add(parseWeather(forecastJsonArray.getJSONObject(i), cityName, country));
        }

        return forecast;
    }


    /**
     * Parse weather model from current position in forecast
     *
     * @param jsonObject weather json object
     * @return parsed instance of weather model
     * @throws JSONException exception during weather json parsing
     */
    private WeatherModel parseWeather(JSONObject jsonObject, String cityName, String country)
            throws JSONException {
        JSONObject temperatureJson = jsonObject.getJSONObject("temp");
        JSONArray weatherJsonArray = jsonObject.getJSONArray("weather");
        JSONObject weatherJson = weatherJsonArray.getJSONObject(0);

        float temperature;
        String icon;
        float windSpeed;
        float windDegrees;
        Date date;
        String weatherCondition;
        String weatherConditionDescription;
        int humidity;
        float pressure;
        float rain = 0;

        // parse data
        date = UnitConverterHelper.getDateFromUnixTime(jsonObject.getLong("dt"));
        temperature = (float) temperatureJson.getDouble("day");
        pressure = (float) jsonObject.getDouble("pressure");
        humidity = jsonObject.getInt("humidity");
        weatherCondition = weatherJson.getString("main");
        weatherConditionDescription = weatherJson.getString("description");
        icon = weatherJson.getString("icon");
        windSpeed = (float) jsonObject.getDouble("speed");
        windDegrees = (float) jsonObject.getDouble("deg");
        if (jsonObject.has("rain")) {
            rain = (float) jsonObject.getDouble("rain");
        }

        return new WeatherModel(temperature, icon, windSpeed, windDegrees, cityName, country,
                date.getTime(), weatherCondition, weatherConditionDescription, humidity, pressure,
                rain, true, true);
    }
}
