package com.gifs.collection.custom

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by vivek on 17/09/17.
 */
open class CustomTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}