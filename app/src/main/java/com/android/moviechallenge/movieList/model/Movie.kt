package com.android.moviechallenge.movieList.model

import com.google.gson.annotations.SerializedName

data class Movie(
  @SerializedName("imdbID") val id: String,
  @SerializedName("Title") val title: String,
  @SerializedName("Year") val year: Int,
  @SerializedName("Type") val type: String,
  @SerializedName("Poster") val poster: String
)