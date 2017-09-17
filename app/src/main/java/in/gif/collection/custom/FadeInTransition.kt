package `in`.gif.collection.custom

import android.transition.AutoTransition
import android.transition.Transition

/**
 * Created by vivek on 17/09/17.
 */
class FadeInTransition : AutoTransition() {

    companion object {
        const val duration: Long = 200
        fun createTransition(): Transition {
            val autoTransition = AutoTransition()
            autoTransition.duration = duration
            return autoTransition
        }
    }
}