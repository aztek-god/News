package dv.serg.lib.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.lib.collection.ObservableList

open class StandardAdapter<T, VH>(private val layoutId: Int, private val initView: (View) -> VH,
                                  private val data: ObservableList<T> = ObservableList()) :
        DiffUtilAdapter<T, VH>(), MutableList<T> by data, ObservableList.ChangesObserver<T>
        where VH : RecyclerView.ViewHolder, VH : StandardAdapter.BindViewHolder<T, VH> {

    init {
        data.registerObserver(this)
    }

    var onClickListener: OnClickListener<T> = object : OnClickListener<T> {}

    interface OnClickListener<T> {
        fun onClick(data: T, position: Int) {}
        fun onLongClick(data: T, position: Int) {}
    }

    interface BindViewHolder<T, in VH> {
        fun onBind(position: Int, item: T)
    }

    // todo fix observeChanges
    override fun observeChanges(oldData: List<T>, newData: List<T>) {
        calculateDiff(oldData, newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(parent!!.context).inflate(layoutId, parent, false)
        return initView.invoke(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(position, data[position])
    }
}