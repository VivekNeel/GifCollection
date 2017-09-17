package `in`.gif.collection

import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.support.annotation.BoolRes
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by vivek on 15/09/17.
 */
fun ImageView.loadImage(url: String) {
    Glide.with(context)
            .load(url).asGif().into(this)
}

fun Context.showKeyboard() {
    var inputMethodManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}


fun Context.hideKeyboard() {
    var inputMethodManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Context.runOnApiLevelLessThanLollipop(run: () -> Unit) {
    if (SDK_INT < LOLLIPOP) {
        run()
    }
}

fun AppCompatActivity.commitFragment(fragment: Fragment, @IdRes containerId: Int, tag: String, @BoolRes allowStateLoss: Boolean = false) {
    val ft = supportFragmentManager.beginTransaction().replace(containerId, fragment, tag)
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun Context.fetchColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}
