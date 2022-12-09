package dhcnhn.aduc8386.nixflet.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.adapter.CastAdapter;
import dhcnhn.aduc8386.nixflet.controller.adapter.MovieAdapter;
import dhcnhn.aduc8386.nixflet.controller.adapter.UserCommentAdapter;
import dhcnhn.aduc8386.nixflet.controller.adapter.VideoAdapter;
import dhcnhn.aduc8386.nixflet.helper.FirebaseDatabaseHelper;
import dhcnhn.aduc8386.nixflet.helper.SharedPreferencesHelper;
import dhcnhn.aduc8386.nixflet.model.Cast;
import dhcnhn.aduc8386.nixflet.model.MovieDetail;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;
import dhcnhn.aduc8386.nixflet.model.User;
import dhcnhn.aduc8386.nixflet.model.UserComment;
import dhcnhn.aduc8386.nixflet.service.TMDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private String movieId;
    private boolean isMovie;

    private ImageView imageViewBackdrop;
    private ImageView imageViewPoster;
    private ImageView imageViewSendButton;
    private ImageView imageViewUserProfile;
    private TextView textViewName;
    private TextView textViewGenres;
    private TextView textViewOverview;
    private EditText editTextUserComment;
    private RecyclerView recyclerViewCasts;
    private RecyclerView recyclerViewVideo;
    private RecyclerView recyclerRecommendations;
    private RecyclerView recyclerUserComment;
    private ProgressBar progressBar;

    private CastAdapter castAdapter;
    private MovieAdapter movieAdapter;
    private VideoAdapter videoAdapter;
    private UserCommentAdapter userCommentAdapter;

    private List<MovieResponse> recommendations;
    private List<Cast> casts;
    private List<UserComment> userComments;

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
        imageViewSendButton = findViewById(R.id.imageview_movie_detail_send);
        imageViewUserProfile = findViewById(R.id.circle_imageview_movie_detail_user_profile);
        textViewName = findViewById(R.id.textview_movie_detail_movie_name);
        textViewGenres = findViewById(R.id.textview_movie_detail_movie_genre);
        textViewOverview = findViewById(R.id.textview_movie_detail_movie_overview);
        editTextUserComment = findViewById(R.id.edittext_movie_detail_user_comment);
        recyclerViewCasts = findViewById(R.id.recyclerview_movie_detail_cast);
        recyclerViewVideo = findViewById(R.id.recyclerview_movie_detail_video);
        recyclerRecommendations = findViewById(R.id.recyclerview_movie_detail_recommendation);
        recyclerUserComment = findViewById(R.id.recyclerview_movie_detail_user_comments);
        progressBar = findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.VISIBLE);

        casts = new ArrayList<>();
        recommendations = new ArrayList<>();

        Glide.with(this)
                .load(SharedPreferencesHelper.getUserProfilePicture())
                .error(R.drawable.user_avatar_holder)
                .centerCrop()
                .into(imageViewUserProfile);

        if (isMovie) {
            getMovieDetail();
        } else {
            getTVShowDetail();
        }

        getMovieComment();

        imageViewSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editTextUserComment.getText().toString().trim();
                User user = SharedPreferencesHelper.getUser();


                UserComment userComment = new UserComment(user, comment);

                String path = String.format("%s/%s", movieId, userComment.getId());
                FirebaseDatabaseHelper.getMovieReference().child(path).setValue(userComment, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        editTextUserComment.setText("");
                    }
                });
            }
        });

    }

    private void getMovieComment() {
        FirebaseDatabaseHelper.getSpecificMovieReference(movieId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userComments = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    UserComment userComment = child.getValue(UserComment.class);
                    userComments.add(userComment);
                }

                for (int i = 0; i < userComments.size(); i++) {
                    for (int j = i + 1; j < userComments.size() - 1; j++) {
                        if (userComments.get(i).getId() > userComments.get(j).getId()) {
                            UserComment temp = userComments.get(i);
                            userComments.set(i, userComments.get(j));
                            userComments.set(j, temp);
                        }
                    }
                }
                userCommentAdapter = new UserCommentAdapter(userComments);
                recyclerUserComment.setAdapter(userCommentAdapter);
                recyclerUserComment.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.VERTICAL, false));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        recommendations.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movieAdapter = new MovieAdapter(recommendations, new MovieAdapter.OnMovieClickListener() {
                        @Override
                        public void onMovieClick(MovieResponse movieResponse) {
                            Intent intent = new Intent(MovieDetailActivity.this, MovieDetailActivity.class);

                            intent.putExtra(MainActivity.MOVIE_ID, movieResponse.getId());
                            intent.putExtra(MainActivity.IS_MOVIE, movieResponse.getTitle() != null);

                            startActivity(intent);
                        }
                    });

                    recyclerRecommendations.setAdapter(movieAdapter);
                    recyclerRecommendations.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    progressBar.setVisibility(View.GONE);
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
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        recommendations.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movieAdapter = new MovieAdapter(recommendations, new MovieAdapter.OnMovieClickListener() {
                        @Override
                        public void onMovieClick(MovieResponse movieResponse) {
                            Intent intent = new Intent(MovieDetailActivity.this, MovieDetailActivity.class);

                            intent.putExtra(MainActivity.MOVIE_ID, movieResponse.getId());
                            intent.putExtra(MainActivity.IS_MOVIE, movieResponse.getTitle() != null);

                            startActivity(intent);
                        }
                    });

                    recyclerRecommendations.setAdapter(movieAdapter);
                    recyclerRecommendations.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}