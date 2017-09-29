package com.gifs.collection.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import com.gifs.collection.R

/**
 * Created by vivek on 16/09/17.
 */
class CustomSearchBar(context: Context?, attrs: AttributeSet?) : CustomToolbar(context, attrs) {

    private var editText: EditText? = null

    init {
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        setNavigationIcon(R.drawable.arrow_back_black)
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        var view: View = inflate(context, R.layout.merge_search, this)
        editText = findViewById(R.id.toolbar_search_edittext)
    }

    override fun showContent() {
        super.showContent()
        editText?.requestFocus()
    }

    fun clearText() {
        editText?.setText(null)
    }

}