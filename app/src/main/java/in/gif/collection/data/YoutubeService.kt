package `in`.gif.collection.data

import `in`.gif.collection.BuildConfig
import `in`.gif.collection.model.youtube.YoutubeSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by vivek on 23/09/17.
 */
interface YoutubeService {

    @GET("search?part=snippet")
    fun fetchSearcableVideos(@Query("q") query: String, @Query("maxResults") limit: Int = 20, @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY)
            : Call<YoutubeSearchResponse>
}