package cz.stepansanda.weather.android.client.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.stepansanda.weather.android.database.model.WeatherModel;

/**
 * Created by a111 on 24.05.15.
 */
public class CurrentWeatherParser {

    /**
     * Parse current weather from json
     *
     * @param jsonObject current weather json response
     * @return parsed current weather object instance
     * @throws JSONException exception during json response parsing
     */
    public WeatherModel parse(JSONObject jsonObject) throws JSONException {
        JSONObject mainJson = jsonObject.getJSONObject("main");
        JSONArray weatherJsonArray = jsonObject.getJSONArray("weather");
        JSONObject weatherJson = weatherJsonArray.getJSONObject(0);
        JSONObject windJson = jsonObject.getJSONObject("wind");
        JSONObject sysJson = jsonObject.getJSONObject("sys");

        float temperature;
        String icon;
        float windSpeed;
        float windDegrees;
        String cityName;
        String country;
        Date date;
        String weatherCondition;
        String weatherConditionDescription;
        int humidity;
        float pressure;
        float rain = 0;

        // parse data
        cityName = jsonObject.getString("name");
        date = new Date();
        temperature = (float) mainJson.getDouble("temp");
        pressure = (float) mainJson.getDouble("pressure");
        humidity = mainJson.getInt("humidity");
        weatherCondition = weatherJson.getString("main");
        weatherConditionDescription = weatherJson.getString("description");
        icon = weatherJson.getString("icon");
        windSpeed = (float) windJson.getDouble("speed");
        windDegrees = (float) windJson.getDouble("deg");
        country = sysJson.getString("country");
        if (jsonObject.has("rain")) {
            JSONObject rainJson = jsonObject.getJSONObject("rain");
            if (rainJson.has("3h")) {
                rain = (float) rainJson.getDouble("3h");
            }
        }

        return new WeatherModel(temperature, icon, windSpeed, windDegrees, cityName, country,
                date.getTime(), weatherCondition, weatherConditionDescription, humidity, pressure,
                rain, false, true);
    }

}
