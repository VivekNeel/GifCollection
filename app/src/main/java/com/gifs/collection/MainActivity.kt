package com.gifs.collection

import com.gifs.collection.custom.CustomTransitionListener
import com.gifs.collection.custom.FadeInTransition
import com.gifs.collection.custom.FadeOutTransition
import com.gifs.collection.databinding.ActivityMainBinding
import com.gifs.collection.view.AboutActivity
import com.gifs.collection.view.BaseActivity
import com.gifs.collection.view.SearchActivity
import com.gifs.collection.view.fragments.*
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import android.telephony.PhoneNumberUtils
import android.content.ComponentName
import android.net.Uri
import com.mega4tech.whatsappapilibrary.WhatsappApi
import com.mega4tech.whatsappapilibrary.liseteners.SendMessageListener
import com.mega4tech.whatsappapilibrary.model.WContact
import com.mega4tech.whatsappapilibrary.model.WMessage
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder


class MainActivity : BaseActivity() {

    private var toolbarMargin: Int = 0
    private lateinit var mainActivityBinding: ActivityMainBinding
    private var bundle = Bundle()

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
        setupDrawer()
    }

    fun setupDrawer() {

        val trending = PrimaryDrawerItem().withIdentifier(1).withName(R.string.item_trending).withIcon(R.drawable.trending)
        val navrathri = PrimaryDrawerItem().withIdentifier(2).withName(R.string.item_navrathri).withIcon(R.drawable.navrathri)
        val love = PrimaryDrawerItem().withIdentifier(3).withName(R.string.item_love).withIcon(R.drawable.love)
        val sad = PrimaryDrawerItem().withIdentifier(4).withName(R.string.item_sad).withIcon(R.drawable.sad)
        val wedding = PrimaryDrawerItem().withIdentifier(5).withName(R.string.item_wedding).withIcon(R.drawable.wedding)
        val friends = PrimaryDrawerItem().withIdentifier(6).withName(R.string.item_friends).withIcon(R.drawable.friends)
        val coding = PrimaryDrawerItem().withIdentifier(7).withName(R.string.item_coding).withIcon(R.drawable.programming)
        val birthday = PrimaryDrawerItem().withIdentifier(8).withName(R.string.birthday).withIcon(R.drawable.birthday)
        val anniversary = PrimaryDrawerItem().withIdentifier(9).withName(R.string.anniversary).withIcon(R.drawable.anniversary)

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_gif_black_24dp)
                .build()

        //create the drawer and remember the `Drawer` result object
        var isBottomNavigationSetup = false
        val result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        SectionDrawerItem().withName(R.string.category),
                        trending,
                        navrathri,
                        love,
                        sad,
                        wedding,
                        friends,
                        coding,
                        birthday,
                        anniversary
                )
                .withOnDrawerItemClickListener { _, _, drawerItem ->
                    if (drawerItem.identifier == 1.toLong()) {
                        isBottomNavigationSetup = true
                        setupBottomNavigation()
                    } else {
                        if (drawerItem.identifier == 2.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "navrathri")
                        } else if (drawerItem.identifier == 3.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "love")

                        } else if (drawerItem.identifier == 4.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "sad")

                        } else if (drawerItem.identifier == 5.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "wedding")

                        } else if (drawerItem.identifier == 6.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "friends")

                        } else if (drawerItem.identifier == 7.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "programming")

                        } else if (drawerItem.identifier == 8.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "birthday")

                        } else if (drawerItem.identifier == 9.toLong()) {
                            bundle.putString(Constants.KEY_FRAGMENT_SEARCH_QUERY, "anniversary")

                        }
                        bottomBar.hide()
                        setupFragments(TAG_SEARCH, bundle)
                    }
                    false
                }
                .build()

        result.setSelection(1)

    }


    fun setupBottomNavigation() {
        bottomBar.show()
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
                commitFragment(fragment, R.id.frame, TAG_TRENDING_TERM, addToBackStack = true)
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
}
