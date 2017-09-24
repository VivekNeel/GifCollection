package `in`.gif.collection.model.youtube;

import com.google.gson.annotations.SerializedName

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by vivek on 23/09/17.
 */

open class ItemsData : RealmObject() {

    private var type: String = ""

    var holderType: String = ""

    fun getType(): String {
        return type
    }

    fun setType(type: String) {
        this.type = type
    }

    @PrimaryKey
    @SerializedName("etag")
    var etag: String = ""

    @SerializedName("id")
    var idData: IdData? = null

    @SerializedName("snippet")
    var snippetData: SnippetData? = null

}
