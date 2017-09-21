package `in`.gif.collection

import `in`.gif.collection.custom.CustomTransitionListener
import `in`.gif.collection.custom.FadeInTransition
import `in`.gif.collection.custom.FadeOutTransition
import `in`.gif.collection.databinding.ActivityMainBinding
import `in`.gif.collection.view.AboutActivity
import `in`.gif.collection.view.BaseActivity
import `in`.gif.collection.view.SearchActivity
import `in`.gif.collection.view.fragments.*
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private var toolbarMargin: Int = 0
    private lateinit var mainActivityBinding: ActivityMainBinding

    companion object {
        const val TAG_TRENDING = "trend"
        const val TAG_FAVOURITE = "fav"
        const val TAG_TRENDING_TERM = "trendingTerm"
        const val TAG_SEARCH = "search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupToolbar()
        if (savedInstanceState == null)
            setupBottomNavigation()
    }


    fun setupBottomNavigation() {
        val bottomNavigator = mainActivityBinding.bottomBar
        bottomNavigator.apply {
            setDefaultTab(R.id.trending)
            setOnTabSelectListener { tabId ->
                when (tabId) {
                    R.id.trending -> setupFragments(TAG_TRENDING, null)
                    R.id.favourite -> setupFragments(TAG_FAVOURITE, null)
                    R.id.trendingTerm -> setupFragments(TAG_TRENDING_TERM, null)
                }
            }
        }
    }


    fun setupFragments(tag: String, bundle: Bundle?) {

        when (tag) {
            TAG_TRENDING -> {
                commitFragment(TrendingGifFragment(), R.id.frame, TAG_TRENDING)
            }
            TAG_FAVOURITE -> {
                commitFragment(FavouritesFragment(), R.id.frame, TAG_FAVOURITE)
            }

            TAG_TRENDING_TERM -> {
                commitFragment(TrendingTermFragment(), R.id.frame, TAG_TRENDING_TERM)
            }
            TAG_SEARCH -> {
                val fragment = SearchFragment()
                fragment.arguments = bundle
                commitFragment(fragment, R.id.frame, TAG_TRENDING_TERM , addToBackStack = true)
            }

        }
    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.option -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            else -> {
                throw UnsupportedOperationException("unknown id")
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
