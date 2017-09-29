package in.gif.collection;

/**
 * Created by vivek on 26/09/17.
 */

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class CustomDownloadManager {
    static String TAG = CustomDownloadManager.class.getName();
    private DownloadManager downloadManager;

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public CustomDownloadManager(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
    }

    public Long enqueue(@NonNull Uri uri, @NonNull String id , @NonNull String name) {
        DownloadManager.Request request = new DownloadManager.Request(uri);
        File root = Environment.getExternalStorageDirectory();
        File myDir = new File(root + "/GifVideos");
        File path = new File(myDir, id + ".mp4");

        request.setDescription("Downloading..");
        request.setTitle(name);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(path));

        long downloadId = downloadManager.enqueue(request);
        return downloadId;
    }

}
