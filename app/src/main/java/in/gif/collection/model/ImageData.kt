package `in`.gif.collection.model

import com.google.gson.annotations.SerializedName

/**
 * Created by vivek on 15/09/17.
 */
data class ImageData(@SerializedName("fixed_width_small") var fixedHeightGifs: FixedHeightDownSampled
                     , @SerializedName("fixed_width") var fixedWidth: FixedWidthDetail)