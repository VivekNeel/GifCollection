package com.gifs.collection.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gifs.collection.*
import com.gifs.collection.databinding.ListItemDetailBinding
import com.gifs.collection.viewmodel.GifDetailViewModel
import kotlinx.android.synthetic.main.list_item_detail.*
import java.lang.Exception


/**
 * Created by vivek on 15/09/17.
 */
class GifDetailActivity : AppCompatActivity(), ShowDialogCallback {

    private lateinit var binding: ListItemDetailBinding
    private lateinit var imageUrl: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        getExtrasFromIntent()
    }

    fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.list_item_detail)

    }


    fun getExtrasFromIntent() {
        imageUrl = intent.getStringExtra(Constants.EXTRA_DETAIL_THUMNAIL_URL)
        binding.gifDetailViewModel = GifDetailViewModel(this, this, imageUrl)
        (binding.gifDetailViewModel as GifDetailViewModel).detailViewVisibility.set(View.VISIBLE)
        binding.animationView.show()
        shareButtonsContainer.hide()
        save.hide()

        Glide.with(binding.detailIv.context)
                .load(imageUrl)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(object : RequestListener<String, GifDrawable> {
                    override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        binding.animationView.hide()
                        shareButtonsContainer.show()
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {

                        return false
                    }
                }).into(binding.detailIv)


    }

    companion object {
        fun launchDetail(context: Context, url: String): Intent {
            val intent = Intent(context, GifDetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_DETAIL_IMAGE_URL, url)
            intent.putExtra(Constants.EXTRA_DETAIL_THUMNAIL_URL, url)
            return intent
        }
    }

    override fun showDialog() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_STORAGE)
    }

    override fun showSnackbar() {
        showSnack("App not installed, Please install it and try again", root)
    }
}