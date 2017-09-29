package com.gifs.collection.custom

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View

/**
 * Created by vivek on 16/09/17.
 */
open class CustomToolbar(context: Context?, attrs: AttributeSet?) : Toolbar(context, attrs) {

    fun hideContent() {
        for (i in 0..childCount - 1) {
            getChildAt(i).visibility = View.GONE
        }
    }

    open fun showContent() {
        for (i in 0..childCount - 1) {
            getChildAt(i).visibility = View.VISIBLE
        }
    }

}