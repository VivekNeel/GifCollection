package `in`.gif.collection.view.fragments

import `in`.gif.collection.runOnApiLevelLessThanLollipop
import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment

/**
 * Created by vivek on 17/09/17.
 */
open class BaseFragment : Fragment() {

    lateinit var activity: Activity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as Activity
    }

    @Suppress("OverridingDeprecatedMember")
    override fun onAttach(activity: Activity) {
        @Suppress("DEPRECATION")
        super.onAttach(activity)
        activity.runOnApiLevelLessThanLollipop { this.activity = activity }
    }

    fun getFragmentHost(): Activity {
        return activity
    }
}


