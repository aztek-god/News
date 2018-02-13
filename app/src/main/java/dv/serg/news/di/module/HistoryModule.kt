package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dao.Dao
import dv.serg.news.di.PerActivity
import dv.serg.news.model.dao.room.database.AppDatabase
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.model.dao.room.entity.Record
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.RecordPresenter


@Module
class HistoryModule(private val view: MvpView<Record>?) {

    private class HistoryDao(appDatabase: AppDatabase) : Dao<History> {
        private val historyDao = appDatabase.getHistoryDao()

        override fun getAll(): List<History> {
            return historyDao.getAll()
        }

        override fun delete(value: History) {
            historyDao.delete(value)
        }

        override fun deleteAll() {
            historyDao.deleteAll()
        }

        override fun insert(value: History) {
            historyDao.insert(value)
        }

        override fun insertAll(values: List<History>) {
            historyDao.insertAll(values)
        }
    }

    @PerActivity
    @Provides
    fun provideRecordDao(appDatabase: AppDatabase): Dao<Record> {
        return object : Dao<Record> {
            private val historyDao = appDatabase.getHistoryDao()

            override fun getAll(): List<Record> {
                return historyDao.getAll()
            }

            override fun delete(value: Record) {
                historyDao.delete(value as History)
            }

            override fun deleteAll() {
                historyDao.deleteAll()
            }

            override fun insert(value: Record) {
                historyDao.insert(value as History)
            }

            override fun insertAll(values: List<Record>) {
                val list: MutableList<History> = ArrayList()
                values.forEach {
                    if (it is History) {
                        list.add(it)
                    }
                }

                historyDao.insertAll(list)
            }
        }
    }

    @PerActivity
    @Provides
    fun provideBookmarkDao(appDatabase: AppDatabase): Dao<History> {
        return HistoryDao(appDatabase)
    }


    @PerActivity
    @Provides
    fun providePresenter(dao: Dao<Record>): RecordPresenter<Record> {
        return RecordPresenter(view!!, dao)
    }
}














