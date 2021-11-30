package com.example.app_movie_gs.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_movie_gs.R
import com.example.app_movie_gs.adapter.VideoAdapter
import com.example.app_movie_gs.model.Constant
import com.example.app_movie_gs.model.VideoResponse
import com.example.app_movie_gs.retrofit.ApiServices
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VideoActivity : AppCompatActivity() {

    private val TAG: String = "VideoActivity"

    lateinit var videoAdapter : VideoAdapter
    lateinit var youTubePlayer: YouTubePlayer
    private  var youTubeKey : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        setupView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter(arrayListOf(), object : VideoAdapter.OnAdapterListener{
            override fun onLoad(key: String) {
                youTubeKey = key
            }
            override fun onPlay(key: String) {
                youTubePlayer.loadVideo(key, 0f)
            }

        })
        list_video.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        getVideoMovie()
    }


    private fun setupView() {
        supportActionBar!!.title = Constant.MOVIE_TITLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youTubeKey?.let{
                    youTubePlayer.cueVideo(it, 0f)
                }
            }
        })

    }

    private fun getVideoMovie(){

        showLoadingVideo(true)

        ApiServices().endPoint.getMovieVideo(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<VideoResponse> {
                override fun onResponse(
                    call: Call<VideoResponse>,
                    response: Response<VideoResponse>
                ) {
                    showLoadingVideo(false)
                    if(response.isSuccessful){
                        showVideo(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    showLoadingVideo(false)
                }

            })
    }

    private fun showLoadingVideo(loading: Boolean){
        when(loading){
            true -> {
                progress_video.visibility = View.VISIBLE
            }
            false -> {
                progress_video.visibility = View.GONE
            }
        }

    }

    private fun showVideo(video: VideoResponse){
        videoAdapter.setData(video.results)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}