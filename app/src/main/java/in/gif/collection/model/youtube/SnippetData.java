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

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("channelTitle")
    private String channelTitle;

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

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
