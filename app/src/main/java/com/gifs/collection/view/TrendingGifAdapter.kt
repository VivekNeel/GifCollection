package com.gifs.collection.view

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
import com.gifs.collection.*
import com.gifs.collection.Utils.CommonUtils
import com.gifs.collection.Utils.PreferenceHelper
import com.gifs.collection.anayltics.CustomAnayltics
import com.gifs.collection.databinding.ListItemRandomGifBinding
import com.gifs.collection.model.tenor.GifResultsData
import com.gifs.collection.viewmodel.CommonItemGifModel
import kotlinx.android.synthetic.main.list_item_random_gif.view.*
import java.lang.Exception

/**
 * Created by vivek on 15/09/17.
 */

class TrendingGifAdapter(activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<GifResultsData> = emptyList()
    val activity: Activity = activity
    val VIEW_TYPE_LOADING = 1
    val VIEW_TYPE_CONTENT = 2
    private var shouldLoadMore: Boolean = false


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is TrendingGifViewHolder -> {
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
                        CustomAnayltics.logCustom("Removed from fav")
                    } else {
                        oldSet.add(list[position].id)
                        holder.itemView.fav.setBackgroundResource(R.drawable.fav_selected)
                        view.context.toast("Added to favourites!")
                        CustomAnayltics.logCustom("Added to favourites")
                    }
                    PreferenceHelper.defaultPrefs(view.context)[Constants.KEY_FAVOURITE] = oldSet

                }
            }

            is LoadingViewHolder -> {
                holder.progress?.isIndeterminate = true
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

    fun setGifList(list: List<GifResultsData>) {
        this.list = list
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

    class LoadingViewHolder : RecyclerView.ViewHolder {
        var progress: ProgressBar?

        constructor(itemView: View?) : super(itemView) {
            progress = itemView?.findViewById(R.id.progressBar)

        }
    }
}