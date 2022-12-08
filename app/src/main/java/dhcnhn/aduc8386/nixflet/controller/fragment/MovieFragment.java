package dhcnhn.aduc8386.nixflet.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.activity.MainActivity;
import dhcnhn.aduc8386.nixflet.controller.activity.MovieDetailActivity;
import dhcnhn.aduc8386.nixflet.controller.adapter.CategoryAdapter;
import dhcnhn.aduc8386.nixflet.model.Category;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;
import dhcnhn.aduc8386.nixflet.service.TMDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private TextView textViewMovieName;
    private ImageView imageViewMoviePoster;

    private RecyclerView recyclerViewListMovie;

    private CategoryAdapter movieAdapter;

    private List<Category> movies;


    public MovieFragment() {
        super(R.layout.fragment_movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);

    }

    void getPopularMovies() {
        TMDBService.init().getPopularMovies().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    List<MovieResponse> movieResponses = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        movieResponses.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    textViewMovieName.setText(movieResponses.get(0).getTitle());
                    Glide.with(MovieFragment.this.getContext()).
                            load(String.format("https://image.tmdb.org/t/p/original/%s", movieResponses.get(0).getPosterPath()))
                            .centerCrop()
                            .error(R.drawable.ic_nixflet_text)
                            .into(imageViewMoviePoster);

                    movies.add(new Category("Popular", movieResponses));

                    movieAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MovieFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getTopRatedMovies() {
        TMDBService.init().getTopRatedMovies().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    List<MovieResponse> movieResponses = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        movieResponses.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movies.add(new Category("Top Rated", movieResponses));

                    movieAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MovieFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getUpcomingMovies() {
        TMDBService.init().getUpcomingMovies().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    List<MovieResponse> movieResponses = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        movieResponses.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movies.add(new Category("Upcoming", movieResponses));
                    movieAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, Throwable t) {
                Toast.makeText(MovieFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getNowPlayingMovies() {
        TMDBService.init().getNowPlayingMovies().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    List<MovieResponse> movieResponses = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        movieResponses.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movies.add(new Category("Now Playing", movieResponses));
                    movieAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MovieFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindView(View view) {
        recyclerViewListMovie = view.findViewById(R.id.recyclerview_movie_category_list);
        textViewMovieName = view.findViewById(R.id.textview_movie_movie_name);
        imageViewMoviePoster = view.findViewById(R.id.imageview_movie_movie_poster);

        movies = new ArrayList<>();
        movieAdapter = new CategoryAdapter(movies, this);
        recyclerViewListMovie.setAdapter(movieAdapter);
        recyclerViewListMovie.setLayoutManager(new LinearLayoutManager(MovieFragment.this.getActivity(), LinearLayoutManager.VERTICAL, false));

        getPopularMovies();
        getTopRatedMovies();
        getUpcomingMovies();
        getNowPlayingMovies();

    }

    @Override
    public void onCategoryClick(MovieResponse movieResponse) {

        Intent intent = new Intent(MovieFragment.this.getContext(), MovieDetailActivity.class);

        intent.putExtra(MainActivity.MOVIE_ID, movieResponse.getId());
        intent.putExtra(MainActivity.IS_MOVIE, true);

        startActivity(intent);
    }
}
