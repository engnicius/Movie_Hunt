package app.moviewdb.com.br.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String api_key, @Query("page") int pageIndex);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedeMovies(@Query("api_key") String api_key, @Query("page") int pageIndex);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
