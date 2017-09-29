package in.gif.collection;

/**
 * Created by vivek on 26/09/17.
 */

public class DownloadItem {

    long id;
    int itemDownloadPercent;
    String downloadStatus;

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getItemDownloadPercent() {
        return itemDownloadPercent;
    }

    public void setItemDownloadPercent(int itemDownloadPercent) {
        this.itemDownloadPercent = itemDownloadPercent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
