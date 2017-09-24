package in.gif.collection.model.youtube;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * Created by vivek on 23/09/17.
 */

public class YoutubeSearchResponse {


    @SerializedName("etag")
    private String etag;

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    @SerializedName("items")
    private List<ItemsData> itemsData;

    public List<ItemsData> getItemsData() {
        return itemsData;
    }

    public void setItemsData(List<ItemsData> itemsData) {
        this.itemsData = itemsData;
    }
}
