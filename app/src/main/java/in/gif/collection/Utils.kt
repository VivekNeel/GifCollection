package `in`.gif.collection

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by vivek on 18/09/17.
 */

inline fun <reified T> getList(json: String): T? =
        Gson().fromJson<T>(json, object : TypeToken<T>() {}.type)

