package dv.serg.news.recycler.abstr

import android.support.v7.widget.RecyclerView

abstract class AbstractAdapter<T, VH : RecyclerView.ViewHolder> : DiffUtilAdapter<T, VH>(), RecyclerAdapter<T> {
    private var newData: MutableList<T> = ArrayList()
    override var oldData: List<T> = ArrayList()

    override fun clear() {
        newData = mutableListOf()
        super.updateData(newData)
        oldData = newData
    }

    override fun updateData(data: List<T>) {
        oldData = data
        newData = data.toMutableList()
        super.updateData(newData)
        oldData = newData
    }

    override fun appendData(data: List<T>) {
        super.updateData(oldData + data)
        oldData += data
    }

    override fun removeAt(index: Int) {
        val _newData: MutableList<T> = ArrayList()
        for (loopIndex in 0 until oldData.size) {
            if (loopIndex != index) {
                _newData.add(oldData[loopIndex])
            }
        }
        newData = _newData
        super.updateData(newData)
        oldData = newData
    }

    override fun set(index: Int, item: T) {
        newData = oldData.toMutableList()
        newData[index] = item
        super.updateData(newData)
        oldData = newData
    }

    override fun notifyDataChanged() {
        notifyDataSetChanged()
    }

    override fun get(index: Int): T {
        return oldData[index]
    }
}