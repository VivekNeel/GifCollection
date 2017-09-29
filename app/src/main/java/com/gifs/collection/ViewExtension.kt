package com.gifs.collection

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.support.annotation.BoolRes
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

/**
 * Created by vivek on 15/09/17.
 */
fun ImageView.loadImage(url: String) {
    Glide.with(context)
            .load(url).asGif().into(this)
}

fun Context.showKeyboard() {
    val inputMethodManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}


fun Context.hideKeyboard() {
    val inputMethodManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Context.runOnM(run: () -> Unit) {
    if (SDK_INT >= Build.VERSION_CODES.M) {
        run()
    }
}

fun Context.runOnKK(run: () -> Unit) {
    if (SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        run()
    }
}

fun AppCompatActivity.commitFragment(fragment: Fragment, @IdRes containerId: Int, tag: String, @BoolRes allowStateLoss: Boolean = false, @BoolRes addToBackStack: Boolean = false) {
    val ft = supportFragmentManager.beginTransaction().replace(containerId, fragment, tag)
    if (!supportFragmentManager.isStateSaved) {
        if (addToBackStack)
            ft.addToBackStack(null)
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun Context.fetchColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.clear()
    editor.apply()
}

operator inline fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is MutableSet<*> -> edit { it.putStringSet(key, value as MutableSet<String>?) }
        else -> throw UnsupportedOperationException("unsupported data type")
    }
}

operator inline fun <reified T : Any> SharedPreferences.get(key: String, value: T? = null): T {
    return when (T::class) {
        String::class -> getString(key, value as? String ?: "") as T
        Int::class -> getInt(key, value as? Int ?: -1) as T
        Boolean::class -> getBoolean(key, value as?  Boolean ?: false) as T
        MutableSet::class -> getStringSet(key, value as? MutableSet<String> ?: mutableSetOf()) as T
        else -> throw UnsupportedOperationException("unsupported data type")
    }
}

fun Context.toast(msg: String) {
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
}