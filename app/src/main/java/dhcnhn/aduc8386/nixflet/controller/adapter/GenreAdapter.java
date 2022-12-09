package dhcnhn.aduc8386.nixflet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.model.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {

    private List<Genre> genres;
    private OnGenreClickListener onGenreClickListener;

    public GenreAdapter(List<Genre> genres, OnGenreClickListener onGenreClickListener) {
        this.genres = genres;
        this.onGenreClickListener = onGenreClickListener;
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_select, parent, false);

        return new GenreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreHolder holder, int position) {
        Genre genre = genres.get(position);

        holder.textViewGenre.setText(genre.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGenreClickListener.onGenreClick(genre);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    static class GenreHolder extends RecyclerView.ViewHolder {

        private TextView textViewGenre;

        public GenreHolder(@NonNull View itemView) {
            super(itemView);

            textViewGenre = itemView.findViewById(R.id.textview_item_category_select_category);
        }
    }

    public interface OnGenreClickListener{
        void onGenreClick(Genre genre);
    }

}
