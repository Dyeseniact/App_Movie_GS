package com.example.app_movie_gs.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_movie_gs.R
import com.example.app_movie_gs.model.Constant
import com.example.app_movie_gs.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_main.view.*

class MainAdapter(var movies: ArrayList<MovieModel>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val TAG: String = "MainAdapter"

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val view = view
        fun bind(movies: MovieModel){
            view.text_title.text = movies.title
            //view.text_title.text = movies.title


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_main, parent, false)

    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind( movie)
        val posterPath = Constant.POSTER_PHAT + movie.poster_path
        Log.d(TAG, "backdrop_path: $posterPath")

        Picasso.get()
            .load(posterPath)
            .placeholder(R.drawable.placeholder_portrait)
            .error(R.drawable.placeholder_portrait_error)
            .into(holder.view.img_movie);
    }

    override fun getItemCount() = movies.size

    public fun setData(newMovies: List<MovieModel>){
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}