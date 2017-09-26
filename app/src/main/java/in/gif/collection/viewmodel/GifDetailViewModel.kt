package `in`.gif.collection.viewmodel

import `in`.gif.collection.*
import `in`.gif.collection.Utils.CommonUtils
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.data.GifFactory
import `in`.gif.collection.model.GifResponse
import `in`.gif.collection.model.RandomGifData
import `in`.gif.collection.model.TranslateData
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.NotificationCompat
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.InterstitialAd
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

class GifDetailViewModel(context: Context, callback: ShowDialogCallback, url: String = "") : Observable() {

    val urlKey = "url"
    private var url = url
    private var context = context
    var fileName: ObservableField<String> = ObservableField("check_fit.json")
    var downloadProgress: ObservableInt = ObservableInt(View.GONE)
    var downloadButton: ObservableInt = ObservableInt(View.GONE)
    var mBuilder = NotificationCompat.Builder(context)
    var mNotificationManager: NotificationManager? = null
    var detailViewVisibility: ObservableInt = ObservableInt(View.GONE)

    var singleGifSubmitButtonText: ObservableField<String> = ObservableField("Done")
    var randomgSingleGifSubmitButtonText: ObservableField<String> = ObservableField("Done")

    var fieldText: ObservableField<String> = ObservableField("")
    var singleGifDownloadButtonVisibility: ObservableInt = ObservableInt(View.GONE)

    var noGifContainerVisibility: ObservableInt = ObservableInt(View.VISIBLE)

    var showDialogCallback = callback

    fun getImageUrl(): String {
        return url
    }

    fun onSubmitButtonClicked(view: View) {
        singleGifSubmitButtonText.set("Fetching..")
        singleGifDownloadButtonVisibility.set(View.GONE)
        GifFactory.create().fetchTranslateGif(this.fieldText.get().toString()).enqueue(object : Callback<TranslateData> {
            override fun onFailure(call: Call<TranslateData>?, t: Throwable?) {
                noGifContainerVisibility.set(View.VISIBLE)
                singleGifSubmitButtonText.set("Done")
                singleGifDownloadButtonVisibility.set(View.GONE)
            }

            override fun onResponse(call: Call<TranslateData>?, response: Response<TranslateData>?) {
                singleGifSubmitButtonText.set("Done")
                if (response!!.body() != null) {
                    url = response.body()!!.data.images.fixedHeightGifs.url
                    noGifContainerVisibility.set(View.GONE)
                    val pref = PreferenceHelper.defaultPrefs(view.context)
                    pref[Constants.KEY_TRANSLATE_GIF_URL] = url
                    setChanged()
                    notifyObservers()
                } else {
                    noGifContainerVisibility.set(View.VISIBLE)
                    singleGifDownloadButtonVisibility.set(View.GONE)

                }
            }
        })
    }

    fun onRandomSubmitButtonClicked(view: View) {
        randomgSingleGifSubmitButtonText.set("Fetching..")
        singleGifDownloadButtonVisibility.set(View.GONE)
        GifFactory.create().fetchRandomGifs(this.fieldText.get().toString()).enqueue(object : Callback<RandomGifData> {
            override fun onFailure(call: Call<RandomGifData>?, t: Throwable?) {
                noGifContainerVisibility.set(View.VISIBLE)
                randomgSingleGifSubmitButtonText.set("Done.")
                singleGifDownloadButtonVisibility.set(View.GONE)
                setChanged()
                notifyObservers()

            }

            override fun onResponse(call: Call<RandomGifData>?, response: Response<RandomGifData>?) {
                randomgSingleGifSubmitButtonText.set("Done.")
                if (response != null) {
                    url = response.body()!!.data.url
                    if (!url.isNullOrEmpty()) {
                        noGifContainerVisibility.set(View.GONE)
                        singleGifDownloadButtonVisibility.set(View.VISIBLE)
                    }
                    PreferenceHelper.defaultPrefs(view.context)[Constants.KEY_RANDOM_GIF_URL] = url
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
        context.runOnM {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showDialogCallback.showDialog()
                return@runOnM
            } else {
                saveGif(view.context)
            }
        }

        context.runOnKK { saveGif(view.context) }

    }

    fun onShareButtonClicked(view: View) {
        context.runOnM {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showDialogCallback.showDialog()
                return@runOnM
            } else {
                io.reactivex.Observable.fromCallable {
                    Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            saveImage(it, context, true)
                        }
            }
        }
        context.runOnKK {
            io.reactivex.Observable.fromCallable {
                Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        saveImage(it, context, true)
                    }
        }
    }

    fun saveGif(context: Context) {
        downloadProgress.set(View.VISIBLE)
        downloadButton.set(View.GONE)
        fileName.set("loading.json")
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder.setSmallIcon(R.drawable.ic_arrow_downward_black_24dp)
                .setContentTitle("Saving")
                .setAutoCancel(true)
        mNotificationManager?.notify(11, mBuilder.build())

        io.reactivex.Observable.fromCallable {
            Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    saveImage(it, context)
                }
    }

    private fun saveImage(cache: File, context: Context, isShareButtonClicked: Boolean = false) {

        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root + "/GifCollection")

        myDir.mkdirs()

        val n = cache.name
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

        val imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        val shareIntent = Intent()
        with(shareIntent) {
            action = (Intent.ACTION_SEND);
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = ("image/*");
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (!isShareButtonClicked) {
            io.reactivex.Observable.fromCallable {
                mBuilder.setSmallIcon(R.drawable.ic_arrow_downward_black_24dp)
                        .setLargeIcon(Glide.with(context)
                                .load(url)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()
                                .into(300, 300)
                                .get())
                        .setContentIntent(PendingIntent.getActivity(context, 99, shareIntent, 0))
                        .setContentTitle("Saved")
                        .setAutoCancel(true)

                mNotificationManager?.notify(11, mBuilder.build())

                context.startActivity(Intent.createChooser(shareIntent, "Share with.."))

            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe { CommonUtils.showInterstitialAds(InterstitialAd(context)) }
        } else {
            CommonUtils.showInterstitialAds(InterstitialAd(context))
            context.startActivity(Intent.createChooser(shareIntent, "Share with.."))
        }
    }

    private fun scanMedia(file: File, context: Context) {
        val uri = Uri.fromFile(file)
        val scanFileIntent = Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
        context.sendBroadcast(scanFileIntent)
    }

}