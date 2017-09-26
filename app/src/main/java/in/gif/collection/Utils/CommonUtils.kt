package `in`.gif.collection.Utils

import `in`.gif.collection.*
import android.content.Context
import android.os.Environment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

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

        fun showInterstitialAds(mInterstitialAd: InterstitialAd) {
            mInterstitialAd.adUnitId = "ca-app-pub-2885720067654353/6367966726"
            val builder = AdRequest.Builder()
            if (BuildConfig.DEBUG) {
                builder.addTestDevice("BB86E6BA9A2A567C9E68AC37E9755267")
            }
            mInterstitialAd.loadAd(builder.build())
            mInterstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    showInterstitial(mInterstitialAd)
                }
            }
        }

        private fun showInterstitial(interstitial: InterstitialAd) {
            if (interstitial.isLoaded) {
                interstitial.show()
            }
        }

        fun checkIfAlreadyDownloaded(id: String): Boolean {
            val root = Environment.getExternalStorageDirectory()
            val myDir = File("${root}${"/GifVideos"}")
            val file = File(myDir, id + ".mp4")
            if (file.exists()) {
                return true
            }
            return false
        }
    }

}