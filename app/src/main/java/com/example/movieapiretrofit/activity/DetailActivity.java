package com.example.movieapiretrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapiretrofit.databinding.ActivityDetailBinding;
import com.example.movieapiretrofit.databinding.ContentDetailBinding;
import com.example.movieapiretrofit.model.DetailModel;
import com.example.movieapiretrofit.model.MovieModel;
import com.example.movieapiretrofit.retrofit.Constant;
import com.example.movieapiretrofit.retrofit.MovieService;
import com.example.movieapiretrofit.retrofit.RetrofitInstance;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.movieapiretrofit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private String TAG = "DetailActivity";
    

    private MovieService service = RetrofitInstance.getUrl().create(MovieService.class);
    
    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView txvTitle, txvVoteAverage, txvGenre, txvOverview;
    private FloatingActionButton fabTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupView();
        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Constant.MOVIE_ID != 0){
            getDetailMovie();
        }else{
            finish();
        }
    }

    private void setupView(){
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.image_backdrop);
        progressBar = findViewById(R.id.progress_loading);
        txvTitle = findViewById(R.id.text_title);
        txvVoteAverage = findViewById(R.id.text_vote);
        txvGenre = findViewById(R.id.text_genre);
        txvOverview = findViewById(R.id.text_overview);
        fabTrailer = findViewById(R.id.fab_play);
    }

    private void setupListener(){
        fabTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this, TrailerActivity.class));
            }
        });
    }

    private void getDetailMovie(){
        showLoading(true);

        Call<DetailModel> call = service.getDetail(String.valueOf(Constant.MOVIE_ID),
                Constant.API_KEY);
        call.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                Log.d(TAG, response.toString());
                showLoading(false);
                if (response.isSuccessful()){
                    DetailModel detail = response.body();
                    showMovie(detail);
                }
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                Log.d(TAG, t.toString());
                showLoading(false);
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

    private void showMovie(DetailModel detail){

        txvTitle.setText(detail.getTitle());
        txvVoteAverage.setText(detail.getVote_average().toString());
        txvOverview.setText(detail.getOverview());

        for (DetailModel.Genres genre: detail.getGenres()){
            txvGenre.setText(genre.getName() + " " );
        }

        Picasso.get()
                .load(Constant.BACKDROP_PATH + detail.getBackdrop_path())
                .placeholder(R.drawable.placeholder_landscape)//jika gambar blm terlihat akan menampilkan gambar ini
                .error(R.drawable.placeholder_landscape)
                .fit().centerCrop()
                .into(imageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}