package `in`.gif.collection.data

import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.RandomGifData
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by vivek on 15/09/17.
 */
interface GifService {

    @Headers("accept: image/*")
    @GET("gifs/trending?api_key=61dfb1c03a8440b891d9661b2521214c&limit=25&rating=G")
    fun fetchTrendingGif(@Query("offset") offset: Int): Call<GifResponse>

    @Headers("accept: image/*")
    @GET("gifs/search?api_key=61dfb1c03a8440b891d9661b2521214c&q=&limit=25&offset=0&rating=G&lang=en")
    fun fetchSearchableGifs(@Query("q") query: String): Call<GifResponse>

    @Headers("accept: image/*")
    @GET("gifs/translate?api_key=61dfb1c03a8440b891d9661b2521214c")
    fun fetchTranslateGif(@Query("s") query: String): Call<GifResponse>

    @Headers("accept: image/*")
    @GET("gifs/random?api_key=61dfb1c03a8440b891d9661b2521214c&tag=&rating=G")
    fun fetchRandomGifs(@Query("tag") offset: String): Call<RandomGifData>
}