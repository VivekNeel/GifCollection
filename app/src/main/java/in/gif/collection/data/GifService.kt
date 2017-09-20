package `in`.gif.collection.data

import `in`.gif.collection.BuildConfig
import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.RandomGifData
import `in`.gif.collection.model.TranslateData
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by vivek on 15/09/17.
 */
interface GifService {

    @Headers("accept: image/*")
    @GET("gifs/trending?limit=25&rating=G")
    fun fetchTrendingGif(@Query("offset") offset: Int, @Query("api_key") key: String = BuildConfig.API_KEY): Call<GifResponse>

    @Headers("accept: image/*")
    @GET("gifs/search?limit=25&offset=0&rating=G&lang=en")
    fun fetchSearchableGifs(@Query("q") query: String, @Query("api_key") key: String = BuildConfig.API_KEY): Call<GifResponse>

    @Headers("accept: image/*")
    @GET("gifs/translate")
    fun fetchTranslateGif(@Query("s") query: String, @Query("api_key") key: String = BuildConfig.API_KEY): Call<TranslateData>

    @Headers("accept: image/*")
    @GET("gifs/random?tag=&rating=G")
    fun fetchRandomGifs(@Query("tag") offset: String, @Query("api_key") key: String = BuildConfig.API_KEY): Call<RandomGifData>
}