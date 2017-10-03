package com.gifs.collection.viewmodel.search

import android.databinding.ObservableInt
import android.text.TextUtils
import android.view.View
import com.gifs.collection.data.GifFactory
import com.gifs.collection.model.tenor.GifResultsData
import com.gifs.collection.model.tenor.MediaGifResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by vivek on 17/09/17.
 */
class SearchGifViewModel : Observable() {

    var gifProgress: ObservableInt = ObservableInt(View.GONE)
    var gifRecyclerView: ObservableInt = ObservableInt(View.GONE)
    var gifs: ArrayList<GifResultsData> = arrayListOf()
    private var next: String? = null

    var noGifContainerVisibility: ObservableInt = ObservableInt(View.VISIBLE)

    fun initialiseViews() {
        gifProgress.set(View.VISIBLE)
        gifRecyclerView.set(View.GONE)
        noGifContainerVisibility.set(View.GONE)
    }

    fun fetchSearchableGif(search: String?, offset: String = "") {
        if (TextUtils.isEmpty(offset)) {
            initialiseViews()
        }
        GifFactory.create().fetchSearchableGifs(search, offset = offset).enqueue(object : Callback<MediaGifResponse> {
            override fun onFailure(call: Call<MediaGifResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
                noGifContainerVisibility.set(View.VISIBLE)
            }

            override fun onResponse(call: Call<MediaGifResponse>?, response: Response<MediaGifResponse>?) {
                gifProgress.set(View.GONE)
                if (response != null) {
                    if (response.body()?.results != null && response.body()!!.results!!.isNotEmpty()) {
                        changeDataSet(response.body()!!.results!!, response.body()!!.next)
                        gifRecyclerView.set(View.VISIBLE)
                    } else {
                        noGifContainerVisibility.set(View.VISIBLE)
                        gifRecyclerView.set(View.GONE)
                    }
                }

            }
        })
    }

    fun changeDataSet(gifs: List<GifResultsData>, next: String) {
        this.gifs.addAll(gifs)
        this.next = next
        setChanged()
        notifyObservers()
    }

    fun getGifs(): List<GifResultsData> {
        return gifs
    }

    fun getNext(): String? {
        return next
    }
}