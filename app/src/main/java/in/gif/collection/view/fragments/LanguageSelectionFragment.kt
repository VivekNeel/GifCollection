package `in`.gif.collection.view.fragments

import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.Utils.CommonUtils
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.data.db.StorageService
import `in`.gif.collection.databinding.FragmentLanguageSelectionBinding
import `in`.gif.collection.get
import `in`.gif.collection.model.TempLang
import `in`.gif.collection.set
import `in`.gif.collection.viewmodel.LanguageSelectionViewModel
import agency.tango.materialintroscreen.SlideFragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.InterstitialAd
import java.util.*


/**
 * Created by vivek on 25/09/17.
 */
class LanguageSelectionFragment : SlideFragment(), Observer {
    override fun update(p0: Observable?, p1: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var binding: FragmentLanguageSelectionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_language_selection, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUtils.showInterstitialAds(InterstitialAd(context))
        binding.viewModel = LanguageSelectionViewModel()
        binding.viewModel!!.addObserver(this)
    }

    override fun backgroundColor(): Int {
        return R.color.custom_slide_background
    }

    override fun buttonsColor(): Int {
        return R.color.custom_slide_buttons
    }

    override fun canMoveFurther(): Boolean {
        return StorageService.getLangKey() != null
    }

    override fun cantMoveFurtherErrorMessage(): String {
        return getString(R.string.error_message)
    }
}