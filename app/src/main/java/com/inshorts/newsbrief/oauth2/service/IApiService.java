package com.inshorts.newsbrief.oauth2.service;

import com.inshorts.newsbrief.oauth2.request.ApiRequest;
import com.inshorts.newsbrief.oauth2.response.ApiResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public interface IApiService {


    @Headers({
            "Content-type : application/json",
            "Authorization: Bearer vZFxTvBVMD9zTZdCiAX2B2iVRDyynM"
    })
    @POST("/recommend/api/rating/")
    void Api(@Body ApiRequest apiRequest,
             Callback<ApiResponse> ResponseCallback);


}
