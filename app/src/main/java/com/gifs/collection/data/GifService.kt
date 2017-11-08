package com.gifs.collection.data

import com.gifs.collection.BuildConfig
import com.gifs.collection.model.RandomGifData
import com.gifs.collection.model.TranslateData
import com.gifs.collection.model.tenor.HourlyTrendingData
import com.gifs.collection.model.tenor.MediaGifResponse
import com.gifs.collection.model.tenor.exploreterm.ExploreResponse
import com.gifs.collection.model.tenor.tags.TagResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by vivek on 15/09/17.
 */
interface GifService {

    @GET("trending")
    fun fetchTrendingGif(@Query("pos") offset: String?, @Query("key") apiKey: String = BuildConfig.API_KEY): Call<MediaGifResponse>

    @GET("search")
    fun fetchSearchableGifs(@Query("q") query: String?, @Query("pos") offset: String = "", @Query("key") apiKey: String = BuildConfig.API_KEY): Call<MediaGifResponse>

    @Headers("accept: image/*")
    @GET("gifs/translate")
    fun fetchTranslateGif(@Query("s") query: String, @Query("api_key") key: String = BuildConfig.API_KEY): Call<TranslateData>

    @Headers("accept: image/*")
    @GET("gifs/random?tag=&rating=G")
    fun fetchRandomGifs(@Query("tag") offset: String, @Query("api_key") key: String = BuildConfig.API_KEY): Call<RandomGifData>

    @GET("gifs")
    fun fetchFavourites(@Query("ids") listOfIds: String, @Query("key") apiKey: String = BuildConfig.API_KEY): Call<MediaGifResponse>

    @GET("autocomplete?type=trending")
    fun fetchHourlyTrendingTerms(@Query("key") apiKey: String = BuildConfig.API_KEY): Call<HourlyTrendingData>

    @GET("tags")
    fun fetchTags(@Query("type") type: String, @Query("key") apiKey: String = BuildConfig.API_KEY): Call<TagResponse>

    @GET("search")
    fun fetchTagSearchableGif(@Query("tag") tags: String?, @Query("pos") offset: String = "", @Query("key") apiKey: String = BuildConfig.API_KEY): Call<MediaGifResponse>

    @GET("exploreterm")
    fun exploreTerms(@Query("tag") tag: String?, @Query("key") apiKey: String = BuildConfig.API_KEY): Call<ExploreResponse>

}

