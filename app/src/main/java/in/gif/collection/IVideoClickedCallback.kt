package `in`.gif.collection

import `in`.gif.collection.model.youtube.ItemsData

/**
 * Created by vivek on 24/09/17.
 */
interface IVideoClickedCallback {
    fun onItemClicked(itemsData: ItemsData)
}