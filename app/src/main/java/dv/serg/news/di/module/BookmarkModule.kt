package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dao.Dao
import dv.serg.news.di.PerActivity
import dv.serg.news.model.dao.room.database.AppDatabase
import dv.serg.news.model.dao.room.entity.Bookmark
import dv.serg.news.model.dao.room.entity.Record
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.RecordPresenter

@Module
class BookmarkModule(private val view: MvpView<Record>?) {

    private class BookmarkDao(appDatabase: AppDatabase) : Dao<Bookmark> {
        private val bookmarkDao = appDatabase.getBookmarkDao()

        override fun getAll(): List<Bookmark> {
            return bookmarkDao.getAll()
        }

        override fun delete(value: Bookmark) {
            bookmarkDao.delete(value)
        }

        override fun deleteAll() {
            bookmarkDao.deleteAll()
        }

        override fun insert(value: Bookmark) {
            bookmarkDao.insert(value)
        }
    }

    @PerActivity
    @Provides
    fun provideRecordDao(appDatabase: AppDatabase): Dao<Record> {
        return object : Dao<Record> {
            private val bookmarkDao = appDatabase.getBookmarkDao()

            override fun getAll(): List<Record> {
                return bookmarkDao.getAll()
            }

            override fun delete(value: Record) {
                bookmarkDao.delete(value as Bookmark)
            }

            override fun deleteAll() {
                bookmarkDao.deleteAll()
            }

            override fun insert(value: Record) {
                bookmarkDao.insert(value as Bookmark)
            }

            override fun insertAll(values: List<Record>) {
                val list: MutableList<Bookmark> = ArrayList()
                values.forEach {
                    if (it is Bookmark) {
                        list.add(it)
                    }
                }

                bookmarkDao.insertAll(list)
            }
        }
    }

    @PerActivity
    @Provides
    fun provideBookmarkDao(appDatabase: AppDatabase): Dao<Bookmark> {
        return BookmarkDao(appDatabase)
    }


    @PerActivity
    @Provides
    fun providePresenter(dao: Dao<Record>): RecordPresenter<Record> {
        return RecordPresenter(view!!, dao)
    }
}
