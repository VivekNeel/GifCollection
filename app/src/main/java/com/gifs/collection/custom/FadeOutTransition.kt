package com.gifs.collection.custom

import android.transition.AutoTransition
import android.transition.Transition

/**
 * Created by vivek on 17/09/17.
 */
class FadeOutTransition : AutoTransition() {
    companion object {
        private const val FADE_OUT_DURATION : Long = 250
        fun withAction(callback : TransitionListener) : Transition{
            val autoTransition = AutoTransition()
            autoTransition.addListener(callback)
            autoTransition.duration = FADE_OUT_DURATION
            return autoTransition
        }
    }


}