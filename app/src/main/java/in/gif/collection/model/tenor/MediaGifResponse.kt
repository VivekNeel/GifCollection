package `in`.gif.collection.model.tenor

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 21/09/17.
 */
class MediaGifResponse(@SerializedName("results") val results: List<GifResultsData>?, @SerializedName("next") val next: String)