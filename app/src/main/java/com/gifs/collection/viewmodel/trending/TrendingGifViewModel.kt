package com.gifs.collection.viewmodel.trending

import android.content.Context
import android.databinding.ObservableInt
import android.text.TextUtils
import android.view.View
import com.gifs.collection.Constants
import com.gifs.collection.GifApplication
import com.gifs.collection.utils.PreferenceHelper
import com.gifs.collection.data.GifFactory
import com.gifs.collection.get
import com.gifs.collection.model.tenor.GifResultsData
import com.gifs.collection.model.tenor.HourlyTrendingData
import com.gifs.collection.model.tenor.MediaGifResponse
import com.gifs.collection.model.tenor.exploreterm.ExploreResponse
import com.gifs.collection.model.tenor.tags.TagData
import com.gifs.collection.model.tenor.tags.TagResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by vivek on 15/09/17.
 */
class TrendingGifViewModel(context: Context) : Observable() {

    var gifProgress: ObservableInt = ObservableInt(View.GONE)
    var gifRecyclerView: ObservableInt = ObservableInt(View.GONE)
    var emptyState: ObservableInt = ObservableInt(View.GONE)

    private var gifList: ArrayList<GifResultsData> = arrayListOf()
    private var trendingTerms: ArrayList<String> = arrayListOf()
    private var context = context
    private var next: String? = null
    private var tagList: ArrayList<TagData> = arrayListOf()
    private var exploreResponse: ExploreResponse? = null


    fun getData(offset: String) {
        initializeViews()
        fetchTrendingGif(offset)
    }

    fun fetchTrendingGif(offset: String?) {
        val gifApp = GifApplication.createGifApplication(context)
        gifApp.getGifService().fetchTrendingGif(offset).enqueue(object : Callback<MediaGifResponse> {
            override fun onResponse(call: Call<MediaGifResponse>?, response: Response<MediaGifResponse>?) {
                var mediaGifResponse = response?.body()?.results
                if (mediaGifResponse != null) {
                    changeDataSet(mediaGifResponse, response?.body()?.next)
                    gifRecyclerView.set(View.VISIBLE)
                    gifProgress.set(View.GONE)
                }
            }

            override fun onFailure(call: Call<MediaGifResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
            }
        })
    }

    fun fetchFavouritesGifs() {
        initializeViews()
        val ids: MutableSet<String> = PreferenceHelper.defaultPrefs(context)[Constants.KEY_FAVOURITE]
        GifFactory.create().fetchFavourites(TextUtils.join(", ", ids.toList())).enqueue(object : Callback<MediaGifResponse> {
            override fun onFailure(call: Call<MediaGifResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
                emptyState.set(View.VISIBLE)
            }

            override fun onResponse(call: Call<MediaGifResponse>?, response: Response<MediaGifResponse>?) {
                if (response!!.body()!!.results != null) {
                    changeDataSet(response!!.body()!!.results!!, response.body()?.next)
                    gifRecyclerView.set(View.VISIBLE)
                    gifProgress.set(View.GONE)
                } else {
                    gifProgress.set(View.GONE)
                    emptyState.set(View.VISIBLE)
                }
            }
        })
    }

    fun fetchTrendingTerms() {
        initializeViews()
        GifFactory.create().fetchHourlyTrendingTerms().enqueue(object : Callback<HourlyTrendingData> {
            override fun onFailure(call: Call<HourlyTrendingData>?, t: Throwable?) {
                gifProgress.set(View.GONE)
            }

            override fun onResponse(call: Call<HourlyTrendingData>?, response: Response<HourlyTrendingData>?) {
                if (response!!.body()!!.list != null) {
                    changeDataSet(response!!.body()!!.list)
                    gifRecyclerView.set(View.VISIBLE)
                    gifProgress.set(View.GONE)
                } else {
                    gifProgress.set(View.GONE)
                }
            }
        })
    }

    fun fetchTags(type : String) {
        initializeViews()
        GifFactory.create().fetchTags(type).enqueue(object : Callback<TagResponse> {
            override fun onFailure(call: Call<TagResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
                emptyState.set(View.VISIBLE)
            }

            override fun onResponse(call: Call<TagResponse>?, response: Response<TagResponse>?) {
                if (response!!.isSuccessful) {
                    changeTagDataSet(response.body()!!.tagList)
                    gifRecyclerView.set(View.VISIBLE)
                    gifProgress.set(View.GONE)
                } else {
                    gifProgress.set(View.GONE)
                }
            }
        })
    }

    fun fetchExploreTerm(tag: String?) {
        initializeViews()
        GifFactory.create().exploreTerms(tag).enqueue(object : Callback<ExploreResponse> {
            override fun onResponse(call: Call<ExploreResponse>?, response: Response<ExploreResponse>?) {
                if (response!!.isSuccessful) {
                    changeExploreTermDataSet(response!!.body()!!)
                    gifRecyclerView.set(View.VISIBLE)
                } else {
                    changeExploreTermDataSet(ExploreResponse(emptyList()))
                }
            }

            override fun onFailure(call: Call<ExploreResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
                emptyState.set(View.VISIBLE)
            }
        })
    }


    fun initializeViews() {
        gifProgress.set(View.VISIBLE)
        gifRecyclerView.set(View.GONE)
    }

    fun changeDataSet(gifList: List<GifResultsData>, next: String?) {
        this.gifList.addAll(gifList)
        this.next = next
        setChanged()
        notifyObservers()
    }

    fun changeDataSet(gifList: List<String>) {
        this.trendingTerms.addAll(gifList)
        setChanged()
        notifyObservers()
    }


    fun changeTagDataSet(tagList: List<TagData>) {
        this.tagList.addAll(tagList)
        setChanged()
        notifyObservers()
    }

    fun changeExploreTermDataSet(exploreSearches: ExploreResponse) {
        this.exploreResponse = exploreSearches
        setChanged()
        notifyObservers()
    }

    fun getGifs(): List<GifResultsData> {
        return gifList
    }

    fun getTerms(): List<String> {
        return trendingTerms
    }

    fun getNext(): String? {
        return next
    }

    fun getTags(): List<TagData> {
        return tagList
    }

    fun getExploreTerms(): ExploreResponse? {
        return exploreResponse
    }
}