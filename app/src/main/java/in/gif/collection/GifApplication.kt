package `in`.gif.collection

import `in`.gif.collection.data.GifFactory
import `in`.gif.collection.data.GifService
import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.realm.Realm


/**
 * Created by vivek on 15/09/17.
 */
class GifApplication : Application() {

    private var gifService: GifService? = null

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Realm.init(this)
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