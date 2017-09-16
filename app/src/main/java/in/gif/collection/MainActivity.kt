package `in`.gif.collection

import `in`.gif.collection.databinding.ActivityMainBinding
import `in`.gif.collection.view.TrendingGifAdapter
import `in`.gif.collection.viewmodel.RandomGifViewModel
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import java.util.*

class MainActivity : AppCompatActivity(), Observer {

    private lateinit var mainActivityDataBinding: ActivityMainBinding
    var isLoading = false
    var currentOffsetValue = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        setUPList(mainActivityDataBinding.randomGifRV)
        setUPObserver(mainActivityDataBinding.randomGifModel!!)
        mainActivityDataBinding.randomGifModel!!.getData(0)
    }

    fun initDataBinding() {
        mainActivityDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityDataBinding.randomGifModel = RandomGifViewModel(this)
    }

    fun setUPObserver(observable: Observable) {
        observable.addObserver(this)
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

    fun setUPList(recyclerView: RecyclerView) {
        val adapter = TrendingGifAdapter(this@MainActivity)

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager: StaggeredGridLayoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPositions(null)
                    if ((totalItemCount - 1) == getLastVisibleItem(lastVisibleItemPos)) {
                        isLoading = true
                        currentOffsetValue++
                        mainActivityDataBinding.loadMoreProgress.visibility = View.VISIBLE
                        mainActivityDataBinding.randomGifModel?.fetchTrendingGif(currentOffsetValue)
                    }
                }
            }
        }
        )

    }

    override fun update(o: Observable?, arg: Any?) {
        when (o) {
            is RandomGifViewModel -> {
                isLoading = false
                mainActivityDataBinding.loadMoreProgress.visibility = View.GONE
                val adapter = mainActivityDataBinding.randomGifRV.adapter as TrendingGifAdapter
                adapter.setGifList(list = o.getGifs())
            }
        }
    }

}
