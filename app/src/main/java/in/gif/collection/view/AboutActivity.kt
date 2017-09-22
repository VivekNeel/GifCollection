package `in`.gif.collection.view


import `in`.gif.collection.BuildConfig
import `in`.gif.collection.R
import `in`.gif.collection.databinding.ActivityAboutBinding
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by vivek on 20/09/17.
 */
class AboutActivity : BaseActivity() {

    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupViews()
    }

    fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about)
    }

    fun setupViews() {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.versionTv.text = "Version : ${BuildConfig.VERSION_NAME}"
    }
}