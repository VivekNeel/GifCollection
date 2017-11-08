package com.gifs.collection.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.view.View
import com.gifs.collection.Utils.NetworkUtil
import com.gifs.collection.model.tenor.GifResultsData
import com.gifs.collection.model.tenor.tags.TagData
import com.gifs.collection.view.GifDetailActivity

/**
 * Created by vivek on 15/09/17.
 */


class CommonItemGifModel(context: Context, gifData: GifResultsData, pos: Int, term: String) : BaseObservable() {
    private var tagData: TagData? = null

    constructor(tagData: TagData, context: Context) : this(context, GifResultsData(emptyList(), ""), 0, "") {
        this.tagData = tagData
    }

    private var context = context
    private var gif = gifData
    private var pos = pos
    private var term: String = term
    private var searchName: String = ""

    fun getSearchName(): String {
        return tagData?.name!!
    }

    fun setGif(gifData: GifResultsData) {
        this.gif = gifData
        notifyChange()
    }

    fun setTag(tagData: TagData) {
        this.tagData = tagData
        notifyChange()
    }

    fun setTrendingTermData(data: String) {
        this.term = data
        notifyChange()
    }

    fun getTerm(): String {
        return term
    }

    fun onItemClick(view: View) {
        //  var options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, ViewCompat.getTransitionName(view))
        context.startActivity(GifDetailActivity.launchDetail(context, NetworkUtil.getAppropriateImageUrl(gif.mediaData[0], context)))
    }

    //TODO
    fun onFavClicked(view: View) {

    }

    //TODO
    fun onTermsItemClicked(view: View) {
    }

}