package `in`.gif.collection.data.db

import `in`.gif.collection.model.youtube.ItemsData
import io.realm.Realm
import io.realm.RealmObject

/**
 * Created by vivek on 24/09/17.
 */
object StorageService{

    fun <T : RealmObject> putListIntoDB(item: List<T>) {
        Realm.getDefaultInstance().use { realmInstance -> realmInstance.executeTransaction { realm -> realm.insertOrUpdate(item) } }

    }

    fun <T : RealmObject> putDataIntoDB(item: T) {
        Realm.getDefaultInstance().use { realmInstance -> realmInstance.executeTransaction { realm -> realm.insertOrUpdate(item) } }

    }

    fun getVideosFromDb(): List<ItemsData> {
        val realm: Realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        var list = realm.copyFromRealm(realm.where(ItemsData::class.java).findAll())
        realm.commitTransaction()
        return list
    }
}