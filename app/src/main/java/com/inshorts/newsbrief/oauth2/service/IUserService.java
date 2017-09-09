package com.inshorts.newsbrief.oauth2.service;

import com.inshorts.newsbrief.oauth2.response.UserResponse;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public interface IUserService {

    @GET("/user")
    void user(Callback<UserResponse> userResponseCallback);
}
