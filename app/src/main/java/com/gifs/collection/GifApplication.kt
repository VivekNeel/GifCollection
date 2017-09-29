package com.gifs.collection

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.gifs.collection.data.GifFactory
import com.gifs.collection.data.GifService
import io.fabric.sdk.android.Fabric


/**
 * Created by vivek on 15/09/17.
 */
class GifApplication : Application() {

    private var gifService: GifService? = null

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }



    companion object {
        private fun getGifApplication(context: Context): GifApplication {
            return context.applicationContext as GifApplication
        }

        fun createGifApplication(context: Context): GifApplication {
            return getGifApplication(context)
        }

        fun getApplicationContext() : Context{
            return GifApplication.getApplicationContext()
        }
    }

    fun getGifService(): GifService {
        if (gifService == null) gifService = GifFactory.create()
        return gifService as GifService
    }


}