package `in`.gif.collection.view.fragments

import `in`.gif.collection.BuildConfig
import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.databinding.FragmentVideoVideBinding
import `in`.gif.collection.set
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.android.youtube.player.internal.d
import android.widget.Toast
import com.commit451.youtubeextractor.YouTubeExtractionResult
import com.commit451.youtubeextractor.YouTubeExtractor
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_video_vide.*
import java.io.File


/**
 * Created by vivek on 24/09/17.
 */
class YoutubePlayerFragment : BaseFragment() {

    private lateinit var binding: FragmentVideoVideBinding
    lateinit var id: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_vide, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { id = arguments.getString(Constants.EXTRA_VIDEO_ID) }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val youtubeSupportFragment = YouTubePlayerSupportFragment.newInstance()
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.youtubeLayout, youtubeSupportFragment).commit()
        youtubeSupportFragment.initialize(BuildConfig.YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
                if (!p2) {
                    with(player!!) {
                        setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        loadVideo(id)
                        play()
                    }
                }
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                val errorMessage = p1.toString()
                Toast.makeText(getFragmentHost(), errorMessage, Toast.LENGTH_LONG).show()
                Log.d("errorMessage:", errorMessage)
            }
        })

        shareButton.setOnClickListener {
          doShare()
        }
    }

    fun doShare() {
        val extractor = YouTubeExtractor.create()
        extractor.extract(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<YouTubeExtractionResult> {
                    override fun onSubscribe(d: Disposable) {
                        //You should store this disposable and dispose when appropriate
                    }

                    override fun onSuccess(value: YouTubeExtractionResult) {
                        Log.d("value", value.standardThumbUri.toString())
                        val request =
                                DownloadManager.Request(value.sd360VideoUri)
                        request.setTitle("download");
                        request.setDescription("your file is downloading ...");
                        request.allowScanningByMediaScanner()

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                        val root = Environment.getExternalStorageDirectory().toString()
                        val myDir = File(root + "/GifVideos")

                        myDir.mkdirs()

                        val file = File(myDir, id + ".mp4")
                        request.setDestinationUri(Uri.fromFile(file))
                        PreferenceHelper.defaultPrefs(getFragmentHost())["downloadId"] = id

                        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        manager.enqueue(request)
                    }

                    override fun onError(e: Throwable) {
                        Log.d("onerror", e.message)
                    }
                })
    }

    class DownloadComplete : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File(root + "/GifVideos")

            myDir.mkdirs()
            val downloadId = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)

            val file = File(myDir, downloadId.toString() + ".mp4")

            val imageUri = FileProvider.getUriForFile(p0, BuildConfig.APPLICATION_ID + ".provider", file)

            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.type = "video/*";
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Video")
            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy the Video");
            p0?.startActivity(Intent.createChooser(sendIntent, "Email:"))
        }
    }

}