package com.gifs.collection.utils

import android.content.Context
import android.telephony.TelephonyManager
import com.gifs.collection.model.tenor.MediaGifData


/**
 * Created by vivek on 21/09/17.
 */
class NetworkUtil {

    companion object {

        fun getNetworkType(context: Context): String {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val networkType = telephonyManager.networkType
            when (networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN ->
                    return "2g"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP ->
                    return "3g"
                TelephonyManager.NETWORK_TYPE_LTE ->
                    return "4g"
                else -> return "4g"
            }
        }

        fun getAppropriateImageUrl(mediaGifData: MediaGifData, context: Context): String {
            when (getNetworkType(context)) {
                "2g" -> return mediaGifData.nano.url
                "3g", "4g" -> return mediaGifData.highGif.url
                else -> return mediaGifData.highGif.url
            }
        }
    }
}