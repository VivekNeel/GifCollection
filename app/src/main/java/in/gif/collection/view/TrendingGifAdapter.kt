package `in`.gif.collection.view

import `in`.gif.collection.*
import `in`.gif.collection.Utils.CommonUtils
import `in`.gif.collection.Utils.NetworkUtil
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.databinding.ListItemRandomGifBinding
import `in`.gif.collection.model.tenor.GifResultsData
import `in`.gif.collection.model.youtube.ItemsData
import `in`.gif.collection.viewmodel.CommonItemGifModel
import android.app.Activity
import android.databinding.DataBindingUtil.inflate
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.list_item_random_gif.view.*
import kotlinx.android.synthetic.main.list_item_youtube_videos.view.*
import java.lang.Exception
import `in`.gif.collection.MainActivity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log
import com.commit451.youtubeextractor.YouTubeExtractionResult
import io.reactivex.disposables.Disposable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import com.commit451.youtubeextractor.YouTubeExtractor
import io.reactivex.schedulers.Schedulers
import java.io.File


/**
 * Created by vivek on 15/09/17.
 */

class TrendingGifAdapter(activity: Activity, isYouTube: Boolean = false, callback: ITermItemClickedCallback? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<GifResultsData> = emptyList()
    private var videoList: List<ItemsData> = emptyList()
    val activity: Activity = activity
    val VIEW_TYPE_CONTENT = 2
    val VIEW_TYPE_GIF = 0
    val VIEW_TYPE_VIDEO = 1
    val isVideo = isYouTube
    val callback = callback


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder?.itemViewType) {
            VIEW_TYPE_CONTENT -> {
                with(holder as TrendingGifViewHolder) {
                    holder.itemGifBinding.progress.visibility = View.VISIBLE
                    val view = holder.itemGifBinding.gifIv
                    holder.itemView.fav.hide()
                    if (CommonUtils.isGifFavourited(list[position].id, view.context)) {
                        holder.itemView.fav.setBackgroundResource(R.drawable.fav_selected)
                    } else {
                        holder.itemView.fav.setBackgroundResource(R.drawable.fav_unselected)
                    }
                    holder.itemView.gifIv.isClickable = false
                    Glide.with(view.context)
                            .load(list[position].mediaData[0].nano.url)
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .listener(object : RequestListener<String, GifDrawable> {
                                override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                    holder.itemGifBinding.progress.visibility = View.GONE
                                    holder.itemView.fav.show()
                                    holder.itemView.gifIv.isClickable = true
                                    return false
                                }

                                override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }
                            }).into(view)
                    holder.bindGif(list[holder.adapterPosition], holder.adapterPosition, activity)
                    holder.itemView.fav.setOnClickListener {
                        val oldSet: MutableSet<String> = PreferenceHelper.defaultPrefs(view.context)[Constants.KEY_FAVOURITE]
                        if (oldSet.contains(list[position].id)) {
                            oldSet.remove(list[position].id)
                            holder.itemView.fav.setBackgroundResource(R.drawable.fav_unselected)
                            view.context.toast("Removed from favourites :(")
                        } else {
                            oldSet.add(list[position].id)
                            holder.itemView.fav.setBackgroundResource(R.drawable.fav_selected)
                            view.context.toast("Added to favourites!")
                        }
                        PreferenceHelper.defaultPrefs(view.context)[Constants.KEY_FAVOURITE] = oldSet

                    }
                }
            }

            VIEW_TYPE_VIDEO -> {
                //TODO Follow MVVM
                with((holder as VideoViewHolder).itemView) {
                    Glide.with(context)
                            .load(videoList[position].snippetData?.thumbnails?.medium?.url).into(videoThumbnail)
                    videoTitle.text = videoList[position].snippetData?.title
                    videoTitle.setOnClickListener {
                        val intent = Intent(context , VideoViewActivity::class.java)
                        intent.putExtra(Constants.EXTRA_VIDEO_ID , videoList[position].idData?.videoId)
                        context.startActivity(intent)
                    }
                }
            }
        }

    }

    private fun scanMedia(file: File, context: Context) {
        val uri = Uri.fromFile(file)
        val scanFileIntent = Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
        context.sendBroadcast(scanFileIntent)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var viewHolder: RecyclerView.ViewHolder? = null
        if (viewType == VIEW_TYPE_VIDEO) {
            var view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_youtube_videos, parent, false)
            viewHolder = VideoViewHolder(view)
        } else if (viewType == VIEW_TYPE_GIF) {
            val listItemViewBinding: ListItemRandomGifBinding = inflate(LayoutInflater.from(parent?.context), R.layout.list_item_random_gif, parent, false)
            viewHolder = TrendingGifViewHolder(listItemViewBinding.root, listItemViewBinding)
        }
        return viewHolder

    }


    override fun getItemViewType(position: Int): Int {
        if (isVideo) return VIEW_TYPE_VIDEO else return VIEW_TYPE_GIF
    }

    override fun getItemCount(): Int {
        if (isVideo)
            return videoList.size
        else
            return list.size
    }

    fun setGifList(list: List<GifResultsData>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setVideoList(list: List<ItemsData>) {
        this.videoList = list
        notifyDataSetChanged()
    }


    class TrendingGifViewHolder(itemView: View?, var itemGifBinding: ListItemRandomGifBinding) : RecyclerView.ViewHolder(itemView) {

        fun bindGif(gif: GifResultsData, pos: Int, activity: Activity) {
            if (itemGifBinding.itemRandomGifModel == null) {
                itemGifBinding.itemRandomGifModel = CommonItemGifModel(activity, gif, pos, "")
            } else
                itemGifBinding.itemRandomGifModel!!.setGif(gif)
        }
    }

    class VideoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}