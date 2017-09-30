package com.gifs.collection.Utils

import android.content.Context
import com.gifs.collection.BuildConfig
import com.gifs.collection.Constants
import com.gifs.collection.get
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_trending.*

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

        fun showInterstitialAds(mInterstitialAd: InterstitialAd) {
            mInterstitialAd.adUnitId = "ca-app-pub-2885720067654353/2790951272"
            val builder = AdRequest.Builder()
            if (BuildConfig.DEBUG) {
                builder.addTestDevice("871BCDCF1EF645445413FAFED12CC6E4")
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

        fun setupBanner(adView: AdView) {
            val adRequest = AdRequest.Builder()
            if (BuildConfig.DEBUG) {
                adRequest.addTestDevice("871BCDCF1EF645445413FAFED12CC6E4")
            }
            adView.loadAd(adRequest.build())
        }
    }
}