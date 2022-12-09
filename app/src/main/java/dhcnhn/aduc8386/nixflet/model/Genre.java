package dhcnhn.aduc8386.nixflet.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    private boolean isMovie;

    public Genre(String id, String name, boolean isMovie) {
        this.id = id;
        this.name = name;
        this.isMovie = isMovie;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isMovie() {
        return isMovie;
    }

    public void setMovie(boolean movie) {
        isMovie = movie;
    }
}
