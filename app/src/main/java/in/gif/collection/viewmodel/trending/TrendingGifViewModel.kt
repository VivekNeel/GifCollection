package `in`.gif.collection.viewmodel.trending

import `in`.gif.collection.Constants
import `in`.gif.collection.GifApplication
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.data.GifFactory
import `in`.gif.collection.data.db.StorageService
import `in`.gif.collection.get
import `in`.gif.collection.model.tenor.GifResultsData
import `in`.gif.collection.model.tenor.HourlyTrendingData
import `in`.gif.collection.model.tenor.MediaGifResponse
import `in`.gif.collection.model.youtube.ItemsData
import `in`.gif.collection.model.youtube.YoutubeSearchResponse
import android.content.Context
import android.databinding.ObservableInt
import android.text.TextUtils
import android.view.View
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
    private var videosData: ArrayList<ItemsData> = arrayListOf()
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

    fun fetchYoutubeVideos(query: String) {
        initializeViews()
        GifFactory.createYoutubeService().fetchSearcableVideos(query).enqueue(object : Callback<YoutubeSearchResponse> {
            override fun onFailure(call: Call<YoutubeSearchResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
                changeVideoDataSet(StorageService.getVideosFromDb())
                gifRecyclerView.set(View.VISIBLE)
            }

            override fun onResponse(call: Call<YoutubeSearchResponse>?, response: Response<YoutubeSearchResponse>?) {
                if (response != null && response.body() != null && response.body()!!.itemsData?.isNotEmpty()!!) {
                    for (item in response.body()!!.itemsData){
                        StorageService.putDataIntoDB(item)
                    }
                    changeVideoDataSet(StorageService.getVideosFromDb())
                    gifRecyclerView.set(View.VISIBLE)
                    gifProgress.set(View.GONE)
                } else {
                    gifProgress.set(View.GONE)
                    changeVideoDataSet(StorageService.getVideosFromDb())
                }
                gifRecyclerView.set(View.VISIBLE)
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

    fun changeVideoDataSet(gifList: List<ItemsData>) {
        this.videosData.addAll(gifList)
        setChanged()
        notifyObservers()
    }

    fun getGifs(): List<GifResultsData> {
        return gifList
    }

    fun getVideos(): List<ItemsData> {
        return videosData
    }

    fun getTerms(): List<String> {
        return trendingTerms
    }

    fun getNext(): String? {
        return next
    }
}