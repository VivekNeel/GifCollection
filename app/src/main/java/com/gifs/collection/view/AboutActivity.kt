package com.gifs.collection.view


import com.gifs.collection.BuildConfig
import com.gifs.collection.R
import com.gifs.collection.databinding.ActivityAboutBinding
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
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