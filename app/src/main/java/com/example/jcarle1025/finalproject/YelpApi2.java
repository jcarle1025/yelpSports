package com.example.jcarle1025.finalproject;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Created by jcarle1025 on 5/6/16.
 */
public class YelpApi2 extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

}
