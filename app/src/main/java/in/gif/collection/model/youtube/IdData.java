package in.gif.collection.model.youtube;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by vivek on 23/09/17.
 */

public class IdData extends RealmObject {

    @SerializedName("videoId")
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
