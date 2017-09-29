package `in`.gif.collection.data.db

import `in`.gif.collection.model.TempLang
import `in`.gif.collection.model.youtube.ItemsData
import io.realm.Realm
import io.realm.RealmObject

/**
 * Created by vivek on 24/09/17.
 */
object StorageService {

    fun <T : RealmObject> putListIntoDB(item: List<T>) {
        Realm.getDefaultInstance().use { realmInstance -> realmInstance.executeTransaction { realm -> realm.insertOrUpdate(item) } }

    }

    fun <T : RealmObject> putDataIntoDB(item: T) {
        Realm.getDefaultInstance().use { realmInstance -> realmInstance.executeTransaction { realm -> realm.insertOrUpdate(item) } }

    }

    fun getVideosFromDb(type: String): List<ItemsData> {
        val realm: Realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        var list : List<ItemsData> ;
        if(type.isNullOrEmpty()){
           list=  realm.copyFromRealm(realm.where(ItemsData::class.java).findAll())
        } else {
            list = realm.copyFromRealm(realm.where(ItemsData::class.java).equalTo("type", type).findAll())
        }
        realm.commitTransaction()
        return list
    }

    fun getLangKey(): TempLang? {
        val realm: Realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val list = realm.where(TempLang::class.java).findFirst()
        realm.commitTransaction()
        return list
    }
}