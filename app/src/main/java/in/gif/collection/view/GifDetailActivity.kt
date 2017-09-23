package `in`.gif.collection.view

import `in`.gif.collection.*
import `in`.gif.collection.databinding.ListItemDetailBinding
import `in`.gif.collection.viewmodel.GifDetailViewModel
import `in`.gif.collection.model.tenor.GifResultsData
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
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_trending.*
import kotlinx.android.synthetic.main.list_item_detail.*
import java.lang.Exception
import com.google.android.gms.ads.InterstitialAd


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
        setupAds()
    }

    fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.list_item_detail)

    }

    fun setupAds() {
        val adRequest = AdRequest.Builder().build()
        activityDetailAddView.loadAd(adRequest)
        activityDetailBottomAddView.loadAd(adRequest)

    }

    fun getExtrasFromIntent() {
        imageUrl = intent.getStringExtra(Constants.EXTRA_DETAIL_THUMNAIL_URL)
        binding.gifDetailViewModel = GifDetailViewModel(this, this, imageUrl)
        (binding.gifDetailViewModel as GifDetailViewModel).detailViewVisibility.set(View.VISIBLE)
        binding.animationView.show()
        binding.downloadCardView.hide()
        binding.shareButton.hide()

        Glide.with(binding.detailIv.context)
                .load(imageUrl)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(object : RequestListener<String, GifDrawable> {
                    override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        binding.animationView.hide()
                        binding.downloadCardView.show()
                        binding.shareButton.show()
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

    fun run(f: () -> Unit): Unit {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            f()
        }
    }

    override fun showDialog() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_STORAGE)
    }
}