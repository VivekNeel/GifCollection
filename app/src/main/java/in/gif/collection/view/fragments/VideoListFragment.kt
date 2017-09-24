package `in`.gif.collection.view.fragments

import `in`.gif.collection.BuildConfig
import `in`.gif.collection.ITermItemClickedCallback
import `in`.gif.collection.R
import `in`.gif.collection.databinding.FragmentTrendingBinding
import `in`.gif.collection.view.TrendingGifAdapter
import `in`.gif.collection.viewmodel.trending.TrendingGifViewModel
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commit451.youtubeextractor.YouTubeExtractionResult
import com.commit451.youtubeextractor.YouTubeExtractor
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.File
import java.util.*

/**
 * Created by vivek on 24/09/17.
 */
class VideoListFragment : BaseFragment(), Observer, ITermItemClickedCallback {
    override fun onTermClicked(name: String) {

    }

    lateinit var binding: FragmentTrendingBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupList()
        binding.randomGifModel?.fetchYoutubeVideos("english whatsapp videos status")
    }

    override fun update(p0: Observable?, p1: Any?) {
        when (p0) {
            is TrendingGifViewModel -> {
                val adapter = randomGifRV.adapter as TrendingGifAdapter
                adapter.setVideoList(p0.getVideos())

            }
        }
    }

    fun setupObserver() {
        binding.randomGifModel = TrendingGifViewModel(getFragmentHost())
        binding.randomGifModel!!.addObserver(this)
    }

    fun setupList() {
        randomGifRV.layoutManager = LinearLayoutManager(getFragmentHost())
        randomGifRV.setHasFixedSize(true)
        randomGifRV.adapter = TrendingGifAdapter(getFragmentHost(), true)
    }
}