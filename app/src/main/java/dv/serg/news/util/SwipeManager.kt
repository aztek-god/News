package dv.serg.news.util

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class SwipeManager(private val directions: Int, private val swipeCallback: SwipeCallback) {

    interface SwipeCallback {
        fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean
        fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int)
    }

    private inner class PrivateSwipeCallback : ItemTouchHelper.SimpleCallback(0, directions) {
        override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
            return swipeCallback.onMove(recyclerView, viewHolder, target)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
            swipeCallback.onSwiped(viewHolder, direction)
        }
    }

    fun attachToRecycler(recycler: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(PrivateSwipeCallback())
        itemTouchHelper.attachToRecyclerView(recycler)
    }
}