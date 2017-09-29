package `in`.gif.collection.view.fragments

import `in`.gif.collection.*
import `in`.gif.collection.Utils.CommonUtils
import `in`.gif.collection.data.db.StorageService
import `in`.gif.collection.databinding.FragmentVideoVideBinding
import `in`.gif.collection.model.youtube.ItemsData
import `in`.gif.collection.rx.RxBus
import `in`.gif.collection.rx.events.Events
import `in`.gif.collection.view.ReleatedViewsAdapter
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import android.widget.Toast
import com.commit451.youtubeextractor.YouTubeExtractionResult
import com.commit451.youtubeextractor.YouTubeExtractor
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_video_vide.*
import kotlinx.android.synthetic.main.layout_downlaod_progress.view.*
import java.io.File


/**
 * Created by vivek on 24/09/17.
 */
class YoutubePlayerFragment : BaseFragment(), IVideoClickedCallback, IButtonClickedCallback {
    override fun onDownloadButtonClicked() {
     }

    override fun onShareButtonClicked() {
        if (CommonUtils.checkIfAlreadyDownloaded(id)) {
            createIntentChooser()
            return
        }
        initiateDownload()
    }

    override fun onItemClicked(itemsData: ItemsData) {
        id = itemsData.idData?.videoId!!
        title = itemsData.snippetData?.title!!
        setupList()
        setupPlayer()
    }

    private lateinit var binding: FragmentVideoVideBinding
    lateinit var id: String
    private lateinit var type: String
    private lateinit var title: String
    private var manager: DownloadManager? = null
    private var uri: Uri? = null
    private var downloadPercentageDisposable: Disposable? = null
    private val itemDownloadPercentEmitter: ItemDownloadPercentEmitter

    init {
        itemDownloadPercentEmitter = ItemDownloadPercentEmitter.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_vide, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = arguments.getString(Constants.EXTRA_VIDEO_ID)
            type = arguments.getString("type")
            title = arguments.getString("title")
        }
    }

    fun setupList() {
        relatedVideosRv.layoutManager = LinearLayoutManager(getFragmentHost())
        relatedVideosRv.isNestedScrollingEnabled = false
        relatedVideosRv.setHasFixedSize(true)
        relatedVideosRv.adapter = ReleatedViewsAdapter(this, this , title)
        (relatedVideosRv.adapter as ReleatedViewsAdapter).setVideoList(getRelatedVideosList())
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayer()
        setupList()

        disposable.add(RxBus.toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe { events ->
            when (events) {
                is Events.DownloadCompleteEvent -> {
                }
            }
        })
    }

    fun setupPlayer() {
        val youtubeSupportFragment = YouTubePlayerSupportFragment.newInstance()
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.youtubeLayout, youtubeSupportFragment).commit()
        youtubeSupportFragment.initialize(BuildConfig.YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
                if (!p2) {
                    with(player!!) {
                        setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
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
    }

    fun triggerDownload(uri: Uri) {
        context.runOnM {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_STORAGE)
                return@runOnM
            } else {
                startDownload()
            }
        }

        context.runOnKK {
            startDownload()
        }
    }

    fun initiateDownload() {

        val extractor = YouTubeExtractor.create()
        val progressDialog = ProgressDialog(getFragmentHost())
        progressDialog.show()
        progressDialog.setMessage("Please wait")
        progressDialog.setCancelable(true)
        if (id.isNullOrEmpty()) {
            getFragmentHost().toast("id is null")
            return
        }
        extractor.extract(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<YouTubeExtractionResult> {
                    override fun onSubscribe(d: Disposable) {
                        //You should store this disposable and dispose when appropriate
                    }

                    override fun onSuccess(value: YouTubeExtractionResult) {
                        progressDialog.hide()
                        uri = value.sd360VideoUri
                        if (uri == null) {
                            getFragmentHost().toast("This video is not sharable")
                            return
                        }
                        triggerDownload(uri!!)
                    }


                    override fun onError(e: Throwable) {
                        Log.d("onerror", e.message)
                    }
                })
    }

    fun startDownload() {
        val view = LayoutInflater.from(getFragmentHost()).inflate(R.layout.layout_downlaod_progress, null)

        val dialog = AlertDialog.Builder(getFragmentHost()).setView(view)
                .setCancelable(true)
                .create()
        dialog.show()
        val itemDownloadPercentEmitter = ItemDownloadPercentEmitter.getInstance()

        val id = CustomDownloadManager(getFragmentHost()).enqueue(uri!!, id, title)
        itemDownloadPercentEmitter.addDownloadId(getFragmentHost(), id)
        subscribe(id, dialog, view)

    }


    fun createIntentChooser() {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root + "/GifVideos")

        myDir.mkdirs()

        val file = File(myDir, id + ".mp4")

        val imageUri = FileProvider.getUriForFile(getFragmentHost(), BuildConfig.APPLICATION_ID + ".provider", file)

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "video/*"
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Video")
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy the Video")
        getFragmentHost().startActivity(Intent.createChooser(sendIntent, "Share With"))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_STORAGE && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (CommonUtils.checkIfAlreadyDownloaded(id)) {
                createIntentChooser()
                return
            }
            triggerDownload(uri!!)
        }
    }

    fun getRelatedVideosList(): List<ItemsData> {
        val listOne: MutableList<ItemsData> = mutableListOf()
        val item = ItemsData()
        item.holderType = "static"
        listOne.add(item)

        StorageService.getVideosFromDb(type).forEach { item -> listOne.add(item) }

        listOne.filter { it.idData?.videoId != null && !it.idData!!.videoId.equals(id) }
        return listOne

    }

    private fun subscribe(id: Long, dialog: AlertDialog, view: View) {

        if (downloadPercentageDisposable == null)
            downloadPercentageDisposable = ItemDownloadPercentEmitter.getInstance()
                    .getObservable(getFragmentHost(), id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer<DownloadItem> { downloadItem ->
                        if (downloadItem == null)
                            return@Consumer
                        view.downloadProgress.text = downloadItem.itemDownloadPercent.toString() + " % downloaded"
                        if (downloadItem.downloadStatus.isNotEmpty()) {
                            if (downloadItem.downloadStatus.equals("success")) {
                                dialog.hide()
                                createIntentChooser()
                            } else if (downloadItem.downloadStatus.equals("failed")) {
                                dialog.hide()
                            }
                        }

                    }, Consumer<Throwable> { throwable -> Log.d("YOUTUBEPLAYER", "accept: " + throwable) })

    }

}