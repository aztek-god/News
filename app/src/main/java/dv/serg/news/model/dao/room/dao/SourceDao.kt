package dv.serg.news.model.dao.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import dv.serg.news.model.dao.room.entity.Source
import io.reactivex.Flowable

@Dao
interface SourceDao {
    @Query("SELECT * FROM source")
    fun getAll(): Flowable<List<Source>>

    @Query("DELETE FROM source")
    fun deleteAll()

    @Insert
    fun insert(value: Source)

    @Insert
    fun insertAll(values: List<Source>)
}