package com.android.moviechallenge.movieList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.moviechallenge.R
import com.android.moviechallenge.movieDetail.MovieDetailActivity
import com.android.moviechallenge.movieList.model.Movie
import com.android.moviechallenge.utils.StringUtils
import com.bumptech.glide.Glide


class MovieListAdapter(private val movies: List<Movie>, private val context: Context) : RecyclerView.Adapter<MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        return holder.bind(movies[position], context)
    }
}

class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val photo: ImageView = itemView.findViewById(R.id.movie_photo)
    private val title: TextView = itemView.findViewById(R.id.movie_title)

    fun bind(movie: Movie,context: Context) {
        Glide.with(itemView.context).load(movie.poster).error(R.mipmap.ic_launcher).into(photo)
        title.text = movie.title

        itemView.setOnClickListener {
            val intent = Intent(context, MovieDetailActivity::class.java).apply{
                putExtra(StringUtils.MOVIE_ID, movie.id)
            }
            context.startActivity(intent)
        }
    }

}