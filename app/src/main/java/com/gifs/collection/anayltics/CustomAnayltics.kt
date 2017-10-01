package com.gifs.collection.anayltics

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.crashlytics.android.answers.SearchEvent
import com.crashlytics.android.answers.ShareEvent

/**
 * Created by vivek on 01/10/17.
 */
object CustomAnayltics {

    fun logShare(method: String, contentType: String) {
        Answers.getInstance().logShare(ShareEvent()
                .putMethod(method)
                .putContentType(contentType))

    }

    fun logSearch(query: String) {
        Answers.getInstance().logSearch(SearchEvent().putQuery(query))
    }

    fun logCustom(type: String) {
        Answers.getInstance().logCustom(CustomEvent(type))
    }

}