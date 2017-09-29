package com.gifs.collection.custom

import com.gifs.collection.R
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet

/**
 * Created by vivek on 17/09/17.
 */
class MainToolbar(context: Context?, attrs: AttributeSet?) : CustomToolbar(context, attrs) {

    init {
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        setNavigationIcon(R.drawable.ic_search_black_24dp)
    }
}