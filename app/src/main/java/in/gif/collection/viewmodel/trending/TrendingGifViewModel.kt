package `in`.gif.collection.viewmodel.trending

import `in`.gif.collection.GifApplication
import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.TrendingGifResponse
import android.content.Context
import android.databinding.ObservableInt
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by vivek on 15/09/17.
 */
class TrendingGifViewModel(context: Context) : Observable() {

    var gifProgress: ObservableInt = ObservableInt(View.GONE)
    var gifRecyclerView: ObservableInt = ObservableInt(View.GONE)
    private var gifList: ArrayList<TrendingGifResponse> = arrayListOf()
    private var context = context

    fun getData(offset: Int) {
        initializeViews()
        fetchTrendingGif(offset)
    }

    fun fetchTrendingGif(offset: Int) {
        val gifApp = GifApplication.createGifApplication(context)
        gifApp.getGifService().fetchTrendingGif(offset).enqueue(object : Callback<GifResponse> {
            override fun onResponse(call: Call<GifResponse>?, response: Response<GifResponse>?) {
                changeDataSet(response!!.body()!!.data)
                gifRecyclerView.set(View.VISIBLE)
                gifProgress.set(View.GONE)
            }

            override fun onFailure(call: Call<GifResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
            }
        })
    }

    fun initializeViews() {
        gifProgress.set(View.VISIBLE)
        gifRecyclerView.set(View.GONE)
    }

    fun changeDataSet(gifList: List<TrendingGifResponse>) {
        this.gifList.addAll(gifList)
        setChanged()
        notifyObservers()
    }

    fun getGifs(): List<TrendingGifResponse> {
        return gifList
    }
}