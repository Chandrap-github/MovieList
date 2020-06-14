package com.android.moviechallenge.movieList

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.android.moviechallenge.R
import com.android.moviechallenge.api.ApiClient
import com.android.moviechallenge.api.ApiService
import com.android.moviechallenge.movieList.model.MovieListResponse
import com.android.moviechallenge.utils.StringUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getMovieList(StringUtils.Marvel)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_list, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.searchHint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getMovieList(query!!)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    fun getMovieList(inputText: String){
        val request = ApiClient.buildService(ApiService::class.java)
        val call = request.getMovieList(StringUtils.API_KEY, inputText, StringUtils.MOVIE)

        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.body()!!.response) {
                    progress_bar.visibility = View.GONE
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@MovieListActivity, 2)
                        adapter = MovieListAdapter(response.body()!!.movies, context)
                    }
                }else{
                    Toast.makeText(this@MovieListActivity,getString(R.string.error_movie_list),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                Toast.makeText(this@MovieListActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}