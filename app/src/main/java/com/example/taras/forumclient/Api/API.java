package com.example.taras.forumclient.Api;


import com.example.taras.forumclient.Model.Article;
import com.example.taras.forumclient.Model.Profile;
import com.example.taras.forumclient.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by User on 10.12.2016.
 */

public interface API {
    @GET("auth/me")
    Call<Profile> getMe(@Query("token") String token);

    @POST("auth")
    Call<Profile> getToken(@Query("email") String email,@Query("password") String password);

    @GET("articles/{user_id}")
    Call<List<Article>> getArticles(@Path("user_id") String user_id, @Query("token") String token);
}
