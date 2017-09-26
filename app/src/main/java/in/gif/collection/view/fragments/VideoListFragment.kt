package `in`.gif.collection.view.fragments

import `in`.gif.collection.*
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.databinding.FragmentTrendingBinding
import `in`.gif.collection.model.youtube.ItemsData
import `in`.gif.collection.view.TrendingGifAdapter
import `in`.gif.collection.view.VideoViewActivity
import `in`.gif.collection.viewmodel.trending.TrendingGifViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import android.support.v7.widget.DividerItemDecoration
import com.google.android.gms.ads.AdRequest


/**
 * Created by vivek on 24/09/17.
 */
class VideoListFragment : BaseFragment(), Observer, IVideoClickedCallback {

    override fun onItemClicked(itemsData: ItemsData) {
        val startIntent = VideoViewActivity.getStartIntent(activity, itemsData)
        getFragmentHost().startActivity(startIntent)
    }

    private lateinit var query: String
    private lateinit var type: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = arguments.getString(Constants.KEY_FRAGMENT_YOUTUBE_SEARCH_QUERY)
            type = arguments.getString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE)
        }
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
        val expiryResponseTime: Long = PreferenceManager.getDefaultSharedPreferences(context).getLong("expiryTime", -1)

        if (expiryResponseTime < System.currentTimeMillis()) {
            binding.randomGifModel?.fetchYoutubeVideos(query, type, true)
        } else {
            binding.randomGifModel?.fetchYoutubeVideos(query, type)
        }
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
        randomGifRV.adapter = TrendingGifAdapter(getFragmentHost(), true, this)
        val dividerItemDecoration = DividerItemDecoration(randomGifRV.context,
                (randomGifRV.layoutManager as LinearLayoutManager).orientation)
        randomGifRV.addItemDecoration(dividerItemDecoration)
    }
}