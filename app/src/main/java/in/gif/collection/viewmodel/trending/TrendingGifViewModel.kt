package `in`.gif.collection.viewmodel.trending

import `in`.gif.collection.Constants
import `in`.gif.collection.GifApplication
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.data.GifFactory
import `in`.gif.collection.get
import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.TrendingGifResponse
import `in`.gif.collection.model.tenor.GifResultsData
import `in`.gif.collection.model.tenor.HourlyTrendingData
import `in`.gif.collection.model.tenor.MediaGifResponse
import android.content.Context
import android.databinding.ObservableInt
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Created by vivek on 15/09/17.
 */
class TrendingGifViewModel(context: Context) : Observable() {

    var gifProgress: ObservableInt = ObservableInt(View.GONE)
    var gifRecyclerView: ObservableInt = ObservableInt(View.GONE)
    var emptyState : ObservableInt = ObservableInt(View.GONE)

    private var gifList: ArrayList<GifResultsData> = arrayListOf()
    private var trendingTerms: ArrayList<String> = arrayListOf()
    private var context = context
    private var next: String? = null

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

    fun getGifs(): List<GifResultsData> {
        return gifList
    }

    fun getTerms() : List<String>{
        return trendingTerms
    }

    fun getNext(): String? {
        return next
    }
}