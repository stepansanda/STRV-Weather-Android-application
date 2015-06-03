package cz.stepansanda.weather.android.client.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.stepansanda.weather.android.entity.FlickrImageEntity;

/**
 * Created by a111 on 29.05.15.
 */
public class FlickrImageParser {

    /**
     * Parse location images from json
     *
     * @param jsonObject location image json response
     * @return parsed location image object instance
     * @throws JSONException exception during json response parsing
     */
    public FlickrImageEntity parse(JSONObject jsonObject) throws JSONException {
        JSONObject photo = jsonObject.getJSONObject("photos");
        JSONArray photos = photo.getJSONArray("photo");
        if (photos.length() > 0) {
            JSONObject firstPhoto = photos.getJSONObject(0);
            String id = firstPhoto.getString("id");
            String owner = firstPhoto.getString("owner");
            String secret = firstPhoto.getString("secret");
            String server = firstPhoto.getString("server");

            return new FlickrImageEntity(id, owner, secret, server);
        } else {
            return null;
        }
    }
}
