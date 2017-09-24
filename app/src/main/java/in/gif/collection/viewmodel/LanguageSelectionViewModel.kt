package `in`.gif.collection.viewmodel

import `in`.gif.collection.Constants
import `in`.gif.collection.R
import `in`.gif.collection.Utils.PreferenceHelper
import `in`.gif.collection.data.db.StorageService
import `in`.gif.collection.model.TempLang
import `in`.gif.collection.set
import android.databinding.BaseObservable
import android.view.View
import java.util.*

/**
 * Created by vivek on 25/09/17.
 */
class LanguageSelectionViewModel : Observable() {

    fun onRadioButtonClicked(view: View) {
        val data = TempLang()
        val activity = view.context
        when (view.id) {
            R.id.tamil -> {
                data.setL("tamil")
                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "tamil"
            }
            R.id.hindi -> {
                data.setL("hindi")

                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "hindi"
            }

            R.id.english -> {
                data.setL("english")
                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "english"
            }
            R.id.marathi -> {
                data.setL("marathi")
                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "marathi"
            }
            R.id.punjabi -> {
                data.setL("punjabi")
                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "punjabi"
            }

            R.id.malayalam -> {
                data.setL("malayalam")
                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "malayalam"
            }
            R.id.gujarthi -> {
                data.setL("gujrathi")
                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "gujrathi"
            }
            R.id.bihari -> {
                data.setL("bihari")
                PreferenceHelper.defaultPrefs(activity)[Constants.KEY_LANGUAGE] = "bihari"
            }
        }
        StorageService.putDataIntoDB(data)
    }
}