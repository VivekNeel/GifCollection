package `in`.gif.collection

import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.rx.RxBus
import `in`.gif.collection.rx.events.Events
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.support.v4.content.FileProvider
import java.io.File

/**
 * Created by vivek on 24/09/17.
 */
class DownloadListener : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
   //     RxBus.sendEvent(Events().DownloadCompleteEvent())
    }
}