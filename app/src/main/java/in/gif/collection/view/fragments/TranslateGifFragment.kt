package `in`.gif.collection.view.fragments

import `in`.gif.collection.R
import `in`.gif.collection.databinding.ListItemDetailBinding
import `in`.gif.collection.viewmodel.GifDetailViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception
import java.util.*

/**
 * Created by vivek on 17/09/17.
 */
class TranslateGifFragment : BaseFragment(), Observer {


    private lateinit var binding: ListItemDetailBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.list_item_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gifDetailViewModel = GifDetailViewModel(getFragmentHost())
        (binding.gifDetailViewModel as GifDetailViewModel).translateViewVisibility.set(View.VISIBLE)
        setupObserver()
    }

    fun setupObserver() {
        binding.gifDetailViewModel?.addObserver(this)
    }

    override fun update(p0: Observable?, p1: Any?) {
        when (p0) {
            is GifDetailViewModel -> {
                (binding.gifDetailViewModel as GifDetailViewModel).detailViewVisibility.set(View.VISIBLE)
                Glide.with(binding.detailIv.context)
                        .load(p0.getImageUrl())
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(object : RequestListener<String, GifDrawable> {
                            override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                return false

                            }

                            override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }
                        }).into(binding.detailIv)
            }
        }
    }
}