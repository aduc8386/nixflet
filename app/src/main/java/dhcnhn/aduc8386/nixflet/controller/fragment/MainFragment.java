package dhcnhn.aduc8386.nixflet.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.Random;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.activity.MainActivity;
import dhcnhn.aduc8386.nixflet.controller.activity.MovieActivity;
import dhcnhn.aduc8386.nixflet.controller.activity.MovieDetailActivity;
import dhcnhn.aduc8386.nixflet.controller.activity.TVShowActivity;
import dhcnhn.aduc8386.nixflet.controller.adapter.CategoryAdapter;
import dhcnhn.aduc8386.nixflet.model.Category;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;
import dhcnhn.aduc8386.nixflet.service.TMDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {


    private String movieId;

    private TextView textViewTVShow;
    private TextView textViewMovie;
    private TextView textViewCategorySelect;
    private TextView textViewMovieName;
    private ImageView imageViewMoviePoster;
    private ImageView imageViewCategorySelectIcon;
    private Button buttonMovieInfo;
    private RecyclerView recyclerViewListMovie;

    private CategoryAdapter movieAdapter;
    private LoadingDialogFragment loadingDialogFragment;

    public static List<Category> movies;


    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movies = new ArrayList<>();
        loadingDialogFragment = new LoadingDialogFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        getDataFromApi();

    }

    private void getDataFromApi() {
        getPopularMovies();
        getTopRatedMovies();
        getUpcomingMovies();
        getNowPlayingMovies();
        getPopularTVShows();
        getTVShowsAiringToday();
        getTopRatedTVShows();
    }

    private void bindView(View view) {
        textViewTVShow = view.findViewById(R.id.textview_main_tv_shows);
        textViewMovie = view.findViewById(R.id.textview_main_movies);
        textViewMovieName = view.findViewById(R.id.textview_main_movie_name);
        textViewCategorySelect = view.findViewById(R.id.textview_main_categories);
        imageViewMoviePoster = view.findViewById(R.id.imageview_main_movie_poster);
        imageViewCategorySelectIcon = view.findViewById(R.id.imageview_main_dropdown_icon);
        buttonMovieInfo = view.findViewById(R.id.button_main_info_button);
        recyclerViewListMovie = view.findViewById(R.id.recyclerview_main_category_list);
        loadingDialogFragment.show(this.getParentFragmentManager(), LoadingDialogFragment.TAG);

        movieAdapter = new CategoryAdapter(movies, this);
        recyclerViewListMovie.setAdapter(movieAdapter);
        recyclerViewListMovie.setLayoutManager(new LinearLayoutManager(MainFragment.this.getActivity(), LinearLayoutManager.VERTICAL, false));

        textViewTVShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), TVShowActivity.class);
                startActivity(intent);
            }
        });

        textViewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MovieActivity.class);
                startActivity(intent);
            }
        });

        buttonMovieInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainFragment.this.getContext(), MovieDetailActivity.class);

                intent.putExtra(MainActivity.MOVIE_ID, movieId);
                intent.putExtra(MainActivity.IS_MOVIE, true);

                startActivity(intent);
            }
        });

        textViewCategorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySelectFragment categorySelectFragment = new CategorySelectFragment();
                MainFragment.this.getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container_main_fragment_overlay, categorySelectFragment)
                        .addToBackStack(categorySelectFragment.getClass().getName())
                        .commit();
            }
        });

        imageViewCategorySelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySelectFragment categorySelectFragment = new CategorySelectFragment();
                MainFragment.this.getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container_main_fragment_overlay, categorySelectFragment)
                        .addToBackStack(categorySelectFragment.getClass().getName())
                        .commit();
            }
        });

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

                    Random random = new Random();
                    int ranNum = random.nextInt(result.size());

                    movieId = movieResponses.get(ranNum).getId();
                    textViewMovieName.setText(movieResponses.get(ranNum).getTitle());
                    Glide.with(MainFragment.this.getContext()).
                            load(String.format("https://image.tmdb.org/t/p/original/%s", movieResponses.get(ranNum).getPosterPath()))
                            .centerCrop()
                            .error(R.drawable.ic_nixflet_text)
                            .into(imageViewMoviePoster);

                    movies.add(new Category("Popular", movieResponses));

                    movieAdapter.notifyDataSetChanged();
                    loadingDialogFragment.dismiss();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getUpcomingMovies() {
        TMDBService.init().getUpcomingMovies().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getNowPlayingMovies() {
        TMDBService.init().getNowPlayingMovies().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
                Toast.makeText(MainFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getTVShowsAiringToday() {
        TMDBService.init().getTVShowsAiringToday().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    List<MovieResponse> movieResponses = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        movieResponses.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movies.add(new Category("TV Shows Today", movieResponses));
                    movieAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getPopularTVShows() {
        TMDBService.init().getPopularTVShows().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    List<MovieResponse> movieResponses = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        movieResponses.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movies.add(new Category("Popular TV Shows", movieResponses));
                    movieAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void getTopRatedTVShows() {
        TMDBService.init().getTopRatedTVShows().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    JsonArray result = response.body().getAsJsonArray("results");
                    List<MovieResponse> movieResponses = new ArrayList<>();
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        movieResponses.add(gson.fromJson(result.get(i), MovieResponse.class));
                    }

                    movies.add(new Category("Top Rated TV Shows", movieResponses));
                    movieAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(MovieResponse movieResponse) {

        Intent intent = new Intent(MainFragment.this.getContext(), MovieDetailActivity.class);

        intent.putExtra(MainActivity.MOVIE_ID, movieResponse.getId());
        intent.putExtra(MainActivity.IS_MOVIE, movieResponse.getTitle() != null);

        startActivity(intent);
    }
}
