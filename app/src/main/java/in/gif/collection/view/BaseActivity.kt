package `in`.gif.collection.view

import `in`.gif.collection.hideKeyboard
import `in`.gif.collection.showKeyboard
import android.support.v7.app.AppCompatActivity
import android.view.View

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