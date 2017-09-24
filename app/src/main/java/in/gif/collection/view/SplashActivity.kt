package `in`.gif.collection.view

import `in`.gif.collection.Constants
import android.content.Intent
import `in`.gif.collection.MainActivity
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.data.db.StorageService
import `in`.gif.collection.get
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity


/**
 * Created by vivek on 25/09/17.
 */
class SplashActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        var value: String? = null
        if (StorageService.getLangKey() != null) {
            value = StorageService.getLangKey()?.getL()
        }
        if (value.isNullOrEmpty()) {
            TaskStackBuilder.create(this)
                    .addNextIntent(Intent(this, IntroActivity::class.java))
                    .startActivities()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}