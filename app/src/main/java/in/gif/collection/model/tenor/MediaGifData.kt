package `in`.gif.collection.model.tenor

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 21/09/17.
 */

// nanogif for 2g
// gif for 3g or 4g
class MediaGifData(@SerializedName("nanogif") val nano: GifData, @SerializedName("gif") val highGif : GifData)