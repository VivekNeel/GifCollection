package com.gifs.collection.model.tenor.tags

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 08/10/17.
 */
data class TagData(@SerializedName("searchterm") val searchTerm : String, @SerializedName("image") val gif :String , @SerializedName("name") val name : String )