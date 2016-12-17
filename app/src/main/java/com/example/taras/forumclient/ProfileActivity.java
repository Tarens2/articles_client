package com.example.taras.forumclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taras.forumclient.Api.API;
import com.example.taras.forumclient.Model.Article;
import com.example.taras.forumclient.Model.Profile;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private Button buttonGetArticles;
    private TextView tvArticles;
    private AppCompatActivity myActivity;


    private LoginActivity.Error error;
    private Retrofit restAdapter;
    private API service;

    Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myActivity = this;
    }


    private void getToken(String email, String password){
        Call<List<Article>> call = service.getArticles(profile.getUser().getId(),profile.getToken());
        call.enqueue(new Callback<List<Article>>() {

            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                Log.v("retrofit token",response.code()+"");

                if(response.isSuccessful()){
                    error = LoginActivity.Error.NO_ERROR;
                    List<Article> profile = response.body();
                }
                else{
                     ShowPopUpError(error.getCode());
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {

                if(!LoginActivity.hasConnection(myActivity)){
                    ShowPopUpError(651);
                }
                else{
                    ShowPopUpError(405);
                }
            }
        });
    }
    public void ShowPopUpError(int code)
    {

        error = LoginActivity.Error.fromCode(code);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, error.getText(), duration);
        toast.show();
    }

}
