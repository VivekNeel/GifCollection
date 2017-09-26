package `in`.gif.collection.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vivek on 26/09/17.
 */
object RxBus{

    private val publisher = PublishSubject.create<Any>()

    fun sendEvent(`object`: Any) {
        if (publisher.hasObservers()) {
            publisher.onNext(`object`)
        }
    }

    fun toObservable(): Observable<Any> {
        return publisher
    }
}