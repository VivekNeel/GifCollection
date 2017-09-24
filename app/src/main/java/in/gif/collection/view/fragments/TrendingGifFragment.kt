package `in`.gif.collection.view.fragments

import `in`.gif.collection.BuildConfig
import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.custom.CustomItemDecorator
import `in`.gif.collection.databinding.FragmentTrendingBinding
import `in`.gif.collection.view.TrendingGifAdapter
import `in`.gif.collection.viewmodel.trending.TrendingGifViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_trending.*
import java.util.*

/**
 * Created by vivek on 17/09/17.
 */
class TrendingGifFragment : BaseFragment(), Observer {

    private lateinit var mainActivityDataBinding: FragmentTrendingBinding
    var isLoading = false
    private var next: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainActivityDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false)
        mainActivityDataBinding.randomGifModel = TrendingGifViewModel(getFragmentHost())
        return mainActivityDataBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUPList(mainActivityDataBinding.randomGifRV)
        setUPObserver(mainActivityDataBinding.randomGifModel)
        mainActivityDataBinding.randomGifModel?.getData("")
        if(!BuildConfig.DEBUG)
        setupAds()
    }

    fun setupAds() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        activityAddView.loadAd(adRequest)
    }


    fun setUPList(recyclerView: RecyclerView) {
        val adapter = TrendingGifAdapter(getFragmentHost())

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CustomItemDecorator(15))
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager: StaggeredGridLayoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPositions(null)
                    if ((totalItemCount - 1) == getLastVisibleItem(lastVisibleItemPos)) {
                        isLoading = true
                        mainActivityDataBinding.loadMoreProgress.visibility = View.VISIBLE
                        mainActivityDataBinding.randomGifModel?.fetchTrendingGif(next)
                    }
                }
            }
        }
        )

    }

    override fun update(o: Observable?, arg: Any?) {
        when (o) {
            is TrendingGifViewModel -> {
                isLoading = false
                mainActivityDataBinding.loadMoreProgress.visibility = View.GONE
                val adapter = mainActivityDataBinding.randomGifRV.adapter as TrendingGifAdapter
                adapter.setGifList(list = o.getGifs())
                this.next = o.getNext()
            }
        }
    }

    fun setUPObserver(observable: TrendingGifViewModel?) {
        observable?.addObserver(this)
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}