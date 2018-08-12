package app.moviewdb.com.br.moviedb;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ApiClient {
    private String baseUrl;
    private static Retrofit retrofit;

    public ApiClient(){
        this.baseUrl = "http://api.themoviedb.org/3/";

        retrofit = new Retrofit.Builder().baseUrl(this.baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getClient(){
        return retrofit;
    }

}
