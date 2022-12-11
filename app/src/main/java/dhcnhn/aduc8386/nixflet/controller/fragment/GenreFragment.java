package dhcnhn.aduc8386.nixflet.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import dhcnhn.aduc8386.nixflet.controller.activity.GenreActivity;
import dhcnhn.aduc8386.nixflet.controller.activity.MainActivity;
import dhcnhn.aduc8386.nixflet.controller.activity.MovieDetailActivity;
import dhcnhn.aduc8386.nixflet.controller.adapter.CategoryAdapter;
import dhcnhn.aduc8386.nixflet.model.Category;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;
import dhcnhn.aduc8386.nixflet.service.TMDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private String genreName;
    private String genreId;
    private String movieId;
    private boolean isMovie;

    private TextView textViewMovieName;
    private TextView textViewCategorySelect;
    private ImageView imageViewMoviePoster;
    private ImageView imageViewCategorySelectIcon;
    private Button buttonMovieInfo;
    private RecyclerView recyclerViewListMovie;

    private LoadingDialogFragment loadingDialogFragment;
    private CategoryAdapter movieAdapter;

    private List<Category> movies;

    public GenreFragment() {
        super(R.layout.fragment_movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            genreName = getArguments().getString(GenreActivity.GENRE_NAME);
            genreId = getArguments().getString(GenreActivity.GENRE_ID);
            isMovie = getArguments().getBoolean(GenreActivity.IS_MOVIE);
        }

        movies = new ArrayList<>();
        if (isMovie) {
            getMoviesByGenre(genreId);
        } else {
            getTVShowsByGenre(genreId);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
    }

    private void bindView(View view) {
        textViewMovieName = view.findViewById(R.id.textview_movie_movie_name);
        textViewCategorySelect = view.findViewById(R.id.textview_movie_categories);
        imageViewMoviePoster = view.findViewById(R.id.imageview_movie_movie_poster);
        imageViewCategorySelectIcon = view.findViewById(R.id.imageview_movie_dropdown_icon);
        buttonMovieInfo = view.findViewById(R.id.button_movie_info_button);
        recyclerViewListMovie = view.findViewById(R.id.recyclerview_movie_category_list);
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.show(getParentFragmentManager(), LoadingDialogFragment.TAG);


        movieAdapter = new CategoryAdapter(movies, this);
        recyclerViewListMovie.setAdapter(movieAdapter);
        recyclerViewListMovie.setLayoutManager(new LinearLayoutManager(GenreFragment.this.getActivity(), LinearLayoutManager.VERTICAL, false));

        buttonMovieInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenreFragment.this.getContext(), MovieDetailActivity.class);

                intent.putExtra(MainActivity.MOVIE_ID, movieId);
                intent.putExtra(MainActivity.IS_MOVIE, true);

                startActivity(intent);
            }
        });

        textViewCategorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySelectFragment categorySelectFragment = new CategorySelectFragment();
                GenreFragment.this.getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container_genre_fragment_category, categorySelectFragment)
                        .addToBackStack(categorySelectFragment.getClass().getName())
                        .commit();
            }
        });

        imageViewCategorySelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySelectFragment categorySelectFragment = new CategorySelectFragment();
                GenreFragment.this.getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container_genre_fragment_category, categorySelectFragment)
                        .addToBackStack(categorySelectFragment.getClass().getName())
                        .commit();
            }
        });

    }

    void getTVShowsByGenre(String genreId) {
        TMDBService.init().getTVShowByGenre(genreId).enqueue(new Callback<JsonObject>() {
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
                    Glide.with(GenreFragment.this.getContext()).
                            load(String.format("https://image.tmdb.org/t/p/original/%s", movieResponses.get(ranNum).getPosterPath()))
                            .centerCrop()
                            .error(R.drawable.ic_nixflet_text)
                            .into(imageViewMoviePoster);

                    movies.add(new Category(genreName, movieResponses));

                    movieAdapter.notifyDataSetChanged();
                    loadingDialogFragment.dismiss();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(GenreFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });


    }

    void getMoviesByGenre(String genreId) {
        TMDBService.init().getMovieByGenre(genreId).enqueue(new Callback<JsonObject>() {
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
                    Glide.with(GenreFragment.this.getContext()).
                            load(String.format("https://image.tmdb.org/t/p/original/%s", movieResponses.get(ranNum).getPosterPath()))
                            .centerCrop()
                            .error(R.drawable.ic_nixflet_text)
                            .into(imageViewMoviePoster);

                    movies.add(new Category(genreName, movieResponses));

                    movieAdapter.notifyDataSetChanged();
                    loadingDialogFragment.dismiss();

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(GenreFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(MovieResponse movieResponse) {

        Intent intent = new Intent(GenreFragment.this.getContext(), MovieDetailActivity.class);

        intent.putExtra(MainActivity.MOVIE_ID, movieResponse.getId());
        intent.putExtra(MainActivity.IS_MOVIE, true);

        startActivity(intent);
    }
}
