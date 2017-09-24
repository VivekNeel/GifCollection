package `in`.gif.collection.view

import `in`.gif.collection.MainActivity
import `in`.gif.collection.R
import `in`.gif.collection.view.fragments.LanguageSelectionFragment
import android.widget.Toast
import agency.tango.materialintroscreen.SlideFragmentBuilder
import agency.tango.materialintroscreen.MessageButtonBehaviour
import android.os.Bundle
import agency.tango.materialintroscreen.MaterialIntroActivity
import android.Manifest
import android.content.Intent
import android.support.annotation.Nullable
import android.view.View


/**
 * Created by vivek on 25/09/17.
 */
class IntroActivity : MaterialIntroActivity() {


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLastSlideAlphaExitTransition(true)

        backButtonTranslationWrapper
                .setEnterTranslation { view, percentage -> view.alpha = percentage }


        addSlide(LanguageSelectionFragment())

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.third_slide_background)
                .buttonsColor(R.color.third_slide_buttons)
                .neededPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .title("Now Download best what's app status videos easily.")
                .description("Please give permission to download what's app status on your phone")
                .build(),
                MessageButtonBehaviour(View.OnClickListener { showMessage("Cool!") }, "Awesome"))

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.fourth_slide_background)
                .buttonsColor(R.color.fourth_slide_buttons)
                .title("That's it")
                .description("Now enjoy!")
                .build())
    }

    override fun onFinish() {
        super.onFinish()
        val intent = Intent(this , MainActivity::class.java)
        startActivity(intent)
    }
}