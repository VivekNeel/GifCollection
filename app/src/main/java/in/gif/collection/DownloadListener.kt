package `in`.gif.collection

import `in`.gif.collection.Utils.PreferenceHelper
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
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root + "/GifVideos")

        myDir.mkdirs()
        val downloadId: String = PreferenceHelper.defaultPrefs(p0!!)["downloadId"]

        val file = File(myDir, downloadId + ".mp4")

        val imageUri = FileProvider.getUriForFile(p0, BuildConfig.APPLICATION_ID + ".provider", file)

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "video/*"
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Video")
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy the Video")
        p0?.startActivity(Intent.createChooser(sendIntent, "Share:"))
    }
}