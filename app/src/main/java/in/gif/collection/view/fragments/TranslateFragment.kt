package `in`.gif.collection.view.fragments

import `in`.gif.collection.*
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.databinding.LayoutRandomTranslateGifBinding
import `in`.gif.collection.viewmodel.GifDetailViewModel
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
import kotlinx.android.synthetic.main.layout_random_translate_gif.*
import java.lang.Exception
import java.util.*

/**
 * Created by vivek on 19/09/17.
 */
class TranslateFragment : BaseFragment(), Observer, ShowDialogCallback {

    lateinit var binding: LayoutRandomTranslateGifBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_random_translate_gif, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        labelText.text = "Translate your words/phrases to gif by searching.."
        randomSubmitCardView.hide()
        translateInputlayout.hint = "Enter word/phrase"
        val dbUrl: String = PreferenceHelper.defaultPrefs(getFragmentHost())[Constants.KEY_TRANSLATE_GIF_URL]
        if (!TextUtils.isEmpty(dbUrl)) {
            binding.gifDetailViewModel?.noGifContainerVisibility?.set(View.GONE)
            loadGif(dbUrl)
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
                        downloadCardView.show()
                        return false
                    }

                    override fun onException(e: Exception?, model: String?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                        binding.detailIv.hide()
                        return false
                    }
                }).into(binding.detailIv)
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

    fun setupObserver() {
        binding.gifDetailViewModel = GifDetailViewModel(getFragmentHost(), this, PreferenceHelper.defaultPrefs(getFragmentHost())[Constants.KEY_TRANSLATE_GIF_URL])
        binding.gifDetailViewModel?.addObserver(this)
    }

    override fun showDialog() {
        ActivityCompat.requestPermissions(getFragmentHost(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.REQUEST_CODE_STORAGE)
    }
}