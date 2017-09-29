package `in`.gif.collection

import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.custom.CustomTransitionListener
import `in`.gif.collection.custom.FadeInTransition
import `in`.gif.collection.custom.FadeOutTransition
import `in`.gif.collection.data.db.StorageService
import `in`.gif.collection.databinding.ActivityMainBinding
import `in`.gif.collection.view.AboutActivity
import `in`.gif.collection.view.BaseActivity
import `in`.gif.collection.view.SearchActivity
import `in`.gif.collection.view.fragments.*
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.mikepenz.materialdrawer.DrawerBuilder
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.model.*
import java.util.concurrent.TimeUnit
import android.view.WindowManager


class MainActivity : BaseActivity() {

    private var toolbarMargin: Int = 0
    private lateinit var mainActivityBinding: ActivityMainBinding
    private var bundle = Bundle()
    private var queryFromDb: String? = null

    companion object {
        const val TAG_TRENDING = "trend"
        const val TAG_FAVOURITE = "fav"
        const val TAG_TRENDING_TERM = "trendingTerm"
        const val TAG_SEARCH = "search"
        const val TAG_VIDEO = "video"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        queryFromDb = StorageService.getLangKey()?.getL()
        setupToolbar()
        setupDrawer()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("on", "d")
    }


    fun setupDrawer() {
        DrawerBuilder().withActivity(this)

        val item1 = PrimaryDrawerItem().withIdentifier(1).withIcon(R.drawable.love).withName(R.string.video_love)
        val item2 = PrimaryDrawerItem().withIdentifier(2).withIcon(R.drawable.sad).withName(R.string.video_love_sad)
        val item3 = PrimaryDrawerItem().withIdentifier(3).withIcon(R.drawable.old).withName(R.string.video_old)
        val item4 = PrimaryDrawerItem().withIdentifier(4).withIcon(R.drawable.general).withName(R.string.video_general)
        val item5 = PrimaryDrawerItem().withIdentifier(5).withIcon(R.drawable.friends).withName(R.string.video_friendship)
        val item6 = PrimaryDrawerItem().withIdentifier(6).withIcon(R.drawable.birthday).withName(R.string.video_happy_birthday)
        val item7 = PrimaryDrawerItem().withIdentifier(7).withIcon(R.drawable.downloaded).withName(R.string.video_downloaded)


        val item8 = PrimaryDrawerItem().withIdentifier(8).withIcon(R.drawable.gif).withName(R.string.gif)

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.sidebar)
                .build()


        //create the drawer and remember the `Drawer` result object
        val result = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        SectionDrawerItem().withName(R.string.video_category_name),
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6,
                        item7,
                        DividerDrawerItem(),
                        SectionDrawerItem().withName("Whatsapp Gifs"),
                        item8
                )
                .withOnDrawerItemClickListener { _, _, drawerItem ->

                    toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    toolbar.isClickable = false
                    bottomBar.hide()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val window = window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.statusBarColor = Color.parseColor("#BDBDBD")
                    }

                    val frameLP = toolbar.layoutParams as FrameLayout.LayoutParams
                    frameLP.setMargins(0, 0, 0, 0)
                    toolbar.layoutParams = frameLP


                    if (drawerItem?.identifier == 1.toLong()) {
                        bundle.putString(Constants.KEY_FRAGMENT_YOUTUBE_SEARCH_QUERY, "whatsapp $queryFromDb love video status")
                        bundle.putString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE, "love")
                        toolbar.title = "Love videos"
                        setupFragments(TAG_VIDEO, bundle)
                    } else if (drawerItem?.identifier == 2.toLong()) {
                        bundle.putString(Constants.KEY_FRAGMENT_YOUTUBE_SEARCH_QUERY, "whatsapp $queryFromDb sad love video status")
                        bundle.putString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE, "sad")
                        toolbar.title = "Sad videos"
                        setupFragments(TAG_VIDEO, bundle)
                    } else if (drawerItem?.identifier == 3.toLong()) {
                        bundle.putString(Constants.KEY_FRAGMENT_YOUTUBE_SEARCH_QUERY, "whatsapp $queryFromDb old video status")
                        bundle.putString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE, "old")
                        toolbar.title = "Old videos"
                        setupFragments(TAG_VIDEO, bundle)
                    } else if (drawerItem?.identifier == 4.toLong()) {
                        bundle.putString(Constants.KEY_FRAGMENT_YOUTUBE_SEARCH_QUERY, "whatsapp $queryFromDb video status")
                        bundle.putString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE, "general")
                        toolbar.title = "General videos"

                        setupFragments(TAG_VIDEO, bundle)
                    } else if (drawerItem?.identifier == 5.toLong()) {
                        bundle.putString(Constants.KEY_FRAGMENT_YOUTUBE_SEARCH_QUERY, "whatsapp $queryFromDb friendship status ")
                        bundle.putString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE, "friend")
                        toolbar.title = "Friendship videos"

                        setupFragments(TAG_VIDEO, bundle)
                    } else if (drawerItem?.identifier == 6.toLong()) {
                        bundle.putString(Constants.KEY_FRAGMENT_YOUTUBE_SEARCH_QUERY, "whatsapp $queryFromDb birthday video status")
                        bundle.putString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE, "birthday")
                        toolbar.title = "Birthday videos"

                        setupFragments(TAG_VIDEO, bundle)
                    } else if (drawerItem?.identifier == 7.toLong()) {
                        toolbar.title = "Downloaded videos"
                        bundle.putString(Constants.KEY_FRAGMEN_YOUTUBE_SEARCH_QUERY_TYPE, "")
                        setupFragments(TAG_VIDEO, bundle)

                    } else if (drawerItem?.identifier == 8.toLong()) {
                        setupBottomNavigation()
                    }
                    false
                }

                .build()
        result.setSelection(1)

    }

    fun setupBottomNavigation() {

        bottomBar.show()
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        toolbar.isClickable = true
        toolbar.title = "Search for gifs"

        toolbar.setTitleTextColor(Color.parseColor("#212121"))
        val frameLP = toolbar.layoutParams as FrameLayout.LayoutParams
        main_holder.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
        frameLP.setMargins(toolbarMargin, toolbarMargin, toolbarMargin, toolbarMargin)
        toolbar.layoutParams = frameLP

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.primary_dark)
        }

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
                val fragment = VideoListFragment()
                fragment.arguments = bundle
                commitFragment(TrendingTermFragment(), R.id.frame, TAG_TRENDING_TERM)
            }
            TAG_SEARCH -> {
                val fragment = SearchFragment()
                fragment.arguments = bundle
                commitFragment(fragment, R.id.frame, TAG_TRENDING_TERM, addToBackStack = true)
            }
            TAG_VIDEO -> {
                val fragment = VideoListFragment()
                fragment.arguments = bundle
                commitFragment(fragment, R.id.frame, TAG_VIDEO, addToBackStack = false)
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
