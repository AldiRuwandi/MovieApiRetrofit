package com.example.movieapiretrofit.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.movieapiretrofit.R;
import com.example.movieapiretrofit.adapter.MainAdapter;
import com.example.movieapiretrofit.model.MovieModel;
import com.example.movieapiretrofit.nointernet.InternetReceiver;
import com.example.movieapiretrofit.retrofit.Constant;
import com.example.movieapiretrofit.retrofit.MovieService;
import com.example.movieapiretrofit.retrofit.RetrofitInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MovieService service = RetrofitInstance.getUrl().create(MovieService.class);
    private String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    private ProgressBar progressBarNextPage;
    private NestedScrollView scrollView;
    private FloatingActionButton fab_profile;

    private MainAdapter adapter;
    private List<MovieModel.Results> movies = new ArrayList<>();

    InternetReceiver internetReceiver = new InternetReceiver();

    private String movieCategory = "";
    private Boolean isScrolling = false;
    private int currentPage = 1;
    private int totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupView();
        setupListener();
        setupRecyclerView();

        fab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetReceiver, filter);
        super.onStart();
        if (movieCategory.equals("")){
            movieCategory = Constant.POPULAR;
        }
        getMovie();
        showLoadingNextPage(false);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(internetReceiver);
        super.onStop();
    }

    private void setupView() {
        recyclerView = findViewById(R.id.list_movie);
        progressBar = findViewById(R.id.progress_loading);
        scrollView = findViewById(R.id.scroll_view);
        progressBarNextPage = findViewById(R.id.progress_loading_next_page);
        fab_profile = findViewById(R.id.fab_profile);
    }

    private void setupListener(){
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //untuk mendeteksi apa kah kita sudah menscroll sampe mentok kebawah
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    if (!isScrolling){
                        if (currentPage <= totalPage){
                            getMovieNextPage();
                        }
                    }
                }
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new MainAdapter(movies, this, new MainAdapter.AdapterListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getMovie(){

        scrollView.scrollTo(0, 0);
        currentPage = 1;
        showLoading(true);

        Call<MovieModel> call = null;
        switch (movieCategory){
            case Constant.POPULAR:
                call = service.getPopular(Constant.API_KEY, Constant.LANGUAGE, String.valueOf(currentPage));
                break;
            case Constant.NOW_PLAYING:
                call = service.getNowPlaying(Constant.API_KEY, Constant.LANGUAGE, String.valueOf(currentPage));
                break;
        }
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                showLoading(false);

                if (response.isSuccessful()){
                    MovieModel movie = response.body();
//                    List<MovieModel.Results> results = movieModel.getResults();
//                    Log.d(TAG, results.toString());
                    showMovie(movie);
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                showLoading(false);
                Log.d(TAG, t.toString());
            }
        });
    }

    private void getMovieNextPage(){

        currentPage += 1;
        showLoadingNextPage(true);

        Call<MovieModel> call = null;
        switch (movieCategory){
            case Constant.POPULAR:
                call = service.getPopular(Constant.API_KEY, Constant.LANGUAGE, String.valueOf(currentPage));
                break;
            case Constant.NOW_PLAYING:
                call = service.getNowPlaying(Constant.API_KEY, Constant.LANGUAGE, String.valueOf(currentPage));
                break;
        }
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                showLoadingNextPage(false);

                if (response.isSuccessful()){
                    MovieModel movie = response.body();
//                    List<MovieModel.Results> results = movieModel.getResults();
//                    Log.d(TAG, results.toString());
                    showMovieNextPage(movie);
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                showLoadingNextPage(false);
                Log.d(TAG, t.toString());
            }
        });
    }

    private void showLoading(Boolean loading){
        if (loading){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showLoadingNextPage(Boolean loading){
        if (loading){
            isScrolling = true;
            progressBarNextPage.setVisibility(View.VISIBLE);
        }else{
            isScrolling = false;
            progressBarNextPage.setVisibility(View.GONE);
        }
    }

    private void showMovie(MovieModel movie){
        totalPage = movie.getTotal_pages();
        movies = movie.getResults();
        adapter.setData(movies);
    }

    private void showMovieNextPage(MovieModel movie){
        totalPage = movie.getTotal_pages();
        movies = movie.getResults();
        adapter.setDataNextPage(movies);
        showMessage("Page " + currentPage + " loaded");
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            getSupportActionBar().setTitle("POPULAR");
            movieCategory = Constant.POPULAR;
            getMovie();
            return true;
        }else if (id == R.id.action_now_playing){
            getSupportActionBar().setTitle("NOW PLAYING");
            movieCategory = Constant.NOW_PLAYING;
            getMovie();
            return true;
        }

        if (id == R.id.action_grid_view) {
            layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            getMovie();
        }

        if (id == R.id.action_list_view) {
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            getMovie();
        }

        return super.onOptionsItemSelected(item);
    }
}