package dv.serg.news.recycler.abstr

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class DiffUtilAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected abstract val oldData: List<T>
    private lateinit var newData: List<T>

    private val diffCalc = fun() {
        DiffUtil.calculateDiff(DiffUtilCallback(oldData, newData)).dispatchUpdatesTo(this)
    }

    open fun updateData(data: List<T>) {
        newData = data
        diffCalc()
    }

    private class DiffUtilCallback<T>(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}