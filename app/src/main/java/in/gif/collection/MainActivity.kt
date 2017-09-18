package `in`.gif.collection

import `in`.gif.collection.custom.CustomTransitionListener
import `in`.gif.collection.custom.FadeInTransition
import `in`.gif.collection.custom.FadeOutTransition
import `in`.gif.collection.databinding.ActivityMainBinding
import `in`.gif.collection.view.BaseActivity
import `in`.gif.collection.view.SearchActivity
import `in`.gif.collection.view.fragments.RandomGifFragment
import `in`.gif.collection.view.fragments.TranslateFragment
import `in`.gif.collection.view.fragments.TranslateGifFragment
import `in`.gif.collection.view.fragments.TrendingGifFragment
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private var toolbarMargin: Int = 0
    private lateinit var mainActivityBinding: ActivityMainBinding

    companion object {
        const val TAG_TRENDING = "trend"
        const val TAG_RANDOM = "random"
        const val TAG_TRANSLATE = "translate"
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
                    R.id.trending -> setupFragments(TAG_TRENDING)
                    R.id.random -> setupFragments(TAG_RANDOM)
                    R.id.translate -> setupFragments(TAG_TRANSLATE)
                }
            }
        }
    }


    fun setupFragments(tag: String) {

        when (tag) {
            TAG_TRENDING -> {
                commitFragment(TrendingGifFragment(), R.id.frame, TAG_TRENDING)
            }
            TAG_RANDOM -> {
                commitFragment(RandomGifFragment(), R.id.frame, TAG_RANDOM)
            }

            TAG_TRANSLATE -> {
                commitFragment(TranslateFragment(), R.id.frame, TAG_TRANSLATE)
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

}
