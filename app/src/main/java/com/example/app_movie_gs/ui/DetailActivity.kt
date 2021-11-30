package com.example.app_movie_gs.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.app_movie_gs.R
import com.example.app_movie_gs.databinding.ActivityDetailBinding
import com.example.app_movie_gs.model.Constant
import com.example.app_movie_gs.model.DetailResponse
import com.example.app_movie_gs.retrofit.ApiServices
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val TAG: String = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListener()



    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
    }
    private fun setupView(){
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.title = ""
        supportActionBar!!.hide()
        //supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupListener() {
        fab_play.setOnClickListener {
            startActivity(Intent(applicationContext, VideoActivity::class.java))
        }
    }

    private fun getMovieDetail(){
        ApiServices().endPoint.getMovieDetail(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if(response.isSuccessful){
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

            })
    }

    fun showMovie(detail: DetailResponse){
        val backdropPath = Constant.BACKDROP_PHAT + detail.backdrop_path

        Picasso.get()
            .load(backdropPath)
            .placeholder(R.drawable.placeholder_portrait)
            .error(R.drawable.placeholder_portrait_error)
            .fit().centerCrop()
            .into(image_poster);

        text_title_detail.text = detail.title
        text_voteAverage.text =detail.vote_average.toString()
        text_description.text = detail.overview


        for (genre in detail.genres!!){
            text_genre.text = "${genre.name}"
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}