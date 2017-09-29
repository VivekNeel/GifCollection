package in.gif.collection;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by vivek on 26/09/17.
 */

public enum ItemDownloadPercentEmitter {
    INSTANCE;
    private static String TAG = ItemDownloadPercentEmitter.class.getSimpleName();

    private final static int DOWNLOAD_QUERY_DELAY_PARAM = 400;
    private final static int DOWNLOAD_QUERY_LONG_DELAY_PARAM = 2000;
    private final static int PERCENT_MULTIPLIER = 100;
    Observable observable;

    public static ItemDownloadPercentEmitter getInstance() {
        return INSTANCE;
    }

    public void addDownloadId(Context context, long lectureId) {
        Log.d("lectureId", String.valueOf(lectureId));
        getObservable(context, lectureId);
    }


    ItemDownloadPercentEmitter() {
    }

    public Observable<DownloadItem> getObservable(final Context context, final long lectureId) {
            observable = Observable.create(new ObservableOnSubscribe<DownloadItem>() {

                @Override
                public void subscribe(@NonNull ObservableEmitter<DownloadItem> e) throws Exception {
                    try {
                        DownloadManager.Query query = new DownloadManager.Query();
                        while (true) {

                            DownloadItem downloadItem = new DownloadItem();
                            query.setFilterById(lectureId);
                            Cursor cursor = null;
                            try {
                                cursor = new CustomDownloadManager(context).getDownloadManager().query(query);
                                if (cursor == null || !cursor.moveToFirst()) {
                                    e.onNext(downloadItem);
                                }
                                //Get the download percent
                                float bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                float bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                int downloadPercent = (int) ((bytesDownloaded / bytesTotal) * PERCENT_MULTIPLIER);
                                downloadItem.setItemDownloadPercent(downloadPercent);
                                Log.d(TAG, " downloadPercent :" + downloadPercent);
                                //check if download is complete
                                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                    downloadItem.setDownloadStatus("success");
                                    e.onNext(downloadItem);
                                    break;
                                } else {
                                    downloadItem.setDownloadStatus("progress");
                                    e.onNext(downloadItem);
                                }
                            } catch (Exception exception) {

                                e.onNext(downloadItem);
                                break;
                            } finally {
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                        }
                        Thread.sleep(DOWNLOAD_QUERY_DELAY_PARAM);

                    } catch (InterruptedException exp) {

                        Log.d("int" , exp.getMessage());
                    }

                }
            } );
        return observable;
    }

}
