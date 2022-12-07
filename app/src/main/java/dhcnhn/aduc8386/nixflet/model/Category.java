package dhcnhn.aduc8386.nixflet.model;

import java.util.List;

public class Category {
    private String category;
    private List<MovieResponse> movies;

    public Category(String category, List<MovieResponse> movies) {
        this.category = category;
        this.movies = movies;
    }

    public String getCategory() {
        return category;
    }

    public List<MovieResponse> getMovies() {
        return movies;
    }
}

