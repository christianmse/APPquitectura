package com.etsisi.appquitectura.presentation.components

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.etsisi.appquitectura.presentation.common.StickyHeaderListener

class RankingItemDecoration(private val mListener: StickyHeaderListener): RecyclerView.ItemDecoration() {

    private var mStickyHeaderHeight = 0

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        with(parent.getChildAt(0)) {
            if (this == null) return

            if (parent.getChildAdapterPosition(this) == RecyclerView.NO_POSITION) return

            val headerPos = mListener.getHeaderPositionForItem(parent.getChildAdapterPosition(this))

            getHeaderViewForItem(headerPos, parent)?.let { currentHeader ->
                fixLayoutSize(parent, currentHeader)

                getChildInContact(parent, currentHeader.bottom, headerPos)?.let { childContact ->
                    if (mListener.isHeader(parent.getChildAdapterPosition(childContact))) {
                        moveHeader(c, currentHeader, childContact)
                        return
                    }
                }
                drawHeader(c, currentHeader)
            }
        }
    }

    private fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View? {
        var header: View? = LayoutInflater.from(parent.context).inflate(mListener.getHeaderLayout(headerPosition), parent, false)
        mListener.bindHeaderData(header, headerPosition)
        return header
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(
        parent: RecyclerView,
        contactPoint: Int,
        currentHeaderPos: Int
    ): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            var heightTolerance = 0
            val child: View = parent.getChildAt(i)

            //measure height tolerance with child if child is another header
            if (currentHeaderPos != i) {
                val isChildHeader = mListener.isHeader(parent.getChildAdapterPosition(child))
                if (isChildHeader) {
                    heightTolerance = mStickyHeaderHeight - child.getHeight()
                }
            }

            //add heightTolerance if child top be in display area
            var childBottomPosition: Int
            childBottomPosition = if (child.getTop() > 0) {
                child.getBottom() + heightTolerance
            } else {
                child.getBottom()
            }
            if (childBottomPosition > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight.also { mStickyHeaderHeight = it })
    }
}