package dhcnhn.aduc8386.nixflet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.model.Category;
import dhcnhn.aduc8386.nixflet.model.MovieResponse;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private List<Category> categories;
    private OnCategoryClickListener categoryClickListener;

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener categoryClickListener) {
        this.categories = categories;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.textviewCategory.setText(categories.get(position).getCategory());
        MovieAdapter movieAdapter = new MovieAdapter(categories.get(position).getMovies(), new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(MovieResponse movie) {
                categoryClickListener.onCategoryClick(movie);
            }
        });
        holder.recyclerViewMovies.setAdapter(movieAdapter);
        holder.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryHolder extends RecyclerView.ViewHolder {

        private final TextView textviewCategory;
        private final RecyclerView recyclerViewMovies;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            textviewCategory = itemView.findViewById(R.id.textview_item_category_category);
            recyclerViewMovies = itemView.findViewById(R.id.recyclerview_item_category_category_list);


        }
    }

    public interface OnCategoryClickListener{
        void onCategoryClick(MovieResponse movieResponse);
    }
}
