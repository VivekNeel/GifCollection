package `in`.gif.collection.view

import `in`.gif.collection.R
import `in`.gif.collection.custom.*
import `in`.gif.collection.databinding.ActivitySearchBinding
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Menu
import android.view.ViewTreeObserver
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.merge_search.*


/**
 * Created by vivek on 17/09/17.
 */
class SearchActivity : BaseActivity() {

    private lateinit var searchBinding: ActivitySearchBinding
    private lateinit var searchToolbar: CustomSearchBar
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        searchToolbar = searchBinding.searchToolbar
        searchEditText = toolbar_search_edittext

        setSupportActionBar(searchToolbar)
        searchEditText.addTextChangedListener(object : CustomTextWatcher() {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("textchanged", s.toString())
            }
        })

        // make sure to check if this is the first time running the activity
        // we don't want to play the enter animation on configuration changes (i.e. orientation)
        if (savedInstanceState == null) {
            // Start with an empty looking Toolbar
            // We are going to fade its contents in, as long as the activity finishes rendering
            searchToolbar.hideContent()

            val viewTreeObserver = searchToolbar?.viewTreeObserver
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

}