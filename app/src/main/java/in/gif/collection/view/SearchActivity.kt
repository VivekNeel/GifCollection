package `in`.gif.collection.view

import `in`.gif.collection.R
import `in`.gif.collection.custom.*
import `in`.gif.collection.databinding.ActivitySearchBinding
import `in`.gif.collection.viewmodel.search.SearchGifViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.ViewTreeObserver
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.merge_search.*
import java.util.*
import android.widget.Toast
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View


/**
 * Created by vivek on 17/09/17.
 */
class SearchActivity : BaseActivity(), Observer {

    private lateinit var searchBinding: ActivitySearchBinding
    private lateinit var searchToolbar: CustomSearchBar
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBinding()
        setupObservers(searchBinding.viewModel)
        setupList(searchBinding.randomGifRV)

        // make sure to check if this is the first time running the activity
        // we don't want to play the enter animation on configuration changes (i.e. orientation)
        if (savedInstanceState == null) {
            // Start with an empty looking Toolbar
            // We are going to fade its contents in, as long as the activity finishes rendering
            searchToolbar.hideContent()

            val viewTreeObserver = searchToolbar.viewTreeObserver
            viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    searchToolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // after the activity has finished drawing the initial layout, we are going to continue the animation
                    // that we left off from the MainActivity
                    showSearch()
                }

                private fun showSearch() {
                    // use the TransitionManager to animate the changes of the Toolbar
                    TransitionManager.beginDelayedTransition(searchToolbar, FadeInTransition.createTransition())
                    // here we are just changing all children to VISIBLE
                    searchToolbar.showContent()
                }
            })
        }
    }

    fun initDataBinding() {
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchBinding.viewModel = SearchGifViewModel()
        searchToolbar = searchBinding.searchToolbar
        searchEditText = toolbar_search_edittext

        setSupportActionBar(searchToolbar)
        searchEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchBinding.viewModel?.fetchSearchableGif(searchEditText.text.toString())
                return@OnKeyListener true
            }
            false
        })
    }

    fun setupObservers(observable: SearchGifViewModel?) {
        observable?.addObserver(this)
    }

    fun setupList(recyclerView: RecyclerView) {
        var adapter = TrendingGifAdapter(this)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CustomItemDecorator(15))
    }

    override fun finish() {
        hideKb()
        exitTransitionWithAction({
            super@SearchActivity.finish()
            overridePendingTransition(0, 0)
        })
    }

    private fun exitTransitionWithAction(endingAction: () -> Unit) {

        val transition = FadeOutTransition.withAction(object : CustomTransitionListener() {
            override fun onTransitionEnd(transition: Transition?) {
                endingAction()
            }
        })

        TransitionManager.beginDelayedTransition(searchToolbar, transition)
        searchToolbar.hideContent()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun update(o: Observable?, arg: Any?) {
        when (o) {
            is SearchGifViewModel -> {
                val adapter = searchBinding.randomGifRV.adapter as TrendingGifAdapter
                adapter.setGifList(o.getGifs())
                hideKb()

            }
        }
    }
}