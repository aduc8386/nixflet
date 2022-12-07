package dhcnhn.aduc8386.nixflet.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetail {
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genres")
    private Genre[] genres;
    @SerializedName("id")
    private String id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("title")
    private String title;
    @SerializedName("original_name")
    private String originalName;
    @SerializedName("name")
    private String name;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("video")
    private boolean video;
    @SerializedName("vote_average")
    private double voteAverage;
//    @SerializedName("belongs_to_collection")
//    private String belongsToCollection;
    @SerializedName("budget")
    private double budget;
    @SerializedName("imdb_id")
    private String imdbId;
    @SerializedName("status")
    private String status;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("videos")
    private VideoResult videos;

    public MovieDetail(String posterPath, boolean adult, String overview, String releaseDate, Genre[] genres, String id, String originalTitle, String title, String originalName, String name, String originalLanguage, String backdropPath, double popularity, int voteCount, boolean video, double voteAverage, double budget, String imdbId, String status, String tagline, VideoResult videos) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.originalName = originalName;
        this.name = name;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.budget = budget;
        this.imdbId = imdbId;
        this.status = status;
        this.tagline = tagline;
        this.videos = videos;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public String getId() {
        return id;
    }

    public VideoResult getVideos() {
        return videos;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

//    public String getBelongsToCollection() {
//        return belongsToCollection;
//    }

    public double getBudget() {
        return budget;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

}
