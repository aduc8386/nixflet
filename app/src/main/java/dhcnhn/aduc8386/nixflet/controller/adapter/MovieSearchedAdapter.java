package dhcnhn.aduc8386.nixflet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;

public class MovieSearchedAdapter extends RecyclerView.Adapter<MovieSearchedAdapter.MovieSearchedHolder> {

    private List<MovieResponse> movies;
    private MovieAdapter.OnMovieClickListener onMovieClickListener;

    public MovieSearchedAdapter(List<MovieResponse> movies, MovieAdapter.OnMovieClickListener onMovieClickListener) {
        this.movies = movies;
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MovieSearchedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_searched, parent, false);

        return new MovieSearchedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSearchedHolder holder, int position) {
        MovieResponse movieResponse = movies.get(position);


        if(movieResponse.getName() != null) {
            holder.textViewName.setText(movieResponse.getName());
        } else {
            holder.textViewName.setText(movieResponse.getTitle());
        }

        holder.textViewOverview.setText(movieResponse.getOverview());

        Glide.with(holder.itemView.getContext())
                .load(String.format("https://image.tmdb.org/t/p/original/%s", movieResponse.getPosterPath()))
                .error(R.drawable.movie_poster)
                .centerCrop()
                .into(holder.imageViewPoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMovieClickListener.onMovieClick(movieResponse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieSearchedHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewPoster;
        private TextView textViewName;
        private TextView textViewOverview;

        public MovieSearchedHolder(@NonNull View itemView) {
            super(itemView);

            imageViewPoster = itemView.findViewById(R.id.imageview_item_movie_searched_movie_poster);
            textViewName = itemView.findViewById(R.id.textview_item_movie_searched_movie_name);
            textViewOverview = itemView.findViewById(R.id.textview_item_movie_searched_movie_overview);

        }
    }

}
