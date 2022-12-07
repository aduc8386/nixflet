package dhcnhn.aduc8386.nixflet.service;

import com.google.gson.JsonObject;

import dhcnhn.aduc8386.nixflet.model.MovieDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IService {

    @GET("movie/popular?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getPopularMovies();

    @GET("movie/top_rated?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getTopRatedMovies();

    @GET("movie/upcoming?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getUpcomingMovies();

    @GET("movie/now_playing?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getNowPlayingMovies();

    @GET("tv/airing_today?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getTVShowsAiringToday();

    @GET("tv/popular?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getPopularTVShows();

    @GET("tv/top_rated?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getTopRatedTVShows();

    @GET("movie/{id}?api_key=09da4019f7e387773b013fa80918d219&language=en-US&append_to_response=videos")
    Call<MovieDetail> getMovieDetail(@Path("id") String movieId);

    @GET("movie/{id}/recommendations?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getMovieRecommendations(@Path("id") String movieId);

    @GET("movie/{id}/credits?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getMovieCast(@Path("id") String movieId);

    @GET("tv/{id}?api_key=09da4019f7e387773b013fa80918d219&language=en-US&append_to_response=videos")
    Call<MovieDetail> getTVShowDetail(@Path("id") String movieId);

    @GET("tv/{id}/recommendations?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getTVShowRecommendations(@Path("id") String movieId);

    @GET("tv/{id}/credits?api_key=09da4019f7e387773b013fa80918d219&language=en-US")
    Call<JsonObject> getTVShowCast(@Path("id") String movieId);

}
