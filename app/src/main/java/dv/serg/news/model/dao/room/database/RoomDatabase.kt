package dv.serg.news.model.dao.room.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import dv.serg.news.model.dao.room.dao.BookmarkDao
import dv.serg.news.model.dao.room.dao.HistoryDao
import dv.serg.news.model.dao.room.dao.SourceDao
import dv.serg.news.model.dao.room.entity.Bookmark
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.model.dao.room.entity.Source

@Database(entities = [(Source::class), History::class, Bookmark::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getSourceDao(): SourceDao
    abstract fun getBookmarkDao(): BookmarkDao
    abstract fun getHistoryDao(): HistoryDao
}