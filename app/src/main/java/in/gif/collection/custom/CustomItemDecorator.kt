package `in`.gif.collection.custom

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by vivek on 18/09/17.
 */
class CustomItemDecorator(offset: Int) : RecyclerView.ItemDecoration() {

    var halfSpace: Int = offset / 2

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent != null && outRect != null) {
            if (parent.getPaddingLeft() != halfSpace) {
                parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace)
                parent.setClipToPadding(false)
            }

            outRect.top = halfSpace
            outRect.bottom = halfSpace
            outRect.left = halfSpace
            outRect.right = halfSpace
        }
    }
}