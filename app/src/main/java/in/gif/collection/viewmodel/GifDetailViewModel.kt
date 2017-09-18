package `in`.gif.collection.viewmodel

import `in`.gif.collection.R
import `in`.gif.collection.data.GifFactory
import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.RandomGifData
import `in`.gif.collection.model.RandomGifResponse
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import android.os.Environment
import android.support.v7.app.NotificationCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * Created by vivek on 15/09/17.
 */

@BindingAdapter("lottie_fileName")
fun bindLottie(view: LottieAnimationView, fileName: String) {
    view.setAnimation(fileName)
    view.playAnimation()
}

class GifDetailViewModel(context: Context, url: String = "") : Observable() {

    private var url = url
    private var context = context
    var fileName: ObservableField<String> = ObservableField("check_fit.json")
    var downloadProgress: ObservableInt = ObservableInt(View.GONE)
    var downloadButton: ObservableInt = ObservableInt(View.VISIBLE)
    var mBuilder = NotificationCompat.Builder(context)
    var mNotificationManager: NotificationManager? = null
    var translateViewVisibility: ObservableInt = ObservableInt(View.GONE)
    var detailViewVisibility: ObservableInt = ObservableInt(View.GONE)

    var singleGifSubmitButtonText: ObservableField<String> = ObservableField("Done")
    var randomgSingleGifSubmitButtonText: ObservableField<String> = ObservableField("Done")

    var fieldText: ObservableField<String> = ObservableField("some")
    var singleGifDownloadButtonVisibility: ObservableInt = ObservableInt(View.GONE)

    fun getImageUrl(): String {
        return url
    }

    fun onSubmitButtonClicked(view: View) {
        singleGifSubmitButtonText.set("Fetching..")
        GifFactory.create().fetchSearchableGifs(this.fieldText.get().toString()).enqueue(object : Callback<GifResponse> {
            override fun onFailure(call: Call<GifResponse>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<GifResponse>?, response: Response<GifResponse>?) {
                translateViewVisibility.set(View.VISIBLE)
                singleGifSubmitButtonText.set("Done")
                if (response != null) {
                    url = response.body()!!.data.get(0).images.fixedHeightGifs.url
                    setChanged()
                    notifyObservers()
                }
            }
        })
    }

    fun onRandomSubmitButtonClicked(view: View) {
        randomgSingleGifSubmitButtonText.set("Fetching..")
        GifFactory.create().fetchRandomGifs(this.fieldText.get().toString()).enqueue(object : Callback<RandomGifData> {
            override fun onFailure(call: Call<RandomGifData>?, t: Throwable?) {
                var e = t?.message
            }

            override fun onResponse(call: Call<RandomGifData>?, response: Response<RandomGifData>?) {
                randomgSingleGifSubmitButtonText.set("Done..")
                if (response != null) {
                    url = response.body()!!.data.url
                    setChanged()
                    notifyObservers()
                }
            }
        })

    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        fieldText.set(s.toString())
    }

    fun onFabClicked(view: View) {
        downloadProgress.set(View.VISIBLE)
        downloadButton.set(View.GONE)
        fileName.set("loading.json")
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder.setSmallIcon(R.drawable.ic_arrow_downward_black_24dp)
                .setContentTitle("Downloading")
                .setAutoCancel(true)
        mNotificationManager?.notify(11, mBuilder.build())
        Thread(Runnable {
            var cache = Glide.with(view.context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            SaveImage(cache, view.context)
        }).start()
    }


    private fun SaveImage(cache: File, context: Context) {
        var intent = Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType("image/*");
        context.startActivity(intent)


        Thread(Runnable {

            mBuilder.setSmallIcon(R.drawable.ic_arrow_downward_black_24dp)
                    .setLargeIcon(Glide.with(context)
                            .load(url)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .into(300, 300)
                            .get())
                    .setContentIntent(PendingIntent.getActivity(context, 99, intent, 0))
                    .setContentTitle("Downloaded")
                    .setAutoCancel(true)

            mNotificationManager?.notify(11, mBuilder.build())
        }).start()

        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root + "/saved_images")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.gif"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            out.write(cache.inputStream().readBytes())
            out.flush()
            out.close()
            scanMedia(file, context)
            fileName.set("checked_done.json")

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun scanMedia(file: File, context: Context) {
        val uri = Uri.fromFile(file)
        val scanFileIntent = Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
        context.sendBroadcast(scanFileIntent)
    }

}