package dv.serg.news.model.dao.room.dao

import android.arch.persistence.room.*
import dv.serg.news.model.dao.room.entity.History
import io.reactivex.Flowable

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAllRx(): Flowable<List<History>>

    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Query("SELECT * FROM history WHERE id = :id")
    fun getById(id: Long): Flowable<List<History>>

    @Insert
    fun insert(history: History)

    @Update
    fun update(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

    @Delete
    fun delete(history: History)

}