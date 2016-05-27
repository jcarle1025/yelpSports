package com.example.jcarle1025.finalproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jcarle1025 on 5/5/16.
 * parses json information based on user's search, and fills what
 * it gathers in restaurant objects to be used by the rest of the app
 */
public class YelpJSONParser {
    public static ArrayList<Restaurant> getRestaurants(JSONObject jObj) throws JSONException {
        ArrayList<Restaurant> all = new ArrayList<Restaurant>();

        JSONArray rests = jObj.getJSONArray("businesses");

        for(int i=0; i<rests.length(); i++) {
            Restaurant r = new Restaurant();
            JSONObject rNow = rests.getJSONObject(i);
            r.setRating(getInt("rating", rNow));
            try {
                r.setMobileSite(getString("mobile_url", rNow));
            } catch (Exception e){e.printStackTrace();}
            r.setName(getString("name", rNow));
            r.setWebsite(getString("url", rNow));
            r.setPhone("");
            try {
                r.setPhone(getString("display_phone", rNow));
            } catch (Exception e){e.printStackTrace();}

            Location l = new Location();
            JSONObject lObj = getObject("location", rNow);
            l.setCity(getString("city", lObj));
            r.setCity(getString("city", lObj));
            JSONArray add = lObj.getJSONArray("display_address");
            String addressString = new String();

            for(int a=0;a<add.length();a++)
                addressString+=add.get(a) + "\n";

            r.setAddress(addressString);

            JSONObject coord = lObj.getJSONObject("coordinate");
            l.setLatitude(getFloat("latitude",coord));
            r.setLatitude(getFloat("latitude",coord));
            l.setLongitude(getFloat("longitude", coord));
            r.setLongitude(getFloat("longitude", coord));
            r.setSnippet(getString("snippet_text", rNow));
            r.snippetIcon = ((new HttpClient().getImage(rNow.optString("snippet_image_url"))));
            r.iconData = ((new HttpClient().getImage(rNow.optString("rating_img_url_large"))));

            r.setLocation(l);
            all.add(r);
        }
        return all;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}