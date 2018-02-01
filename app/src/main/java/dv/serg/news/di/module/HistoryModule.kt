package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dao.Dao
import dv.serg.news.di.PerActivity
import dv.serg.news.model.dao.room.database.AppDatabase
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.HistoryPresenter
import io.reactivex.Flowable

@Module
class HistoryModule(private val context: MvpView<History>) {

    @PerActivity
    @Provides
    fun provideHistoryDao(appDatabase: AppDatabase): Dao<History> {
        return object : Dao<History> {
            override fun getAll(): Flowable<List<History>> {
                return appDatabase.getHistoryDao().getAllRx()
            }
        }
    }

    @PerActivity
    @Provides
    fun providePresenter(dao: Dao<History>): HistoryPresenter {
        return HistoryPresenter(context, dao)
    }
}
