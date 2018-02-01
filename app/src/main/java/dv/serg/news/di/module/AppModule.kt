package dv.serg.news.di.module

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import dv.serg.news.di.PerApplication
import dv.serg.news.model.dao.room.database.AppDatabase

@Module
class AppModule(private val appContext: Application) {
    @PerApplication
    @Provides
    fun provideApplication() = appContext


    @PerApplication
    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "app-database").build()
    }

    @PerApplication
    @Provides
    fun provideFilterList(): MutableList<String> {
        return ArrayList()
    }
}