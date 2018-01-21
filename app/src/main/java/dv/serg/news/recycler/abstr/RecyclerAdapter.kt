package dv.serg.news.recycler.abstr

interface RecyclerAdapter<T> {

    fun clear()

    fun updateData(data: List<T>)

    fun appendData(data: List<T>)

    fun removeAt(index: Int)

    fun set(index: Int, item: T)

    fun notifyDataChanged()

    operator fun get(index: Int): T
}