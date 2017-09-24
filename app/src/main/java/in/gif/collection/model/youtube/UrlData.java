package in.gif.collection.model.youtube;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by vivek on 23/09/17.
 */

public class UrlData extends RealmObject {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
