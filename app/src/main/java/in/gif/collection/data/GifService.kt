package `in`.gif.collection.data

import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.TrendingGifResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by vivek on 15/09/17.
 */
interface GifService {

    @GET("gifs/trending?api_key=61dfb1c03a8440b891d9661b2521214c&limit=25&rating=G")
    fun fetchTrendingGif(@Query("offset") offset: Int): Call<GifResponse>
}