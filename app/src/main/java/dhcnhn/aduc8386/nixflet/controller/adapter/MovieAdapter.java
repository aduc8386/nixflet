package dhcnhn.aduc8386.nixflet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<MovieResponse> movieResponses;
    private OnMovieClickListener movieClickListener;

    public MovieAdapter(List<MovieResponse> movieResponses, OnMovieClickListener movieClickListener) {
        this.movieResponses = movieResponses;
        this.movieClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(String.format("https://image.tmdb.org/t/p/original/%s", movieResponses.get(position).getPosterPath()))
                .error(R.drawable.ic_nixflet_text)
                .centerCrop()
                .into(holder.movieAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieClickListener.onMovieClick(movieResponses.get(holder.getAbsoluteAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieResponses.size();
    }

    static class MovieHolder extends RecyclerView.ViewHolder {

        private final ImageView movieAvatar;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);

            movieAvatar = itemView.findViewById(R.id.imageview_item_movie_movie_avatar);
        }
    }

    public interface OnMovieClickListener {
        void onMovieClick(MovieResponse movieResponse);
    }
}


