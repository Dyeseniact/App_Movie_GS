package com.example.app_movie_gs.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_movie_gs.R
import com.example.app_movie_gs.model.VideoModel
import kotlinx.android.synthetic.main.adapter_video.view.*

class VideoAdapter(var videos: ArrayList<VideoModel>, var listener: OnAdapterListener): RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private val TAG: String = "VideoAdapter"

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val view = view
        fun bind(videos: VideoModel){
            view.text_video.text = videos.name


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_video, parent, false)

    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]
        holder.bind( video)

        holder.view.text_video.setOnClickListener{
            listener.onPlay(video.key!!)

        }
    }

    override fun getItemCount() = videos.size

    public fun setData(newVideos: List<VideoModel>){
        videos.clear()
        videos.addAll(newVideos)
        notifyDataSetChanged()
        listener.onLoad(newVideos[0].key!!)
    }

    interface OnAdapterListener{
        fun onLoad(key: String)
        fun onPlay(key: String)
    }
}