package com.gifs.collection.model

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 18/09/17.
 */
class RandomGifResponse(@SerializedName("image_url") var url: String)