package `in`.gif.collection.custom

import `in`.gif.collection.R
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.EditText

/**
 * Created by vivek on 16/09/17.
 */
class CustomSearchBar(context: Context?, attrs: AttributeSet?) : CustomToolbar(context, attrs) {

    private var editText: EditText? = null

    init {
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        setNavigationIcon(R.drawable.ic_arrow_back)
    }


     override fun onFinishInflate() {
        super.onFinishInflate()
        inflate(context, R.layout.merge_search, this)
        editText = findViewById(R.id.toolbar_search_edittext) as EditText
    }

    override fun showContent() {
        super.showContent()
        editText?.requestFocus()
    }

    fun clearText() {
        editText?.setText(null)
    }

}