package com.gifs.collection.view

import android.content.Intent
import android.preference.PreferenceManager
import android.support.v4.app.TaskStackBuilder
import com.gifs.collection.MainActivity
import android.support.v7.app.AppCompatActivity
import com.gifs.collection.Constants
import com.gifs.collection.Utils.PreferenceHelper


/**
 * Created by vivek on 30/09/17.
 */
class SplashActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        val isIntroDone = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.KEY_INTRO_DONE , false)
        if (isIntroDone) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            TaskStackBuilder.create(this)
                    .addNextIntent(Intent(this, IntroActivity::class.java))
                    .startActivities()
        }
    }
}