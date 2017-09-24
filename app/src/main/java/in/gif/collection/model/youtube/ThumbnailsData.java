package in.gif.collection.model.youtube;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by vivek on 23/09/17.
 */

public class ThumbnailsData extends RealmObject {

    @SerializedName("medium")
    private UrlData medium;

    public UrlData getMedium() {
        return medium;
    }

    public void setMedium(UrlData medium) {
        this.medium = medium;
    }
}
