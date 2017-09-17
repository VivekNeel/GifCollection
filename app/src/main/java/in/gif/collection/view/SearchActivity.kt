package `in`.gif.collection.view

import `in`.gif.collection.R
import `in`.gif.collection.custom.CustomSearchBar
import `in`.gif.collection.custom.CustomTextWatcher
import `in`.gif.collection.custom.FadeInTransition
import `in`.gif.collection.custom.FadeOutTransition
import `in`.gif.collection.showKeyboard
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Menu
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.merge_search.*


/**
 * Created by vivek on 17/09/17.
 */
class SearchActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        toolbar_search_edittext.addTextChangedListener(object : CustomTextWatcher() {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("textchanged", s.toString())
            }
        })

        // make sure to check if this is the first time running the activity
        // we don't want to play the enter animation on configuration changes (i.e. orientation)
        if (savedInstanceState == null) {
            // Start with an empty looking Toolbar
            // We are going to fade its contents in, as long as the activity finishes rendering
            search_toolbar?.hideContent()

            val viewTreeObserver = search_toolbar?.viewTreeObserver
            viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    search_toolbar!!.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // after the activity has finished drawing the initial layout, we are going to continue the animation
                    // that we left off from the MainActivity
                    showSearch()
                }

                private fun showSearch() {
                    // use the TransitionManager to animate the changes of the Toolbar
                    TransitionManager.beginDelayedTransition(search_toolbar, FadeInTransition.createTransition())
                    // here we are just changing all children to VISIBLE
                    search_toolbar?.showContent()
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

        val transition = FadeOutTransition.withAction(object : Transition.TransitionListener {
            override fun onTransitionResume(transition: Transition?) {

            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }

            override fun onTransitionEnd(transition: Transition) {
                endingAction()
            }
        })

        TransitionManager.beginDelayedTransition(search_toolbar, transition)
        search_toolbar.hideContent()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

}