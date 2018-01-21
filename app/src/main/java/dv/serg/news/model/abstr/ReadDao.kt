package dv.serg.news.model.abstr

interface ReadDao<in K, out V> {
    fun getById(id: K): V
    fun getAll(): List<V>
}