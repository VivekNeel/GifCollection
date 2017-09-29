package com.gifs.collection.Utils

import com.gifs.collection.Constants
import com.gifs.collection.get
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by vivek on 21/09/17.
 */
inline fun <reified T> getList(json: String): T? =
        Gson().fromJson<T>(json, object : TypeToken<T>() {}.type)

class CommonUtils {
    companion object {
        fun isGifFavourited(id: String, context: Context): Boolean {
            val ids: MutableSet<String> = PreferenceHelper.defaultPrefs(context)[Constants.KEY_FAVOURITE]
            return ids.contains(id)
        }
    }
}