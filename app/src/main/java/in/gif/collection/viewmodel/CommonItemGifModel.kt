package `in`.gif.collection.viewmodel

import `in`.gif.collection.model.TrendingGifResponse
import `in`.gif.collection.view.GifDetailActivity
import android.app.Activity
import android.databinding.BaseObservable
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.view.View

/**
 * Created by vivek on 15/09/17.
 */


class CommonItemGifModel(context: Activity, gifData: TrendingGifResponse, pos: Int) : BaseObservable() {

    private var context = context
    private var gif = gifData
    private var pos = pos

    fun getId(): String {
        return gif.id
    }

    fun getImageUri(): String {
        return this.gif.images.fixedHeightGifs.url
    }

    fun setGif(gifData: TrendingGifResponse) {
        this.gif = gifData
        notifyChange()
    }

    fun onItemClick(view: View) {
        var options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, ViewCompat.getTransitionName(view))
        context.startActivity(GifDetailActivity.launchDetail(context, gifData = gif))
    }

}