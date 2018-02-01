package dv.serg.lib.dao

import org.jetbrains.anko.doAsync

class AsyncDao<V>(private val dao: Dao<V>) : Dao<V> by dao {

    private fun action(action: () -> Unit) {
        doAsync {
            action.invoke()
        }
    }

    override fun insert(value: V) {
        doAsync {
            dao.insert(value)
        }
    }

    override fun insertAll(values: List<V>) {
        doAsync {
            dao.insertAll(values)
        }
    }

    override fun deleteAll() {
        doAsync {
            dao.deleteAll()
        }
    }

    override fun delete(value: V) {
        doAsync {
            dao.delete(value)
        }
    }

    override fun update(value: V) {
        doAsync {
            dao.update(value)
        }
    }
}

