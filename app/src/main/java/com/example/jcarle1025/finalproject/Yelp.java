package com.example.jcarle1025.finalproject;

import android.content.Context;

import org.json.JSONException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Created by jcarle1025 on 5/6/16.
 * service builder allows for access to yelp search api
 */
public class Yelp {
    public static String consumerKey = "qdTlpelJiDJPbN6mMjRiQQ";
    public static String consumerSecret = "uanVOvthneaLUBpN-QlO56Z6sMg";
    public static String token = "nm4PzNthaaoCokiiNH-VeQXNazh3QRw4";
    public static String tokenSecret = "klj0zgK5o-1F0terJl6rk8EsosI";

    OAuthService service;
    Token accessToken;

    public static Yelp getYelp(Context context) {
        return new Yelp(consumerKey,  consumerSecret, token, tokenSecret);
    }

    //Setup the Yelp API OAuth credentials.
    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    public String search(String term, double latitude, double longitude) throws JSONException {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }
}