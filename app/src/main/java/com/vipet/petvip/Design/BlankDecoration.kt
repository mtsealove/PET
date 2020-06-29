package com.vipet.petvip.Design

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

// rycycler view 간격 조절
class BlankDecoration(val height: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        super.getItemOffsets(outRect, itemPosition, parent)
        outRect.top = height
    }
}