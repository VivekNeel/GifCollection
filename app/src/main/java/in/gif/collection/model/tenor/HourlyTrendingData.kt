package `in`.gif.collection.model.tenor

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 21/09/17.
 */
class HourlyTrendingData(@SerializedName("results") val list: List<String>)