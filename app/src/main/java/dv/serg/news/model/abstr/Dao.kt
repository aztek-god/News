package dv.serg.news.model.abstr

interface Dao<in K, V> : ReadDao<K, V> {
    fun insert(value: V)
    fun insertAll(value: List<V>)

    fun deleteAll()
    fun delete(value: V)

    fun update(value: V)
}

