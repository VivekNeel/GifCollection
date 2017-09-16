package `in`.gif.collection

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Created by vivek on 15/09/17.
 */
fun ImageView.loadImage(url: String) {
    Glide.with(context)
            .load(url).asGif().into(this)
}
