package cz.stepansanda.weather.android;

/**
 * Created by a111 on 24.05.15.
 */
public class WeatherConfig {
    // api keys
    public static final String WEATHER_API_KEY = "183e567060e31e21878807e47cc30e4a";
    public static final String FLICKR_API_KEY = "ec2a0332be7d106f1af30317330c359b";

    // server endpoints
    public static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5";
    public static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/%s.png";
    public static final String FLICKR_LOCATION_IMAGE_URL = "http://farm6.staticflickr.com/%s/%s_%s_z.jpg";
    public static final String FLICKR_SEARCH_WEATHER_IMAGE_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=%s&text=%s&sort=relevance&per_page=1&format=json&nojsoncallback=1";
    public static final String FLICKR_SEARCH_LOCATION_IMAGE_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=%s&text=%s&sort=relevance&accuracy=3&lat=%s&lon=%s&per_page=1&format=json&nojsoncallback=1";

    // DEBUG
    public static final boolean LOGS = true;
}
