package dv.serg.lib.dao

import io.reactivex.Flowable

interface Dao<V> {

    // read operations
    fun get(id: Long): V {
        throw Exception("This operation is not supported.")
    }

    fun getAll(): Flowable<List<V>> {
        throw Exception("This operation is not supported.")
    }

    // crud operations
    fun insert(value: V) {
        throwNotSupported()
    }

    fun insertAll(values: List<V>) {
        throwNotSupported()
    }

    fun update(value: V) {
        throwNotSupported()
    }

    fun delete(value: V) {
        throwNotSupported()
    }

    fun deleteAll() {
        throwNotSupported()
    }

    private fun throwNotSupported() {
        throw Exception("This operation is not supported.")
    }
}