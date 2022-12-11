package dhcnhn.aduc8386.nixflet.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.controller.adapter.MovieAdapter;
import dhcnhn.aduc8386.nixflet.controller.adapter.MovieSearchedAdapter;
import dhcnhn.aduc8386.nixflet.controller.fragment.MainFragment;
import dhcnhn.aduc8386.nixflet.model.Category;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;

public class SearchingActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    private ImageView imageViewBackButton;
    private ImageView imageViewSearchButton;
    private EditText editTextSearchInput;
    private RecyclerView recyclerViewMovieSearched;

    private MovieSearchedAdapter movieSearchedAdapter;

    private List<MovieResponse> movies;
    private List<Category> list = MainFragment.movies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        bindView();
    }

    private void bindView() {
        imageViewBackButton = findViewById(R.id.imageview_searching_back_icon);
        imageViewSearchButton = findViewById(R.id.imageview_searching_search_icon);
        editTextSearchInput = findViewById(R.id.edittext_searching_input);
        recyclerViewMovieSearched = findViewById(R.id.recycler_view_searching_list);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editTextSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                movies = new ArrayList<>();

                if(!s.toString().isEmpty()) {
                    for (Category category : list) {
                        for (MovieResponse movieResponse : category.getMovies()) {
                            if(movieResponse.getTitle() != null) {
                                if(movieResponse.getTitle().toLowerCase(Locale.ROOT).contains(s)) {
                                    movies.add(movieResponse);
                                }
                            }

                            if(movieResponse.getName() != null) {
                                if(movieResponse.getName().toLowerCase(Locale.ROOT).contains(s)) {
                                    movies.add(movieResponse);
                                }
                            }

                        }
                    }
                }

                movieSearchedAdapter = new MovieSearchedAdapter(movies, SearchingActivity.this);
                recyclerViewMovieSearched.setAdapter(movieSearchedAdapter);
                recyclerViewMovieSearched.setLayoutManager(new LinearLayoutManager(SearchingActivity.this, LinearLayoutManager.VERTICAL, false));

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onMovieClick(MovieResponse movieResponse) {
        Intent intent = new Intent(this, MovieDetailActivity.class);

        intent.putExtra(MainActivity.MOVIE_ID, movieResponse.getId());
        intent.putExtra(MainActivity.IS_MOVIE, movieResponse.getTitle() != null);

        startActivity(intent);
    }
}
