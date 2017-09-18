package `in`.gif.collection.model

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 18/09/17.
 */
data class RandomGifData(@SerializedName("data") var data: RandomGifResponse)