package `in`.gif.collection.view

import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.commitFragment
import `in`.gif.collection.databinding.ActivityVideoViewBinding
import `in`.gif.collection.model.youtube.ItemsData
import `in`.gif.collection.view.fragments.YoutubePlayerFragment
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_video_view.*


/**
 * Created by vivek on 24/09/17.
 */
class VideoViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoViewBinding

    private lateinit var id: String
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_view)
        setSupportActionBar(toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#BDBDBD")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getIntentFromExtras()
        val fragment = YoutubePlayerFragment()
        val bundle = Bundle()
        bundle.putString(Constants.EXTRA_VIDEO_ID, id)
        bundle.putString("type", type)
        bundle.putString("title", intent.getStringExtra("title"))
        fragment.arguments = bundle
        commitFragment(fragment, R.id.coordinator, "player")

    }


    fun getIntentFromExtras() {
        id = intent.getStringExtra(Constants.EXTRA_VIDEO_ID)
        type = intent.getStringExtra("type")
        toolbar.title = intent.getStringExtra("title")
    }

    companion object {
        fun getStartIntent(context: Context, itemsData: ItemsData): Intent {
            return Intent(context, VideoViewActivity::class.java)
                    .apply {
                        putExtra(Constants.EXTRA_VIDEO_ID, itemsData.idData?.videoId)
                        putExtra("title", itemsData.snippetData?.title)
                        putExtra("type", itemsData.getType())
                    }
        }
    }

}