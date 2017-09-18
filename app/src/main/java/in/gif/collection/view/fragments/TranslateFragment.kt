package `in`.gif.collection.view.fragments

import `in`.gif.collection.R
import `in`.gif.collection.databinding.LayoutRandomTranslateGifBinding
import `in`.gif.collection.hide
import `in`.gif.collection.hideKeyboard
import `in`.gif.collection.show
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
import kotlinx.android.synthetic.main.layout_random_translate_gif.*
import java.lang.Exception
import java.util.*

/**
 * Created by vivek on 19/09/17.
 */
class TranslateFragment : BaseFragment(), Observer {

    lateinit var binding: LayoutRandomTranslateGifBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_random_translate_gif, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        labelText.text = "Translate your words/phrase to gif by searching.."
        randomSubmitCardView.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance(bundle: Bundle): RandomGifFragment {
            val fragment = RandomGifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun update(p0: Observable?, p1: Any?) {
        when (p0) {
            is GifDetailViewModel -> {
                getFragmentHost().hideKeyboard()
                binding.progress.visibility = View.VISIBLE
                Glide.with(binding.detailIv.context)
                        .load(p0.getImageUrl())
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(object : RequestListener<String, GifDrawable> {
                            override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                binding.progress.visibility = View.GONE
                                downloadCardView.show()
                                return false
                            }

                            override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }
                        }).into(binding.detailIv)
            }
        }
    }

    fun setupObserver() {
        binding.gifDetailViewModel = GifDetailViewModel(getFragmentHost())
        binding.gifDetailViewModel?.addObserver(this)
    }
}