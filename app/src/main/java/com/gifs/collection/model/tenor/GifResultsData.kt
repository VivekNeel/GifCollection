package com.gifs.collection.model.tenor

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 21/09/17.
 */
class GifResultsData(@SerializedName("media") val mediaData: List<MediaGifData> ,  @SerializedName("id") val id : String)