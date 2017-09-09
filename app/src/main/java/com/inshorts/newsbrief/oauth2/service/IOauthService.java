package com.inshorts.newsbrief.oauth2.service;

import com.inshorts.newsbrief.oauth2.request.AccessTokenRequest;
import com.inshorts.newsbrief.oauth2.response.AccessTokenResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by dominicneeraj on 08/08/17.
 */

public interface IOauthService {

    @POST("/auth/token/")
    void getAccessToken(@Body AccessTokenRequest accessTokenRequest,
                        Callback<AccessTokenResponse> responseCallback);


}
