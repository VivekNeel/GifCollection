package com.gifs.collection.view

import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import com.gifs.collection.MainActivity
import android.support.v7.app.AppCompatActivity
import com.gifs.collection.Constants


/**
 * Created by vivek on 30/09/17.
 */
class SplashActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        val isIntroDone : Boolean = this.getSharedPreferences("custom" , android.content.Context.MODE_PRIVATE).getBoolean(Constants.KEY_INTRO_DONE , false)
        if (isIntroDone) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            TaskStackBuilder.create(this)
                    .addNextIntent(Intent(this, IntroActivity::class.java))
                    .startActivities()

        }
    }
}