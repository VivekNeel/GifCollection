package `in`.gif.collection.data

import `in`.gif.collection.BuildConfig
import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.RandomGifData
import `in`.gif.collection.model.TranslateData
import `in`.gif.collection.model.tenor.MediaGifResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by vivek on 15/09/17.
 */
interface GifService {

    @GET("trending?key=LIVDSRZULELA")
    fun fetchTrendingGif(@Query("pos") offset: String?): Call<MediaGifResponse>

    @GET("search?key=LIVDSRZULELA")
    fun fetchSearchableGifs(@Query("q") query: String): Call<MediaGifResponse>

    @Headers("accept: image/*")
    @GET("gifs/translate")
    fun fetchTranslateGif(@Query("s") query: String, @Query("api_key") key: String = BuildConfig.API_KEY): Call<TranslateData>

    @Headers("accept: image/*")
    @GET("gifs/random?tag=&rating=G")
    fun fetchRandomGifs(@Query("tag") offset: String, @Query("api_key") key: String = BuildConfig.API_KEY): Call<RandomGifData>

    @GET("gifs?key=LIVDSRZULELA")
    fun fetchFavourites(@Query("ids") listOfIds: String): Call<MediaGifResponse>
}