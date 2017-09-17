package `in`.gif.collection

import `in`.gif.collection.custom.CustomTransitionListener
import `in`.gif.collection.custom.FadeInTransition
import `in`.gif.collection.custom.FadeOutTransition
import `in`.gif.collection.databinding.ActivityMainBinding
import `in`.gif.collection.view.BaseActivity
import `in`.gif.collection.view.SearchActivity
import `in`.gif.collection.view.TrendingGifAdapter
import `in`.gif.collection.viewmodel.RandomGifViewModel
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity(), Observer {

    private lateinit var mainActivityDataBinding: ActivityMainBinding
    var isLoading = false
    var currentOffsetValue = 1
    private var toolbarMargin: Int = 0

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
        setSupportActionBar(mainActivityDataBinding.toolbar)

        toolbarMargin = resources.getDimensionPixelSize(R.dimen.toolbarMargin)
        toolbar.setOnClickListener {
            showKb()
            transitionToSearch()
        }

    }

    private fun transitionToSearch() {
        // create a transition that navigates to search when complete
        val transition = FadeOutTransition.withAction(navigateToSearchWhenDone())

        // let the TransitionManager do the heavy work for us!
        // all we have to do is change the attributes of the toolbar and the TransitionManager animates the changes
        // in this case I am removing the bounds of the toolbar (to hide the blue padding on the screen) and
        // I am hiding the contents of the Toolbar (Navigation icon, Title and Option Items)
        TransitionManager.beginDelayedTransition(toolbar, transition)
        val frameLP = toolbar.layoutParams as FrameLayout.LayoutParams
        frameLP.setMargins(0, 0, 0, 0)
        toolbar.layoutParams = frameLP
        toolbar.hideContent()
    }

    private fun navigateToSearchWhenDone(): Transition.TransitionListener {
        return object : CustomTransitionListener() {
            override fun onTransitionEnd(transition: Transition?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
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

    override fun onResume() {
        super.onResume()
        fadeToolbarIn()
    }

    private fun fadeToolbarIn() {

        TransitionManager.beginDelayedTransition(toolbar, FadeInTransition.createTransition())
        val layoutParams = toolbar.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(toolbarMargin, toolbarMargin, toolbarMargin, toolbarMargin)
        toolbar.showContent()
        toolbar.layoutParams = layoutParams
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
