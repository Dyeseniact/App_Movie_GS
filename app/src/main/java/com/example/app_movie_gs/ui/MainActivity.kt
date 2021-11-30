package com.example.app_movie_gs.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_movie_gs.R
import com.example.app_movie_gs.adapter.MainAdapter
import com.example.app_movie_gs.databinding.ActivityMainBinding
import com.example.app_movie_gs.model.Constant
import com.example.app_movie_gs.model.MovieModel
import com.example.app_movie_gs.model.MovieResponse
import com.example.app_movie_gs.retrofit.ApiServices
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val movieMostPopular = 0
const val moviePlayingNow = 1


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var mainAdapter: MainAdapter
    private val TAG: String = "MainActivity"
    private var category = 0
    private val api = ApiServices().endPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupRecyclerView()


    }

    private fun setupRecyclerView() {
        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener{
            override fun onClick(movie: MovieModel) {
                Constant.MOVIE_ID = movie.id!!
                Constant.MOVIE_TITLE = movie.title!!
                startActivity(Intent(applicationContext, DetailActivity::class.java))
            }

        })
        recyclerView_movies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        getMovies()
    }

    fun showMovies(response: MovieResponse){
        //test
//        for(movie in response.results!!){
//            Log.d(TAG, "titleMovie: ${movie.title}")
//        }
        response.results?.let { mainAdapter.setData(it) }

    }

    fun showLoading(loading: Boolean){
        when(loading){
            true -> progressBar_movies.visibility = View.VISIBLE
            false -> progressBar_movies.visibility = View.GONE
        }

    }

    fun getMovies(){

        showLoading(true)

        var apiCall: Call<MovieResponse>? = null
        when(category){
            movieMostPopular -> {
                apiCall = api.getMoviePopular(Constant.API_KEY, 1)
            }
            moviePlayingNow -> {
                apiCall = api.getMoviePlayingNow(Constant.API_KEY, 1)
            }
        }

        apiCall!!
            .enqueue(object: Callback<MovieResponse>{
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading(false)
                    if(response.isSuccessful){
                        showMovies(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, "ErrorResponse: $t")
                    showLoading(false)
                }

            })

    }

    fun showMessages(msg: String){

        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.item_playingNow -> {
                showMessages("Section: Playing Now")
                category = moviePlayingNow
                getMovies()
                true
            }
            R.id.item_mostPopular -> {
                showMessages("Section: Most Popular")
                category = movieMostPopular
                getMovies()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}