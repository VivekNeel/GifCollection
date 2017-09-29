package com.gifs.collection.view.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gifs.collection.*
import com.gifs.collection.Utils.PreferenceHelper
import com.gifs.collection.databinding.LayoutRandomTranslateGifBinding
import com.gifs.collection.viewmodel.GifDetailViewModel
import kotlinx.android.synthetic.main.layout_random_translate_gif.*
import java.lang.Exception
import java.util.*

/**
 * Created by vivek on 17/09/17.
 */
class RandomGifFragment : BaseFragment(), Observer, ShowDialogCallback {

    lateinit var binding: LayoutRandomTranslateGifBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_random_translate_gif, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        labelText.text = "Try searching for random nano by tag.."
        submitCardView.hide()
        translateInputlayout.hint = "Enter tag"
        val dbUrl: String = PreferenceHelper.defaultPrefs(getFragmentHost())[Constants.KEY_RANDOM_GIF_URL]
        if (!TextUtils.isEmpty(dbUrl)) {
            binding.gifDetailViewModel?.noGifContainerVisibility?.set(View.GONE)
            loadGif(dbUrl)
        }

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
                loadGif(p0.getImageUrl())
            }
        }
    }

    fun loadGif(url: String) {
        if (TextUtils.isEmpty(url)) {
            binding.detailIv.hide()
        }
        Glide.with(binding.detailIv.context)
                .load(url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(object : RequestListener<String, GifDrawable> {
                    override fun onResourceReady(resource: GifDrawable?, model: String?, target: Target<GifDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        binding.progress.visibility = View.GONE
                        downloadCardView?.show()
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                }).into(binding.detailIv)
    }

    fun setupObserver() {
        binding.gifDetailViewModel = GifDetailViewModel(getFragmentHost(), this, PreferenceHelper.defaultPrefs(getFragmentHost())[Constants.KEY_RANDOM_GIF_URL])
        binding.gifDetailViewModel?.addObserver(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showDialog() {
        ActivityCompat.requestPermissions(getFragmentHost(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_STORAGE)
    }

}