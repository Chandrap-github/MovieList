package com.android.moviechallenge.api

import com.android.moviechallenge.movieDetail.MovieDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call;

import com.android.moviechallenge.movieList.model.MovieListResponse


interface ApiService {

    @GET("/")
    fun getMovieList(
        @Query("apikey") apikey: String,
        @Query("s") s: String,
        @Query("type") type: String
    ): Call<MovieListResponse>

    @GET("/")
    fun getMovieDetail(
        @Query("apikey") apikey: String,
        @Query("i") i: String
    ):Call<MovieDetailResponse>
}