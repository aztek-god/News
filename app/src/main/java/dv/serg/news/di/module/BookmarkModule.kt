package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dao.Dao
import dv.serg.news.di.PerActivity
import dv.serg.news.model.dao.room.database.AppDatabase
import dv.serg.news.model.dao.room.entity.Bookmark
import io.reactivex.Flowable

@Module
class BookmarkModule {

    @PerActivity
    @Provides
    fun provideBookmarkDao(appDatabase: AppDatabase): Dao<Bookmark> {
        return object : Dao<Bookmark> {
            override fun getAll(): Flowable<List<Bookmark>> {
                return appDatabase.getBookmarkDao().getAllRx()
            }

            override fun insert(value: Bookmark) {
                appDatabase.getBookmarkDao().insert(value)
            }
        }
    }

//    @PerActivity
//    @Provides
//    fun providePresenter(dao: Dao<Bookmark>): BookmarkPresenter {
//        return BookmarkPresenter(context, dao)
//    }
}
