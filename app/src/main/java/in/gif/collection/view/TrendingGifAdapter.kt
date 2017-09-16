package `in`.gif.collection.view

import `in`.gif.collection.R
import `in`.gif.collection.databinding.ListItemRandomGifBinding
import `in`.gif.collection.model.TrendingGifResponse
import `in`.gif.collection.viewmodel.ItemRandomGifModel
import android.app.Activity
import android.databinding.DataBindingUtil.inflate
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception

/**
 * Created by vivek on 15/09/17.
 */

class TrendingGifAdapter(activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<TrendingGifResponse> = emptyList()
    var activity: Activity = activity
    val VIEW_TYPE_LOADING = 1
    val VIEW_TYPE_CONTENT = 2
    private var shouldLoadMore: Boolean = false


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is TrendingGifViewHolder -> {
                holder?.itemGifBinding?.progress?.visibility = View.VISIBLE
                var view = holder?.itemGifBinding?.gifIv
                Glide.with(view?.context)
                        .load(list[position].images.fixedHeightGifs.url)
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(object : RequestListener<String, GifDrawable> {
                            override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                holder?.itemGifBinding?.progress?.visibility = View.GONE
                                return false
                            }

                            override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }
                        }).into(view)
                holder?.bindGif(list[holder.adapterPosition], holder.adapterPosition, activity)
            }

            is LoadingViewHolder -> {
                holder.progress.isIndeterminate = true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var viewHolder: RecyclerView.ViewHolder? = null
        if (viewType == VIEW_TYPE_LOADING) {
            var view = LayoutInflater.from(parent?.context).inflate(R.layout.progress_item, parent, false)
            viewHolder = LoadingViewHolder(view)
        } else if (viewType == VIEW_TYPE_CONTENT) {
            val listItemViewBinding: ListItemRandomGifBinding = inflate(LayoutInflater.from(parent?.context), R.layout.list_item_random_gif, parent, false)
            viewHolder = TrendingGifViewHolder(listItemViewBinding.root, listItemViewBinding)
        }
        return viewHolder

    }

    fun setShouldLoadMore(boolean: Boolean) {
        shouldLoadMore = boolean
    }

    override fun getItemViewType(position: Int): Int {
        var result = 0
        if (list.get(position).toString().isNullOrEmpty()) return VIEW_TYPE_LOADING else return VIEW_TYPE_CONTENT
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setGifList(list: List<TrendingGifResponse>) {
        this.list = list
        notifyDataSetChanged()
    }


    class TrendingGifViewHolder : RecyclerView.ViewHolder {
        var itemGifBinding: ListItemRandomGifBinding

        constructor(itemView: View?, itemGifBinding: ListItemRandomGifBinding) : super(itemView) {
            this.itemGifBinding = itemGifBinding
        }

        fun bindGif(gif: TrendingGifResponse, pos: Int, activity: Activity) {
            if (itemGifBinding.itemRandomGifModel == null) {
                itemGifBinding.itemRandomGifModel = ItemRandomGifModel(activity, gif, pos)
            } else
                itemGifBinding.itemRandomGifModel!!.setGif(gif)
        }
    }

    class LoadingViewHolder : RecyclerView.ViewHolder {
        var progress: ProgressBar

        constructor(itemView: View?) : super(itemView) {
            progress = itemView?.findViewById(R.id.progressBar) as ProgressBar

        }
    }
}