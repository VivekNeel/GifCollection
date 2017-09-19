package `in`.gif.collection.viewmodel.search

import `in`.gif.collection.data.GifFactory
import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.TrendingGifResponse
import android.databinding.ObservableInt
import android.view.View
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
    var gifs: ArrayList<TrendingGifResponse> = arrayListOf()

    var noGifContainerVisibility: ObservableInt = ObservableInt(View.VISIBLE)

    fun initialiseViews() {
        gifProgress.set(View.VISIBLE)
        gifRecyclerView.set(View.GONE)
        noGifContainerVisibility.set(View.GONE)
    }

    fun fetchSearchableGif(search: String) {
        initialiseViews()
        GifFactory.create().fetchSearchableGifs(search).enqueue(object : Callback<GifResponse> {
            override fun onFailure(call: Call<GifResponse>?, t: Throwable?) {
                gifProgress.set(View.GONE)
                noGifContainerVisibility.set(View.VISIBLE)
            }

            override fun onResponse(call: Call<GifResponse>?, response: Response<GifResponse>?) {
                gifProgress.set(View.GONE)
                if (response != null) {
                    if (response.body()?.data!!.isNotEmpty()) {
                        changeDataSet(response.body()!!.data)
                        gifRecyclerView.set(View.VISIBLE)
                    } else {
                        noGifContainerVisibility.set(View.VISIBLE)
                        gifRecyclerView.set(View.GONE)
                    }
                }

            }
        })
    }

    fun changeDataSet(gifs: List<TrendingGifResponse>) {
        this.gifs.clear()
        this.gifs.addAll(gifs)
        setChanged()
        notifyObservers()
    }

    fun getGifs(): List<TrendingGifResponse> {
        return gifs
    }
}