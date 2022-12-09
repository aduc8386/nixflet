package dhcnhn.aduc8386.nixflet.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.activity.GenreActivity;
import dhcnhn.aduc8386.nixflet.controller.adapter.GenreAdapter;
import dhcnhn.aduc8386.nixflet.model.Genre;
import dhcnhn.aduc8386.nixflet.service.TMDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorySelectFragment extends Fragment implements GenreAdapter.OnGenreClickListener {

    private ImageView imageViewCloseButton;
    private RecyclerView recyclerViewGenre;

    private GenreAdapter genreAdapter;

    private List<Genre> genres;

    public CategorySelectFragment() {
        super(R.layout.fragment_category_select);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getGenres();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);
    }

    private void bindView(View view) {
        imageViewCloseButton = view.findViewById(R.id.imageview_category_select_close);
        recyclerViewGenre = view.findViewById(R.id.recycler_view_category_select_category);
        genreAdapter = new GenreAdapter(genres, this);
        recyclerViewGenre.setAdapter(genreAdapter);
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));

        imageViewCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySelectFragment.this.getParentFragmentManager().popBackStack();
            }
        });
    }

    private void getGenres() {
        genres = new ArrayList<>();
        getMovieGenres();
        getTVGenres();
    }

    private void getTVGenres() {
        TMDBService.init().getTVGenres().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray result = response.body().getAsJsonArray("genres");
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        Genre genre = gson.fromJson(result.get(i), Genre.class);
                        genre.setMovie(false);
                        genres.add(genre);
                    }

                    genreAdapter.notifyDataSetChanged();

                    Log.d("TAG", "onResponse: successful");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getMovieGenres() {
        TMDBService.init().getMovieGenres().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray result = response.body().getAsJsonArray("genres");
                    Gson gson = new Gson();
                    for (int i = 0; i < result.size(); i++) {
                        Genre genre = gson.fromJson(result.get(i), Genre.class);
                        genre.setMovie(true);
                        genres.add(genre);
                    }

                    genreAdapter.notifyDataSetChanged();

                    Log.d("TAG", "onResponse: successful");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onGenreClick(Genre genre) {
        Intent intent = new Intent(CategorySelectFragment.this.getContext(), GenreActivity.class);
        intent.putExtra(GenreActivity.GENRE_NAME, genre.getName());
        intent.putExtra(GenreActivity.GENRE_ID, genre.getId());
        intent.putExtra(GenreActivity.IS_MOVIE, genre.isMovie());
        startActivity(intent);
        getParentFragmentManager().popBackStack();
    }
}
