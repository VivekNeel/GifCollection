package in.gif.collection.model.youtube;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by vivek on 23/09/17.
 */

public class SnippetData extends RealmObject {

    @SerializedName("title")
    private String title;

    @SerializedName("thumbnails")
    private ThumbnailsData thumbnails;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ThumbnailsData getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ThumbnailsData thumbnails) {
        this.thumbnails = thumbnails;
    }
}
