package com.gifs.collection.view

import android.app.Activity
import android.databinding.DataBindingUtil.inflate
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gifs.collection.*
import com.gifs.collection.utils.CommonUtils
import com.gifs.collection.utils.PreferenceHelper
import com.gifs.collection.anayltics.CustomAnayltics
import com.gifs.collection.databinding.ListItemRandomGifBinding
import com.gifs.collection.model.tenor.GifResultsData
import com.gifs.collection.viewmodel.CommonItemGifModel
import kotlinx.android.synthetic.main.list_item_random_gif.view.*
import java.lang.Exception
import com.gifs.collection.databinding.ListItemTagsBinding
import com.gifs.collection.model.tenor.exploreterm.ExpoloreTermData
import com.gifs.collection.model.tenor.tags.TagData


/**
 * Created by vivek on 15/09/17.
 */

class TrendingGifAdapter(activity: Activity, type: String = "", callback: ITagItemClickedCallback? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<GifResultsData> = emptyList()
    private var tagList: java.util.ArrayList<TagData> = ArrayList()
    private var tagSearchList: java.util.ArrayList<TagData> = arrayListOf()
    private var exploreList: List<ExpoloreTermData> = emptyList()

    val activity: Activity = activity
    val VIEW_TYPE_EXPLORE = 1
    val VIEW_TYPE_TAGS = 3
    val VIEW_TYPE_CONTENT = 2
    private var type = type
    private var callback = callback


    fun doSearch(query: String) {
        tagSearchList.clear()
        tagList
                .filter { it.name.toLowerCase().contains(query.toLowerCase()) }
                .forEach { tagSearchList.add(it) }

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder?.itemViewType) {
            VIEW_TYPE_CONTENT -> {
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
                            val oldSet = HashSet<String>(PreferenceHelper.defaultPrefs(view.context).getStringSet(Constants.KEY_FAVOURITE, HashSet<String>()))
                            if (oldSet.contains(list[position].id)) {
                                oldSet.remove(list[position].id)
                                holder.itemView.fav.setBackgroundResource(R.drawable.fav_unselected)
                                val newSet: Set<String> = oldSet
                                view.context.toast("Removed from favourites :(")
                                CustomAnayltics.logCustom("Removed from fav")
                                PreferenceHelper.defaultPrefs(view.context)[Constants.KEY_FAVOURITE] = newSet

                            } else {
                                oldSet.add(list[position].id)
                                val newSet: Set<String> = oldSet
                                holder.itemView.fav.setBackgroundResource(R.drawable.fav_selected)
                                view.context.toast("Added to favourites!")
                                CustomAnayltics.logCustom("Added to favourites")
                                PreferenceHelper.defaultPrefs(view.context)[Constants.KEY_FAVOURITE] = newSet
                            }
                        }
                    }

                }
            }

            VIEW_TYPE_TAGS -> {
                with(holder as TagsViewHolder) {
                    holder.itemGifBinding.progress.visibility = View.VISIBLE
                    val view = holder.itemGifBinding.gifIv
                    holder.itemView.gifIv.isClickable = false
                    Glide.with(view.context)
                            .load(tagSearchList[position].gif)
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .listener(object : RequestListener<String, GifDrawable> {
                                override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                    holder.itemGifBinding.progress.visibility = View.GONE
                                    holder.itemView.gifIv.isClickable = true
                                    return false
                                }

                                override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }
                            }).into(view)

                    holder.bindGif(tagSearchList[holder.adapterPosition])
                    view.setOnClickListener {
                        callback?.onTagItemClicked(tagSearchList[position].searchTerm)
                    }

                }

            }

            VIEW_TYPE_EXPLORE -> {
                with(holder as TagsViewHolder) {
                    holder.itemGifBinding.progress.visibility = View.VISIBLE
                    val view = holder.itemGifBinding.gifIv
                    holder.itemView.gifIv.isClickable = false
                    Glide.with(view.context)
                            .load(exploreList[position].list[position].gif)
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .listener(object : RequestListener<String, GifDrawable> {
                                override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                    holder.itemGifBinding.progress.visibility = View.GONE
                                    holder.itemView.gifIv.isClickable = true
                                    return false
                                }

                                override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                                    return false
                                }
                            }).into(view)

                    // holder.bindGif(exploreList[holder.adapterPosition])
                    view.setOnClickListener {
                        callback?.onTagItemClicked(tagList[position].searchTerm)
                    }

                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var viewHolder: RecyclerView.ViewHolder? = null
        if (viewType == VIEW_TYPE_CONTENT) {
            val listItemViewBinding: ListItemRandomGifBinding = inflate(LayoutInflater.from(parent?.context), R.layout.list_item_random_gif, parent, false)
            viewHolder = TrendingGifViewHolder(listItemViewBinding.root, listItemViewBinding)
        } else if (viewType == VIEW_TYPE_TAGS || viewType == VIEW_TYPE_EXPLORE) {
            val binding: ListItemTagsBinding = inflate(LayoutInflater.from(parent?.context), R.layout.list_item_tags, parent, false)
            viewHolder = TagsViewHolder(binding.root, binding)
        }
        return viewHolder

    }


    override fun getItemViewType(position: Int): Int {
        when (type) {
            "tags" -> {
                return VIEW_TYPE_TAGS
            }
            "explore" -> {
                return VIEW_TYPE_EXPLORE
            }
            else -> {
                return VIEW_TYPE_CONTENT
            }
        }
    }

    override fun getItemCount(): Int {

        when (type) {
            "tags" -> {
                return tagSearchList.size
            }
            "explore" -> {
                return exploreList.size
            }
            else -> {
                return list.size
            }
        }
    }

    fun setGifList(list: List<GifResultsData>) {
        this.list = list
        notifyDataSetChanged()
    }

    //TODO Make it generic
    fun setTagList(list: ArrayList<TagData>) {
        this.tagList = java.util.ArrayList<TagData>(list)
        this.tagSearchList = java.util.ArrayList(list)
        notifyDataSetChanged()
    }

    //TODO Make it generic
    fun setExploreTermList(list: List<ExpoloreTermData>) {
        this.exploreList = list
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

    class TagsViewHolder(itemView: View?, var itemGifBinding: ListItemTagsBinding) : RecyclerView.ViewHolder(itemView) {
        fun bindGif(tagData: TagData) {
            if (itemGifBinding.itemRandomGifModel == null) {
                itemGifBinding.itemRandomGifModel = CommonItemGifModel(tagData, itemView.context)
            } else itemGifBinding.itemRandomGifModel!!.setTag(tagData)
        }
    }

}