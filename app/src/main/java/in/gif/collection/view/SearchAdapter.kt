package `in`.gif.collection.view

import `in`.gif.collection.R
import `in`.gif.collection.Utils.NetworkUtil
import `in`.gif.collection.databinding.ListItemRandomGifBinding
import `in`.gif.collection.model.TrendingGifResponse
import `in`.gif.collection.model.tenor.GifResultsData
import `in`.gif.collection.viewmodel.CommonItemGifModel
import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception

/**
 * Created by vivek on 17/09/17.
 */
class SearchAdapter(activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<GifResultsData> = emptyList()
    var activity: Activity = activity
    private var shouldLoadMore: Boolean = false


    override fun onBindViewHolder(parentHolder: RecyclerView.ViewHolder?, position: Int) {
        var holder = parentHolder as TrendingGifViewHolder
        holder?.itemGifBinding?.progress?.visibility = View.VISIBLE
        var view = holder?.itemGifBinding?.gifIv
        Glide.with(view?.context)
                .load(NetworkUtil.getAppropriateImageUrl(list[position].mediaData[0], view.context))
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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val listItemViewBinding: ListItemRandomGifBinding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.list_item_random_gif, parent, false)
        return TrendingGifViewHolder(listItemViewBinding.root, listItemViewBinding)

    }


    fun setShouldLoadMore(boolean: Boolean) {
        shouldLoadMore = boolean
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun setGifList(list: List<GifResultsData>) {
        this.list = list
        notifyDataSetChanged()
    }


    class TrendingGifViewHolder : RecyclerView.ViewHolder {
        var itemGifBinding: ListItemRandomGifBinding

        constructor(itemView: View?, itemGifBinding: ListItemRandomGifBinding) : super(itemView) {
            this.itemGifBinding = itemGifBinding
        }

        fun bindGif(nano: GifResultsData, pos: Int, activity: Activity) {
            if (itemGifBinding.itemRandomGifModel == null) {
                itemGifBinding.itemRandomGifModel = CommonItemGifModel(activity, nano, pos , "")
            } else
                itemGifBinding.itemRandomGifModel!!.setGif(nano)
        }
    }

}