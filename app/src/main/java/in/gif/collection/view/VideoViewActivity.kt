package `in`.gif.collection.view

import `in`.gif.collection.BuildConfig
import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.commitFragment
import `in`.gif.collection.databinding.ActivityVideoViewBinding
import `in`.gif.collection.view.fragments.YoutubePlayerFragment
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_view.*

/**
 * Created by vivek on 24/09/17.
 */
class VideoViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoViewBinding

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_view)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getIntentFromExtras()
        val fragment = YoutubePlayerFragment()
        val bundle = Bundle()
        bundle.putString(Constants.EXTRA_VIDEO_ID, id)
        fragment.arguments = bundle
        commitFragment(fragment, R.id.coordinator, "player")

    }

    fun getIntentFromExtras() {
        id = intent.getStringExtra(Constants.EXTRA_VIDEO_ID)
    }

}