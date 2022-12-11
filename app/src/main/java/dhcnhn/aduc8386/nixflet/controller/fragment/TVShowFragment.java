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
import dhcnhn.aduc8386.nixflet.controller.activity.MainActivity;
import dhcnhn.aduc8386.nixflet.controller.activity.MovieDetailActivity;
import dhcnhn.aduc8386.nixflet.controller.adapter.CategoryAdapter;
import dhcnhn.aduc8386.nixflet.model.Category;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;
import dhcnhn.aduc8386.nixflet.service.TMDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private String movieId;

    private TextView textViewTVShowName;
    private ImageView imageViewTVShowPoster;
    private Button buttonMovieInfo;

    private RecyclerView recyclerViewListTVShow;

    private CategoryAdapter tvShowAdapter;

    private List<Category> tvShows;
    private TextView textViewCategorySelect;
    private ImageView imageViewCategorySelectIcon;
    private ProgressBar progressBar;

    public TVShowFragment() {
        super(R.layout.fragment_movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvShows = new ArrayList<>();
        getPopularTVShows();
        getTVShowsAiringToday();
        getTopRatedTVShows();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

    }

    private void bindView(View view) {
        textViewCategorySelect = view.findViewById(R.id.textview_movie_categories);
        textViewTVShowName = view.findViewById(R.id.textview_movie_movie_name);
        imageViewTVShowPoster = view.findViewById(R.id.imageview_movie_movie_poster);
        imageViewCategorySelectIcon = view.findViewById(R.id.imageview_movie_dropdown_icon);
        buttonMovieInfo = view.findViewById(R.id.button_movie_info_button);
        recyclerViewListTVShow = view.findViewById(R.id.recyclerview_movie_category_list);
        progressBar = view.findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.VISIBLE);

        tvShowAdapter = new CategoryAdapter(tvShows, this);
        recyclerViewListTVShow.setAdapter(tvShowAdapter);
        recyclerViewListTVShow.setLayoutManager(new LinearLayoutManager(TVShowFragment.this.getActivity(), LinearLayoutManager.VERTICAL, false));

        buttonMovieInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TVShowFragment.this.getContext(), MovieDetailActivity.class);

                intent.putExtra(MainActivity.MOVIE_ID, movieId);
                intent.putExtra(MainActivity.IS_MOVIE, false);

                startActivity(intent);
            }
        });

        textViewCategorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySelectFragment categorySelectFragment = new CategorySelectFragment();
                TVShowFragment.this.getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container_tv_show_fragment_overlay, categorySelectFragment)
                        .addToBackStack(categorySelectFragment.getClass().getName())
                        .commit();
            }
        });

        imageViewCategorySelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySelectFragment categorySelectFragment = new CategorySelectFragment();
                TVShowFragment.this.getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container_tv_show_fragment_overlay, categorySelectFragment)
                        .addToBackStack(categorySelectFragment.getClass().getName())
                        .commit();
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

                    tvShows.add(new Category("TV Shows Today", movieResponses));
                    tvShowAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(TVShowFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
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

                    Random random = new Random();
                    int ranNum = random.nextInt(result.size());
                    movieId = movieResponses.get(ranNum).getId();

                    textViewTVShowName.setText(movieResponses.get(ranNum).getName());
                    Glide.with(TVShowFragment.this.getContext()).
                            load(String.format("https://image.tmdb.org/t/p/original/%s", movieResponses.get(ranNum).getPosterPath()))
                            .centerCrop()
                            .error(R.drawable.ic_nixflet_text)
                            .into(imageViewTVShowPoster);

                    tvShows.add(new Category("Popular TV Shows", movieResponses));
                    tvShowAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(TVShowFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
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

                    tvShows.add(new Category("Top Rated TV Shows", movieResponses));
                    tvShowAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(TVShowFragment.this.getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(MovieResponse movieResponse) {

        Intent intent = new Intent(TVShowFragment.this.getContext(), MovieDetailActivity.class);

        intent.putExtra(MainActivity.MOVIE_ID, movieResponse.getId());
        intent.putExtra(MainActivity.IS_MOVIE, false);

        startActivity(intent);
    }
}
