package com.android.moviechallenge.movieDetail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.moviechallenge.R
import com.android.moviechallenge.api.ApiClient
import com.android.moviechallenge.api.ApiService
import com.android.moviechallenge.utils.StringUtils
import com.bumptech.glide.Glide
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailActivity : AppCompatActivity() {

    @Suppress("NAME_SHADOWING")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId: String? = intent.getStringExtra(StringUtils.MOVIE_ID)
        val request = ApiClient.buildService(ApiService::class.java)
        val call = request.getMovieDetail(StringUtils.API_KEY, movieId!!)

        call.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                if (response.body()!!.response) {
                    progress_bar.visibility = View.GONE
                    val response = response.body()!!
                    setSupportActionBar(findViewById(R.id.toolbar))
                    val collapsingToolbar =
                        findViewById<SubtitleCollapsingToolbarLayout>(R.id.toolbar_layout)
                    collapsingToolbar.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed)
                    collapsingToolbar.setExpandedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Expanded)
                    collapsingToolbar.setCollapsedSubtitleTextAppearance(R.style.TextAppearance_MyApp_SubTitle_Collapsed)
                    collapsingToolbar.setExpandedSubtitleTextAppearance(R.style.TextAppearance_MyApp_SubTitle_Expanded)

                    findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
                        Snackbar.make(
                            view,
                            getString(R.string.movie_video_text),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(getString(R.string.action), null)
                            .setTextColor(resources.getColor(R.color.colorAccent))
                            .setBackgroundTint(resources.getColor(R.color.colorPrimaryDark)).show()
                    }
                    collapsingToolbar.title = response.title
                    collapsingToolbar.subtitle = response.year
                    Glide.with(this@MovieDetailActivity).load(response.poster)
                        .error(R.drawable.ic_no_image_available)
                        .into(findViewById(R.id.header_image))
                    findViewById<TextView>(R.id.tv_genre).text = response.genre
                    findViewById<TextView>(R.id.tv_duration).text = response.runtime
                    findViewById<TextView>(R.id.tv_rating).text = response.imdbRating
                    findViewById<TextView>(R.id.tv_synopsis).text = response.plot
                    findViewById<TextView>(R.id.tv_score).text = response.metascore
                    findViewById<TextView>(R.id.tv_reviews).text = response.imdbRating
                    findViewById<TextView>(R.id.tv_popularity).text = response.imdbVotes
                    findViewById<TextView>(R.id.tv_director).text = response.director
                    findViewById<TextView>(R.id.tv_writer).text = response.writer
                    findViewById<TextView>(R.id.tv_actor).text = response.actors

                } else {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(
                        this@MovieDetailActivity,
                        getString(R.string.error_movie_detail),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                Toast.makeText(this@MovieDetailActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}