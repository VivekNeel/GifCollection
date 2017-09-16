package `in`.gif.collection.model

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 15/09/17.
 */
data class TrendingGifResponse(@SerializedName("id") var id: String, @SerializedName("type") var type: String
                               , @SerializedName("images") var images: ImageData)