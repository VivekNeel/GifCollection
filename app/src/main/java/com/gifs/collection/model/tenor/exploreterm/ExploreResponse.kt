package com.gifs.collection.model.tenor.exploreterm

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 08/10/17.
 */
data class ExploreResponse(@SerializedName("results") val list: List<ExpoloreTermData>)