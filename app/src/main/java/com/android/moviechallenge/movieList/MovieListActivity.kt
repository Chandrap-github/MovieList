package com.android.moviechallenge.movieList

import VerticalPaginationScrollListener
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.android.moviechallenge.R
import com.android.moviechallenge.api.ApiClient
import com.android.moviechallenge.api.ApiService
import com.android.moviechallenge.movieList.model.Movie
import com.android.moviechallenge.movieList.model.MovieListResponse
import com.android.moviechallenge.utils.StringUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieListActivity : AppCompatActivity() {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page: Int = 1
    var page_size: Int = 9
    val moviesList: MutableList<Movie> = mutableListOf()
    var layoutManager: GridLayoutManager? = null
    var adapter: MovieListAdapter? = null
    var isSearchText: Boolean = false
    lateinit var movieText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.movie_list)

        initailiseViews()


    }

    fun initailiseViews() {

        layoutManager = GridLayoutManager(this@MovieListActivity, 2)
        recyclerView.layoutManager = layoutManager
        adapter = MovieListAdapter(moviesList, this@MovieListActivity)
        recyclerView.adapter = adapter

        getMovieList(StringUtils.Marvel)

        recyclerView?.addOnScrollListener(object :
            VerticalPaginationScrollListener(layoutManager!!) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = false
                page++
                getMovieList(movieText)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_list, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.searchHint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                page = 1
                isSearchText = true
                getMovieList(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    fun getMovieList(inputText: String) {
        isLoading = true
        movieText = inputText
        val request = ApiClient.buildService(ApiService::class.java)
        val call = request.getMovieList(StringUtils.API_KEY, inputText, StringUtils.MOVIE, page)

        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                isLoading = false
                progress_bar.visibility = View.GONE

                if (response.body()!!.response) {
                    isLastPage = if (response.body()!!.movies.isNotEmpty()) {
                        if (isSearchText) {
                            moviesList.clear()
                        }
                        adapter?.addList(response.body()!!.movies)

                        response.body()!!.movies.size < page_size
                    } else {
                        true
                    }

                } else {
                    Toast.makeText(
                        this@MovieListActivity,
                        getString(R.string.error_movie_list),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                progress_bar.visibility = View.GONE
                Toast.makeText(this@MovieListActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}