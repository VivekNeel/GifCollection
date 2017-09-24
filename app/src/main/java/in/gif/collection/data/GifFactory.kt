package `in`.gif.collection.data

import `in`.gif.collection.GifApplication
import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.youtube.player.internal.e
import android.support.v7.app.AppCompatActivity
import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException


/**
 * Created by vivek on 15/09/17.
 */
class GifFactory {

    companion object {

        fun create(): GifService {
            val retrofit = Retrofit.Builder().baseUrl("https://api.tenor.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(GifService::class.java)
        }

        fun createYoutubeService(context: Context): YoutubeService {
            val cacheSize = 10 * 1024 * 1024 // 10 MB
            val cache = Cache(context.applicationContext.cacheDir, cacheSize.toLong())

            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            val okHttpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(logInterceptor)
                    .build()


            val retrofit = Retrofit.Builder().baseUrl("https://www.googleapis.com/youtube/v3/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(YoutubeService::class.java)
        }

    }
}