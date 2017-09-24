package `in`.gif.collection.view

import `in`.gif.collection.IButtonClickedCallback
import `in`.gif.collection.IVideoClickedCallback
import `in`.gif.collection.R
import `in`.gif.collection.Utils.CommonUtils
import `in`.gif.collection.databinding.ItemStaticRelatedViewBinding
import `in`.gif.collection.databinding.ListItemReleatedVideosBinding
import `in`.gif.collection.model.youtube.ItemsData
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_static_related_view.view.*
import kotlinx.android.synthetic.main.list_item_releated_videos.view.*

/**
 * Created by vivek on 24/09/17.
 */
class ReleatedViewsAdapter(callback: IVideoClickedCallback, buttonCallback: IButtonClickedCallback, title: String = "") : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ListItemReleatedVideosBinding
    private lateinit var staticBinding: ItemStaticRelatedViewBinding
    private val callback = callback
    private val title = title
    private val buttonCallback = buttonCallback

    private var videoList: List<ItemsData> = emptyList()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = videoList[holder.adapterPosition].snippetData
        when (holder.itemViewType) {
            0 -> {
                val holder = holder as StaticViewHolder
                holder.itemView.detailVideotitle.text = title
                holder.itemView.downloadButton.setOnClickListener {
                    buttonCallback.onDownloadButtonClicked()
                }

                holder.itemView.shareButton.setOnClickListener { buttonCallback.onShareButtonClicked() }
            }
            1 -> {
                with((holder as ViewHolder).itemView) {
                    Glide.with(context).load(itemData?.thumbnails?.medium?.url).into(releatedVideoThumbnail)
                    releatedVideoTitle.text = itemData?.title
                    relatedVideoDetails.text = ""
                    releatedVideoThumbnail.setOnClickListener { callback.onItemClicked(videoList[position]) }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (videoList[position].holderType.equals("static")) {
            return 0
        } else {
            return 1
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {

        when (viewType) {
            0 -> {
                staticBinding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.item_static_related_view, parent, false)
                return StaticViewHolder(staticBinding.root)
            }
            1 -> {
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.list_item_releated_videos, parent, false)
                return ViewHolder(binding.root)
            }
        }
        return null
    }

    fun setVideoList(list: List<ItemsData>) {
        this.videoList = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    class StaticViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}