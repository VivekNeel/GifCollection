package com.gifs.collection.model.tenor.exploreterm

import com.gifs.collection.model.tenor.tags.TagData
import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 08/10/17.
 */
data class ExpoloreTermData(@SerializedName("searches") val list: List<TagData>,  @SerializedName("searchterm") val searchTerm: String)