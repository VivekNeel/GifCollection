package com.gifs.collection.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionManager
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import com.gifs.collection.R
import com.gifs.collection.anayltics.CustomAnayltics
import com.gifs.collection.commitFragment
import com.gifs.collection.custom.CustomSearchBar
import com.gifs.collection.custom.CustomTransitionListener
import com.gifs.collection.custom.FadeInTransition
import com.gifs.collection.custom.FadeOutTransition
import com.gifs.collection.databinding.ActivitySearchBinding
import com.gifs.collection.view.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.merge_search.*


/**
 * Created by vivek on 17/09/17.
 */
class SearchActivity : BaseActivity() {

    private lateinit var searchToolbar: CustomSearchBar
    private lateinit var searchEditText: EditText
    private lateinit var binding: ActivitySearchBinding
    private var fragment = SearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        // make sure to check if this is the first time running the activity
        // we don't want to play the enter animation on configuration changes (i.e. orientation)
        if (savedInstanceState == null) {
            // Start with an empty looking Toolbar
            // We are going to fade its contents in, as long as the activity finishes rendering
            searchToolbar.hideContent()
            this.commitFragment(fragment, R.id.frame, com.gifs.collection.MainActivity.TAG_SEARCH)

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

    fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchToolbar = search_toolbar
        searchEditText = toolbar_search_edittext

        setSupportActionBar(searchToolbar)
        searchEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                CustomAnayltics.logSearch(searchEditText.text.toString())
                fragment.doSearch(searchEditText.text.toString())
                return@OnKeyListener true
            }
            false
        })
    }


    override fun finish() {
        hideKb()
        exitTransitionWithAction({
            super@SearchActivity.finish()
            overridePendingTransition(0, 0)
        })

        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        }
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

}