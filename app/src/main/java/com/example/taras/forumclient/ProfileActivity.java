package com.example.taras.forumclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Delete;
import com.example.taras.forumclient.Api.API;
import com.example.taras.forumclient.Model.Article;
import com.example.taras.forumclient.Model.Profile;
import com.example.taras.forumclient.Model.User;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private Button buttonGetArticles;
    private ListView lvArticles;
    private AppCompatActivity myActivity;

    private SimpleCursorAdapter adapterTodo;
    private LoginActivity.Error error;
    private Retrofit restAdapter;
    private API service;

    Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myActivity = this;

        profile = new Profile(getIntent().getStringExtra("token"),new User(getIntent().getStringExtra("user_id"),
                getIntent().getStringExtra("name"),getIntent().getStringExtra("email")));
        //Log.v("intent",getIntent().getStringExtra("user_id"));
        lvArticles = (ListView) findViewById(R.id.lvArticles);
        tvName = (TextView) findViewById(R.id.tvName);
        buttonGetArticles = (Button) findViewById(R.id.buttonGetArticles);

        tvName.setText(profile.getUser().getName());
        buttonGetArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArticles();

            }
        });

        restAdapter = new Retrofit.Builder()
                .baseUrl(LoginActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = restAdapter.create(API.class);

        ActiveAndroid.initialize(this);

         adapterTodo = new SimpleCursorAdapter(this,
                R.layout.articles_list_item, null,
                new String[] { Article.USER_ID },
                new int[] { R.id.tvTextArticle },
                0);
        lvArticles.setAdapter(adapterTodo);
        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle cursor) {
                return new CursorLoader(myActivity,
                        ContentProvider.createUri(Article.class, null),
                        null, null, null, null
                );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                ((SimpleCursorAdapter)lvArticles.getAdapter()).swapCursor(cursor);
                Log.v("finished","1");
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                ((SimpleCursorAdapter)lvArticles.getAdapter()).swapCursor(null);
            }
        });
    }


    private void getArticles(){

        Call<List<Article>> call = service.getArticles(profile.getUser().getId() ,profile.getToken());
        call.enqueue(new Callback<List<Article>>() {

            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                Log.v("retrofit articles",response.code()+"");

                if(response.isSuccessful()){
                    error = LoginActivity.Error.NO_ERROR;
                    List<Article> listArticle = response.body();


                    List<Article> listAll = Article.getAll();
                    for (int i=0; i<listAll.size(); i++){
                        listAll.get(i).delete();
                    }


                    for (int i=0; i<listArticle.size(); i++){
                        listArticle.get(i).save();

                    }

                    for (int i=0; i<Article.getAll().size(); i++){
                        Log.v("db get",Article.getAll().get(i).getText());
                    }
                }
                else{
                     ShowPopUpError(error.getCode());
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Log.v("retrofit token",t.getMessage()+"");
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
