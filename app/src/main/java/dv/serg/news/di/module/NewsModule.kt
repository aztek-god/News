package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dao.Dao
import dv.serg.news.di.PerActivity
import dv.serg.news.model.dao.room.entity.Bookmark
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.model.dao.room.entity.Source
import dv.serg.news.ui.activity.NewsActivity
import dv.serg.news.ui.presenter.NewsPresenter
import retrofit2.Retrofit

@Module
class NewsModule(private val newsActivity: NewsActivity) {

    @PerActivity
    @Provides
    fun providePresenter(retrofit: Retrofit,
                         historyDao: Dao<History>,
                         sourceDao: Dao<Source>,
                         bookmarkDao: Dao<Bookmark>,
                         filterList: MutableList<String>): NewsPresenter {
        return NewsPresenter(newsActivity, retrofit, sourceDao, historyDao, bookmarkDao, filterList)
    }

//    @PerActivity
//    @Provides
//    fun provideHistoryDao(appDatabase: AppDatabase): Dao<History> {
//        val roomDao = appDatabase.getHistoryDao()
//        return object : Dao<History> {
////            override fun getAll(): List<History>> {
////                return roomDao.getAllRx()
////            }
//
//            override fun deleteAll() {
//                roomDao.deleteAll()
//            }
//
//            override fun insert(value: History) {
//                roomDao.insert(value)
//            }
//
//            override fun delete(value: History) {
//                roomDao.delete(value)
//            }
//        }
//    }


//    @PerActivity
//    @Provides
//    fun provideSourceCodes(dao: Dao<Source>): Flowable<List<String>> {
//        return Flowable.just(dao.getAll())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .flatMap {
//                    val sourceLabels: MutableList<String> = ArrayList()
//                    it.map { it.sourceCode }.toCollection(sourceLabels)
//                    Flowable.just(sourceLabels.toList())
//                }
//    }
}