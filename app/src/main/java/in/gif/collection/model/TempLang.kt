package `in`.gif.collection.model

import io.realm.RealmObject
import io.realm.RealmQuery

/**
 * Created by vivek on 25/09/17.
 */
open class TempLang : RealmObject() {

     var query: String = ""

     fun getL() : String{
        return query
    }

    fun setL(query: String) {
        this.query = query
    }

}