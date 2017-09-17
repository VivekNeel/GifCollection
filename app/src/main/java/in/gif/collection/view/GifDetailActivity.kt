package `in`.gif.collection.view

import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.databinding.ListItemDetailBinding
import `in`.gif.collection.viewmodel.GifDetailViewModel
import `in`.gif.collection.model.TrendingGifResponse
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception


/**
 * Created by vivek on 15/09/17.
 */
class GifDetailActivity : AppCompatActivity() {

    private lateinit var binding: ListItemDetailBinding
    private lateinit var imageUrl: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(true) }
        getExtrasFromIntent()
    }

    fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.list_item_detail)

    }

    fun getExtrasFromIntent() {
        imageUrl = intent.getStringExtra(Constants.EXTRA_DETAIL_THUMNAIL_URL)
        binding.gifDetailViewModel = GifDetailViewModel(imageUrl, this)
        binding.progress.visibility = View.VISIBLE
        binding.detailIv.visibility = View.VISIBLE
        binding.detailIvThumbnail.visibility = View.GONE

        Glide.with(binding.detailIv.context)
                .load(imageUrl)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(object : RequestListener<String, GifDrawable> {
                    override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        binding.progress.visibility = View.GONE
                        return false

                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {

                        return false
                    }
                }).into(binding.detailIv)


    }

    companion object {
        fun launchDetail(context: Context, gifData: TrendingGifResponse): Intent {
            val intent = Intent(context, GifDetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_DETAIL_IMAGE_URL, gifData.images.fixedWidth.url)
            intent.putExtra(Constants.EXTRA_DETAIL_THUMNAIL_URL, gifData.images.fixedHeightGifs.url)
            return intent
        }
    }

    fun run(f: () -> Unit): Unit {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            f()
        }
    }
}