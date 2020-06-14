package com.android.moviechallenge.movieList.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse (
    @SerializedName("Response") val response: Boolean,
    @SerializedName("Search") val movies: List<Movie>,
    @SerializedName("totalResults") val totalResults: Int
)