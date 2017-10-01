package com.gifs.collection.view

import agency.tango.materialintroscreen.SlideFragmentBuilder
import agency.tango.materialintroscreen.MessageButtonBehaviour
import android.os.Bundle
import agency.tango.materialintroscreen.MaterialIntroActivity
import android.Manifest;
import android.content.Intent
import android.support.annotation.Nullable;
import android.view.View;
import com.gifs.collection.Constants
import com.gifs.collection.MainActivity
import com.gifs.collection.R
import com.gifs.collection.Utils.CommonUtils
import com.gifs.collection.Utils.PreferenceHelper
import com.gifs.collection.set
import com.google.android.gms.ads.InterstitialAd


/**
 * Created by vivek on 30/09/17.
 */
class IntroActivity : MaterialIntroActivity() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLastSlideAlphaExitTransition(true)

        CommonUtils.showInterstitialAds(InterstitialAd(this))
        backButtonTranslationWrapper
                .setEnterTranslation { view, percentage -> view.alpha = percentage }


        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .neededPermissions(arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .title("Storage Permission")
                .description("This permission is required to be able to share gifs across all messaging apps")
                .build(),
                MessageButtonBehaviour(View.OnClickListener { showMessage("Cool!") }, "Awesome"))

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorTrending)
                .buttonsColor(R.color.colorAccentSecondary)
                .title("That's it")
                .description("Enjoy awesome collection of gifs.")
                .build())
    }

    override fun onFinish() {
        super.onFinish()
        PreferenceHelper.defaultPrefs(this).set(Constants.KEY_INTRO_DONE, true)
        startActivity(Intent(this, MainActivity::class.java))
    }
}