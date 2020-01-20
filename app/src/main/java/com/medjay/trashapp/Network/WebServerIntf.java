package com.medjay.trashapp.Network;


import com.medjay.trashapp.entities.Challenge;
import com.medjay.trashapp.entities.Client;
import com.medjay.trashapp.entities.Comment;
import com.medjay.trashapp.entities.Note;
import com.medjay.trashapp.entities.Photo;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServerIntf {

    @POST("/client")// http://domain.com/api/register for exemple
    Call<Client>register(@Body Client client);

    @GET("/login/{username},{password}")
    Call<Client>login(@Path ("username") String username,
                            @Path ("password") String password);

    @POST("/photo/{id_challenge}")
    Call<Photo>PostPhotos(@Body Photo photo,
                          @Path ("id_challenge") int id_challenge);

    @POST("/challenge/{id_user}")
    Call<Challenge>PostChallenge(@Body Challenge challenge,
                           @Path ("id_user") int id_user);

    @POST("/comment/{id_user}/{id_challenge}")
    Call<Comment>PostComment(@Body Comment comment,
                                 @Path ("id_user") int id_user,
                               @Path("id_challenge") int id_challenge);

    @POST("/note/{id_challenge}/{id_user}")
    Call<Note>PostNote(@Body Note note,
                       @Path("id_challenge") int id_challenge,
                             @Path ("id_user") int id_user);

    @POST("/challenges/{id_challenge}/{id_user}")
    Call<String>participate(@Path ("id_user") int id_user,
                       @Path("id_challenge") int id_challenge);

    @GET("/challenges")
    Call<List<Challenge>> getChallenges();

    @GET("/challenges/{id}")
    Call<Challenge> getChallenge(@Path("id") int id_challenge);

    @GET("/participants/{id_challenge}")
    Call<Set<Client>> getParticipents(@Path("id_challenge") int id_challenge);
}
