package dhcnhn.aduc8386.nixflet.controller.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.adapter.CastAdapter;
import dhcnhn.aduc8386.nixflet.controller.adapter.MovieAdapter;
import dhcnhn.aduc8386.nixflet.controller.adapter.VideoAdapter;
import dhcnhn.aduc8386.nixflet.model.Cast;
import dhcnhn.aduc8386.nixflet.model.MovieDetail;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;
import dhcnhn.aduc8386.nixflet.service.TMDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private String movieId;
    private boolean isMovie;

    private ImageView imageViewBackdrop;
    private ImageView imageViewPoster;
    private TextView textViewName;
    private TextView textViewGenres;
    private TextView textViewOverview;
    private RecyclerView recyclerViewCasts;
    private RecyclerView recyclerViewVideo;
    private RecyclerView recyclerRecommendations;
    private RecyclerView recyclerUserComment;

    private CastAdapter castAdapter;
    private MovieAdapter movieAdapter;
    private VideoAdapter videoAdapter;
    private List<MovieResponse> recommendations;
    private List<Cast> casts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            movieId = bundle.getString(MainActivity.MOVIE_ID);
            isMovie = bundle.getBoolean(MainActivity.IS_MOVIE);
        } else {
            movieId = "No id found";
        }

        bindView();

    }

    private void bindView() {
        imageViewPoster = findViewById(R.id.imageview_movie_detail_movie_poster);
        imageViewBackdrop = findViewById(R.id.imageview_movie_detail_movie_backdrop);
        textViewName = findViewById(R.id.textview_movie_detail_movie_name);
        textViewGenres = findViewById(R.id.textview_movie_detail_movie_genre);
        textViewOverview = findViewById(R.id.textview_movie_detail_movie_overview);
        recyclerViewCasts = findViewById(R.id.recyclerview_movie_detail_cast);
        recyclerViewVideo = findViewById(R.id.recyclerview_movie_detail_video);
        recyclerRecommendations = findViewById(R.id.recyclerview_movie_detail_recommendation);
        recyclerUserComment = findViewById(R.id.recyclerview_movie_detail_user_comments);

        if (isMovie) {
            getMovieDetail();
        } else {
            getTVShowDetail();
        }

    }

    private void getMovieDetail() {
        TMDBService.init().getMovieDetail(movieId).enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.body() != null && response.isSuccessful()) {
                    MovieDetail movieDetail = response.body();

                    Glide.with(MovieDetailActivity.this)
                            .load(String.format("https://image.tmdb.org/t/p/original/%s", movieDetail.getBackdropPath()))
                            .error(R.drawable.ic_nixflet_text)
                            .centerCrop()
                            .into(imageViewBackdrop);

                    Glide.with(MovieDetailActivity.this)
                            .load(String.format("https://image.tmdb.org/t/p/original/%s", movieDetail.getPosterPath()))
                            .error(R.drawable.ic_nixflet_text)
                            .centerCrop()
                            .into(imageViewPoster);

                    textViewName.setText(movieDetail.getTitle());
                    textViewOverview.setText(String.format("%s", movieDetail.getOverview()));

                    StringBuilder genres = new StringBuilder();
                    for (int i = 0; i < movieDetail.getGenres().length; i++) {
                        if (i < movieDetail.getGenres().length - 1) {
                            genres.append(String.format("%s ⬩ ", movieDetail.getGenres()[i].getName()));
                        } else {
                            genres.append(String.format("%s", movieDetail.getGenres()[i].getName()));
                        }
                    }

                    getMovieCast();
                    getMovieRecommendations();

                    textViewGenres.setText(genres);

                    videoAdapter = new VideoAdapter(movieDetail.getVideos().getVideos());
                    recyclerViewVideo.setAdapter(videoAdapter);
                    recyclerViewVideo.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {

            }
        });
    }

    private void getMovieCast() {
        TMDBService.init().getMovieCast(movieId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("cast");
                    casts = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        casts.add(gson.fromJson(result.get(i), Cast.class));
                    }

                    castAdapter = new CastAdapter(casts);

                    recyclerViewCasts.setAdapter(castAdapter);
                    recyclerViewCasts.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getTVShowDetail() {
        TMDBService.init().getTVShowDetail(movieId).enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.body() != null && response.isSuccessful()) {
                    MovieDetail movieDetail = response.body();

                    Glide.with(MovieDetailActivity.this)
                            .load(String.format("https://image.tmdb.org/t/p/original/%s", movieDetail.getBackdropPath()))
                            .error(R.drawable.ic_nixflet_text)
                            .centerCrop()
                            .into(imageViewBackdrop);

                    Glide.with(MovieDetailActivity.this)
                            .load(String.format("https://image.tmdb.org/t/p/original/%s", movieDetail.getPosterPath()))
                            .error(R.drawable.ic_nixflet_text)
                            .centerCrop()
                            .into(imageViewPoster);

                    textViewName.setText(movieDetail.getName());
                    textViewOverview.setText(String.format("%s", movieDetail.getOverview()));

                    StringBuilder genres = new StringBuilder();
                    for (int i = 0; i < movieDetail.getGenres().length; i++) {
                        if (i < movieDetail.getGenres().length - 1) {
                            genres.append(String.format("%s ⬩ ", movieDetail.getGenres()[i].getName()));
                        } else {
                            genres.append(String.format("%s", movieDetail.getGenres()[i].getName()));
                        }
                    }

                    getTVShowCast();
                    getTVShowRecommendations();

                    textViewGenres.setText(genres);

                    videoAdapter = new VideoAdapter(movieDetail.getVideos().getVideos());
                    recyclerViewVideo.setAdapter(videoAdapter);
                    recyclerViewVideo.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {

            }
        });
    }

    private void getTVShowCast() {
        TMDBService.init().getTVShowCast(movieId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("cast");
                    casts = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        casts.add(gson.fromJson(result.get(i), Cast.class));
                    }

                    castAdapter = new CastAdapter(casts);

                    recyclerViewCasts.setAdapter(castAdapter);
                    recyclerViewCasts.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getMovieRecommendations() {
        TMDBService.init().getMovieRecommendations(movieId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    recommendations = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        recommendations.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movieAdapter = new MovieAdapter(recommendations, new MovieAdapter.OnMovieClickListener() {
                        @Override
                        public void onMovieClick(MovieResponse movieResponse) {

                        }
                    });

                    recyclerRecommendations.setAdapter(movieAdapter);
                    recyclerRecommendations.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getTVShowRecommendations() {
        TMDBService.init().getTVShowRecommendations(movieId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    recommendations = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        recommendations.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movieAdapter = new MovieAdapter(recommendations, new MovieAdapter.OnMovieClickListener() {
                        @Override
                        public void onMovieClick(MovieResponse movieResponse) {

                        }
                    });

                    recyclerRecommendations.setAdapter(movieAdapter);
                    recyclerRecommendations.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}