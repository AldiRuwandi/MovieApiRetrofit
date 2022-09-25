package com.example.movieapiretrofit.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movieapiretrofit.R;
import com.example.movieapiretrofit.activity.DetailActivity;
import com.example.movieapiretrofit.adapter.MainAdapter;
import com.example.movieapiretrofit.adapter.TabAdapter;
import com.example.movieapiretrofit.databinding.FragmentPopularBinding;
import com.example.movieapiretrofit.model.MovieModel;
import com.example.movieapiretrofit.retrofit.Constant;
import com.example.movieapiretrofit.retrofit.MovieService;
import com.example.movieapiretrofit.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PopularFragment extends Fragment {

    private FragmentPopularBinding binding;

    private String TAG = "PopularFragment";

    private MovieService service = RetrofitInstance.getUrl().create(MovieService.class);
    private RecyclerView.LayoutManager layoutManager;
    private MainAdapter adapter;
    private List<MovieModel.Results> movies = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPopularBinding.inflate(inflater, container, false);
        setupRecyclerview();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovie();
    }

    private void setupRecyclerview(){
        adapter = new MainAdapter(movies, getContext(), new MainAdapter.AdapterListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(getContext(), DetailActivity.class));
            }
        });

        layoutManager = new GridLayoutManager(getContext(), 2);
        binding.listMovie.setLayoutManager(layoutManager);
        binding.listMovie.setAdapter(adapter);
    }

    private void getMovie(){
        showLoading(true);

        Call<MovieModel> call = service.getPopular(Constant.API_KEY, Constant.LANGUAGE, "1");
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                showLoading(false);

                if (response.isSuccessful()){
                    MovieModel movie = response.body();
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

    private void showLoading(Boolean loading){
        if (loading){
            binding.progressLoading.setVisibility(View.VISIBLE);
        }else{
            binding.progressLoading.setVisibility(View.GONE);
        }
    }

    private void showMovie(MovieModel movie){
        movies = movie.getResults();
        adapter.setData(movies);
    }
}