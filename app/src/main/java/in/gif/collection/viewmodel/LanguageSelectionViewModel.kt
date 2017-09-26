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
            }
            R.id.hindi -> {
                data.setL("hindi")
            }

            R.id.english -> {
                data.setL("english")
            }
            R.id.marathi -> {
                data.setL("marathi")
            }
            R.id.punjabi -> {
                data.setL("punjabi")
            }

            R.id.malayalam -> {
                data.setL("malayalam")
            }
            R.id.gujarthi -> {
                data.setL("gujrathi")
            }
            R.id.bihari -> {
                data.setL("bihari")
            }
        }
        StorageService.putDataIntoDB(data)
    }
}