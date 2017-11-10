package com.gifs.collection.model.tenor.tags

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 08/10/17.
 */
data class TagResponse(@SerializedName("tags") val tagList : List<TagData>)