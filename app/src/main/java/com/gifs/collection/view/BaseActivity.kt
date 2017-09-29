package com.gifs.collection.view

import com.gifs.collection.hideKeyboard
import com.gifs.collection.showKeyboard
import android.support.v7.app.AppCompatActivity

/**
 * Created by vivek on 17/09/17.
 */
open class BaseActivity : AppCompatActivity() {

    fun hideKb(){
        this.hideKeyboard()
    }

    fun showKb(){
        this.showKeyboard()
    }
}