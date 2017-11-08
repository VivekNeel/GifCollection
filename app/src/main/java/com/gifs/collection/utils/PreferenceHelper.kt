@file: JvmName("PreferenceHelper.kt")
package com.gifs.collection.utils


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by vivek on 19/09/17.
 */
object PreferenceHelper {

    fun defaultPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    fun customPrefs(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

}