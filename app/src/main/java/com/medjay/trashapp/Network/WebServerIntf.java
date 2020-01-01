package com.medjay.trashapp.Network;


import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Hello;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebServerIntf {

    @GET("/hello")
    Call<List<Hello>> getHelloWorld();

    @GET("/challenges")
    Call<List<Challenge>> getChallenges();
}
