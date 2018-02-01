package dv.serg.news.model.dao.room.dao

import android.arch.persistence.room.*
import dv.serg.news.model.dao.room.entity.Bookmark
import io.reactivex.Flowable


// todo rewrite it to generic
// todo add additional layer of abstraction
@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAllRx(): Flowable<List<Bookmark>>

    @Query("SELECT * FROM bookmark")
    fun getAll(): List<Bookmark>

    @Query("SELECT * FROM bookmark WHERE id = :id")
    fun getById(id: Long): Bookmark

    @Insert
    fun insert(bookmark: Bookmark)

    @Update
    fun update(bookmark: Bookmark)

    @Query("DELETE FROM bookmark")
    fun deleteAll()

    @Delete
    fun delete(bookmark: Bookmark)
}