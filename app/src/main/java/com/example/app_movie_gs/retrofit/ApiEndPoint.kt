package com.example.app_movie_gs.retrofit

import com.example.app_movie_gs.model.DetailResponse
import com.example.app_movie_gs.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {

    @GET("movie/now_playing")
    fun getMoviePlayingNow(@Query("api_key") api_key: String, @Query("page") page: Int): Call<MovieResponse>

    @GET("movie/popular")
    fun getMoviePopular(@Query("api_key") api_key: String, @Query("page") page: Int): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String): Call<DetailResponse>

}