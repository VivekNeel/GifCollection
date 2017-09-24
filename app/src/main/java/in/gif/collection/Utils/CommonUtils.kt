package `in`.gif.collection.Utils

import `in`.gif.collection.Constants
import `in`.gif.collection.get
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

        fun convertISO8601_FORMAT(value: String): String {
            try {

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val past = format.parse(value)
                val now = Date()

                val min = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
                val hr = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)

                if (hr > 24) {
                    return "${hr.toString()} ${"hr ago"}"
                } else {
                    return "${min.toString()} ${"mins ago"}"

                }
            } catch (e: ParseException) {

            }

            return ""

        }
    }
}