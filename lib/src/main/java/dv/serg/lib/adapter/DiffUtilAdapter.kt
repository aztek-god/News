package dv.serg.lib.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class DiffUtilAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    fun calculateDiff(oldData: List<T>, newData: List<T>) {
        DiffUtil.calculateDiff(DiffUtilCallback(oldData, newData)).dispatchUpdatesTo(this)
    }

    open fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem

    open fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem

    open fun getChangePayload(any: Any?, oldItemPosition: Int, newItemPosition: Int): Any? {
        return any
    }


    private inner class DiffUtilCallback(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return this@DiffUtilAdapter.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return this@DiffUtilAdapter.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }

//        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
//            return this@DiffUtilAdapter.getChangePayload(super.getChangePayload(oldItemPosition, newItemPosition),
//                    oldItemPosition, newItemPosition)
//        }
    }


}